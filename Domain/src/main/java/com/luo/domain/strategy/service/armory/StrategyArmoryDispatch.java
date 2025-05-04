package com.luo.domain.strategy.service.armory;

import com.luo.domain.strategy.model.entity.StrategyAwardEntity;
import com.luo.domain.strategy.model.entity.StrategyEntity;
import com.luo.domain.strategy.model.entity.StrategyRuleEntity;
import com.luo.domain.strategy.repository.IStrategyRepository;
import com.luo.type.constants.Commons;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;


@Service
public class StrategyArmoryDispatch implements IAssembleArmory, IRaffleDispatch {


    @Autowired
    private IStrategyRepository strategyRepository;


    @Override
    public Boolean assembleRaffleStrategy(Long strategyId) {

        // 1. 查询抽奖策略id对应奖品配置
        List<StrategyAwardEntity> list = strategyRepository.queryStrategyAwardList(strategyId);

        //2. 缓存奖品库存信息【用作decr扣减】
        for (StrategyAwardEntity strategyAwardEntity : list) {
            // 奖品id
            Integer awardId = strategyAwardEntity.getAwardId();
            // 奖品库存
            Integer awardCount = strategyAwardEntity.getAwardCount();
            // 缓存奖品库存
            setCacheAwardCount(awardId, awardCount, strategyId);
        }

        // 3.1 默认装配
        defaultAssemble(list, String.valueOf(strategyId));


        //4. 权重抽奖装配
        StrategyEntity strategyEntity = strategyRepository.queryStrategyEntityByStrategyId(strategyId);

        //获取处理好的rule_weight
        String ruleWeight = strategyEntity.getRuleWeight();
        if (ruleWeight == null) return true;
        StrategyRuleEntity strategyRuleEntity = strategyRepository.queryStrategyRuleEntity(ruleWeight);
        Map<String, List<Integer>> ruleValueMap = strategyRuleEntity.getRuleValue();

        for (String key : ruleValueMap.keySet()) {
            //过滤掉权重中不符合的奖品
            List<StrategyAwardEntity> strategyAwardEntityList = new ArrayList<>();
            List<Integer> awardIdList = ruleValueMap.get(key);

            for (Integer awardId : awardIdList) {
                for (StrategyAwardEntity entity : list) {
                    if (entity.getAwardId() == awardId) {
                        strategyAwardEntityList.add(entity);
                    }
                }
            }

            String caCheKey = String.valueOf(strategyId).concat(Commons.UNDERLINE) + key;
            //权重抽奖信息存储到redis中
            defaultAssemble(strategyAwardEntityList, caCheKey);
        }
        return true;
    }

    @Override
    public Boolean assembleRaffleStrategyByActivityId(Integer activityId) {
        Long strategyId = strategyRepository.queryStrategyIdByActivityId(activityId);
        return assembleRaffleStrategy(strategyId);
    }


    private void setCacheAwardCount(Integer awardId, Integer awardCount, Long strategyId) {

        //拼接key  xxxx_策略id_奖品id
        String caCheKey = Commons.RedisKey.STRATEGY_AWARD_COUNT_KEY + strategyId + Commons.UNDERLINE + awardId;

        //奖品库存信息存储到redis中
        strategyRepository.setCacheAwardCount(caCheKey, awardCount);
    }


    //3.1
    private void defaultAssemble(List<StrategyAwardEntity> list, String strategyId) {

        // 1. 获取最小概率值
        BigDecimal minAwardRate = list.stream()
                .map(StrategyAwardEntity::getAwardRate)
                .min(BigDecimal::compareTo)
                .orElse(BigDecimal.ZERO);

        //2. 最小概率倒数获取数量范围
        int rateRange = convert(minAwardRate.doubleValue());

        //3. 生成概率查找表
        List<Integer> awardIds = new ArrayList<>(rateRange);
        for (StrategyAwardEntity entity : list) {
            //获得单个奖品概率对应的数量
            BigDecimal temp = entity.getAwardRate().multiply(BigDecimal.valueOf(rateRange));

            //向下取整
            int count = temp.setScale(0, RoundingMode.FLOOR).intValue();

            for (int i = 0; i < count; i++) {
                awardIds.add(entity.getAwardId());
            }
        }

        //4. 打乱奖品id
        Collections.shuffle(awardIds);

        //5. 存入redis
        strategyRepository.setCacheAwardIds(strategyId, awardIds, awardIds.size());
    }

    // 假如最小概率为 0.0001 则返回 10000
    private int convert(double minAwardRate) {
        double current = minAwardRate;
        int power = 1;
        while (current < 1) {
            current = current * 10;
            power = power * 10;
        }
        return power;
    }


    @Override
    public Integer getRandomAwardId(Long strategyId) {
        int rateRange = strategyRepository.getRateRange(strategyId);
        return strategyRepository.getAwardRandom(new Random().nextInt(rateRange), strategyId);
    }

    @Override
    public Integer getRandomAwardId(Long strategyId, String ruleWeight) {
        String caCheKey = String.valueOf(strategyId).concat(Commons.UNDERLINE).concat(ruleWeight);
        return getRandomAwardId(caCheKey);
    }

    @Override
    public Integer getRandomAwardId(String caCheKey) {
        return strategyRepository.getAwardRandomByRuleWeight(caCheKey);
    }

    @Override
    public Boolean subtractStock(Long strategyId, Integer awardId) {
        String caCheKey = Commons.RedisKey.STRATEGY_AWARD_COUNT_KEY + strategyId + Commons.UNDERLINE + awardId;
        return strategyRepository.subtractStock(caCheKey);
    }
}
