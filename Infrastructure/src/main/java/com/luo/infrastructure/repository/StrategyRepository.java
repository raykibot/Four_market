package com.luo.infrastructure.repository;


import com.luo.domain.strategy.model.entity.StrategyAwardEntity;
import com.luo.domain.strategy.model.entity.StrategyAwardRuleModelVO;
import com.luo.domain.strategy.model.entity.StrategyEntity;
import com.luo.domain.strategy.model.entity.StrategyRuleEntity;
import com.luo.domain.strategy.model.vo.*;
import com.luo.domain.strategy.repository.IStrategyRepository;
import com.luo.infrastructure.dao.*;
import com.luo.infrastructure.pojo.*;
import com.luo.infrastructure.redis.IRedisService;
import com.luo.type.constants.Commons;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBlockingQueue;
import org.redisson.api.RDelayedQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.TimeUnit;

@Slf4j
@Repository
public class StrategyRepository implements IStrategyRepository {


    @Autowired
    private IRedisService redisService;

    @Autowired
    private IStrategyAwardDAO strategyAwardDAO;

    @Autowired
    private IStrategyDAO strategyDAO;

    @Autowired
    private IStrategyRuleDAO strategyRuleDAO;

    @Autowired
    private IRuleTreeDAO ruleTreeDAO;

    @Autowired
    IRuleTreeNodeDAO ruleTreeNodeDAO;

    @Autowired
    private IRuleTreeNodeLineDAO ruleTreeNodeLineDAO;


    @Override
    public void setCacheAwardIds(String strategyId, List<Integer> awardIds, int size) {
        //抽奖范围存储
        String rangeKey = Commons.RedisKey.STRATEGY_AWARD_RATE_RANGE_KEY + strategyId;
        redisService.setValue(rangeKey, size);

        //奖品查找表存储
        String rateTableKey = Commons.RedisKey.STRATEGY_AWARD_RATE_TABLE_KEY + strategyId;
        redisService.setValue(rateTableKey, awardIds);
    }

    @Override
    public void setCacheAwardCount(String caCheKey, Integer awardCount) {
        //存储奖品id库存信息 原子性操作
        redisService.setActomicLongValue(caCheKey, awardCount);
    }

    @Override
    public List<StrategyAwardEntity> queryStrategyAwardList(Long strategyId) {

        //优先从redis中获取
        String cacheKey = Commons.RedisKey.STRATEGY_AWARD_LIST_KEY + strategyId;
        List<StrategyAwardEntity> value = redisService.getValue(cacheKey);
        if (value != null ){
            return value;
        }

        //查询奖品配置
        List<StrategyAward> strategyAwardList = strategyAwardDAO.queryStrategyAwardList(strategyId);
        //类型转换
        List<StrategyAwardEntity> strategyAwardEntityList = new ArrayList<>();
        for (StrategyAward strategyAward : strategyAwardList) {
            StrategyAwardEntity build = StrategyAwardEntity.builder()
                    .awardId(strategyAward.getAwardId())
                    .awardTitle(strategyAward.getAwardTitle())
                    .awardSubtitle(strategyAward.getAwardSubtitle())
                    .awardCount(strategyAward.getAwardCount())
                    .awardCountSurplus(strategyAward.getAwardCountSurplus())
                    .awardRate(strategyAward.getAwardRate())
                    .sort(strategyAward.getSort())
                    .build();
            strategyAwardEntityList.add(build);
        }

        //缓存到redis中
        redisService.setValue(Commons.RedisKey.STRATEGY_AWARD_LIST_KEY, strategyAwardEntityList);

        return strategyAwardEntityList;
    }

    @Override
    public StrategyEntity queryStrategyEntityByStrategyId(Long strategyId) {
        Strategy strategy = strategyDAO.queryStrategyEntityByStrategyId(strategyId);
        return StrategyEntity.builder()
                .strategyId(strategy.getStrategyId())
                .strategyDesc(strategy.getStrategyDesc())
                .ruleModels(strategy.getRuleModels())
                .build();
    }

    @Override
    public StrategyRuleEntity queryStrategyRuleEntity(String ruleWeight) {
        StrategyRule strategyRule = strategyRuleDAO.queryStrategyRuleEntity(ruleWeight);

        return StrategyRuleEntity.builder()
                .strategyId(strategyRule.getStrategyId())
                .ruleModel(strategyRule.getRuleModel())
                .awardId(strategyRule.getAwardId())
                .ruleDesc(strategyRule.getRuleDesc())
                .ruleValue(strategyRule.getRuleValue())
                .build();
    }

    @Override
    public int getRateRange(Long strategyId) {
        String caCheKey = Commons.RedisKey.STRATEGY_AWARD_RATE_RANGE_KEY + strategyId;
        return redisService.getValue(caCheKey);
    }

    @Override
    public Integer getAwardRandom(int index, Long strategyId) {
        String value = String.valueOf(strategyId);
        String caCheKey = Commons.RedisKey.STRATEGY_AWARD_RATE_TABLE_KEY + value;
        List<Integer> awardList = redisService.getValue(caCheKey);
        return awardList.get(index);
    }

    @Override
    public Integer getAwardRandomByRuleWeight(String caCheKey) {
        String rateRange_RuleWeight = Commons.RedisKey.STRATEGY_AWARD_RATE_RANGE_KEY + caCheKey;
        Integer rateRange = redisService.getValue(rateRange_RuleWeight);
        log.info("rateRange：{}", rateRange);

        String key = Commons.RedisKey.STRATEGY_AWARD_RATE_TABLE_KEY + caCheKey;
        List<Integer> awardList = redisService.getValue(key);
        return awardList.get(new Random().nextInt(rateRange));
    }

    @Override
    public String queryRuleValue(Long strategyId, String ruleModel) {
        return strategyRuleDAO.queryRuleValue(strategyId, ruleModel);
    }

    @Override
    public Boolean subtractStock(String caCheKey) {
        long surplus = redisService.decr(caCheKey);
        if (surplus < 0) {
            redisService.setValue(caCheKey, 0);
        }

        //给奖品库存上锁
        String lockKey = caCheKey + Commons.UNDERLINE + surplus;
        Boolean lockStatus = redisService.setNx(lockKey);
        if (!lockStatus) {
            log.info("上锁失败");
        }
        return lockStatus;
    }

    @Override
    public StrategyAwardEntity queryStrategyAwardEntity(Integer awardId, Long strategyId) {

        //优先从缓存中获取
        String caCheKey = Commons.RedisKey.STRATEGY_AWARD_KEY + strategyId + Commons.UNDERLINE + awardId;
        StrategyAwardEntity strategyAwardEntity = redisService.getValue(caCheKey);
        if (strategyAwardEntity != null) {
            return strategyAwardEntity;
        }

        StrategyAward strategyAwardReq = new StrategyAward();
        strategyAwardReq.setAwardId(awardId);
        strategyAwardReq.setStrategyId(strategyId);

        StrategyAward strategyAward = strategyAwardDAO.queryStrategyAward(strategyAwardReq);

        strategyAwardEntity = new StrategyAwardEntity();

        strategyAwardEntity.setStrategyId(strategyAward.getStrategyId());
        strategyAwardEntity.setAwardId(strategyAward.getAwardId());
        strategyAwardEntity.setAwardTitle(strategyAward.getAwardTitle());
        strategyAwardEntity.setAwardSubtitle(strategyAward.getAwardSubtitle());
        strategyAwardEntity.setAwardCount(strategyAward.getAwardCount());
        strategyAwardEntity.setAwardCountSurplus(strategyAward.getAwardCountSurplus());
        strategyAwardEntity.setSort(strategyAward.getSort());
        strategyAwardEntity.setAwardRate(strategyAward.getAwardRate());


        //缓存结果
        redisService.setValue(caCheKey, strategyAwardEntity);

        return strategyAwardEntity;
    }

    @Override
    public RuleTreeVO queryRuleTreeVOByTreeId(String treeId) {
        //优先从缓存中获取
        String caCheKey = Commons.RedisKey.RULE_TREE_VO_KEY + treeId;
        RuleTreeVO value = redisService.getValue(caCheKey);
        if (null != value) {
            return value;
        }


        //从数据库获取
        RuleTree ruleTree = ruleTreeDAO.queryRuleTreeVOByTreeId(treeId);
        List<RuleTreeNode> ruleTreeNodes = ruleTreeNodeDAO.queryRuleTreeNodeVOByTreeId(treeId);
        List<RuleTreeNodeLine> ruleTreeNodeLines = ruleTreeNodeLineDAO.queryRuleTreeNodeLineVOByTreeId(treeId);


        //1. 构建节点线map 节点名称对应着自己的连线集合
        Map<String, List<RuleTreeNodeLineVO>> ruleTreeNodeLineMap = new HashMap<>();
        for (RuleTreeNodeLine ruleTreeNodeLine : ruleTreeNodeLines) {
            RuleTreeNodeLineVO lineVO = RuleTreeNodeLineVO.builder()
                    .treeId(ruleTreeNodeLine.getTreeId())
                    .ruleNodeFrom(ruleTreeNodeLine.getRuleNodeFrom())
                    .ruleNodeTo(ruleTreeNodeLine.getRuleNodeTo())
                    .ruleLimitType(RuleLimitTypeVO.valueOf(ruleTreeNodeLine.getRuleLimitType()))
                    .ruleLogicCheckTypeVO(RuleLogicCheckTypeVO.valueOf(ruleTreeNodeLine.getRuleLimitValue()))
                    .build();

            List<RuleTreeNodeLineVO> lineVoList = ruleTreeNodeLineMap.get(lineVO.getRuleNodeFrom());
            if (lineVoList == null) {
                lineVoList = new ArrayList<>();
                ruleTreeNodeLineMap.put(lineVO.getRuleNodeFrom(), lineVoList);
            }
            lineVoList.add(lineVO);
        }

        //2. 构建节点名称对应节点VOmap
        Map<String, RuleTreeNodeVO> ruleTreeNodeMap = new HashMap<>();
        for (RuleTreeNode ruleTreeNode : ruleTreeNodes) {
            RuleTreeNodeVO treeNodeVO = RuleTreeNodeVO.builder()
                    .ruleKey(ruleTreeNode.getRuleKey())
                    .ruleDesc(ruleTreeNode.getRuleDesc())
                    .ruleValue(ruleTreeNode.getRuleValue())
                    .ruleTreeNodeLineVOList(ruleTreeNodeLineMap.get(ruleTreeNode.getRuleKey()))
                    .build();

            ruleTreeNodeMap.put(treeNodeVO.getRuleKey(), treeNodeVO);
        }


        //3. 完成ruleTreeVO 规则树总体构建
        RuleTreeVO ruleTreeVO = new RuleTreeVO();
        ruleTreeVO.setTreeId(treeId);
        ruleTreeVO.setTreeName(ruleTree.getTreeName());
        ruleTreeVO.setTreeDesc(ruleTree.getTreeDesc());
        ruleTreeVO.setTreeRootRuleNode(ruleTree.getTreeRootRuleKey());
        ruleTreeVO.setTreeNodeMap(ruleTreeNodeMap);

        return ruleTreeVO;
    }

    @Override
    public StrategyAwardRuleModelVO queryStrategyAwardRuleModel(Long strategyId, Integer awardId) {
        StrategyAward strategyAward = new StrategyAward();
        strategyAward.setAwardId(awardId);
        strategyAward.setStrategyId(strategyId);
        String ruleModels = strategyAwardDAO.queryRuleModel(strategyAward);
        if (ruleModels == null) {
            return null;
        }
        return StrategyAwardRuleModelVO.builder()
                .ruleModel(ruleModels)
                .build();
    }

    @Override
    public void awardStockConsumeSendQueue(StrategyAwardStockVO build) {

        String caCheKey = Commons.RedisKey.STRATEGY_AWARD_QUERY_KEY;
        //根据caCheKey获取一个加锁队列
        RBlockingQueue<StrategyAwardStockVO> blockingDeque = redisService.getBlockingDeque(caCheKey);
        //根据加锁队列获取延迟队列
        RDelayedQueue<StrategyAwardStockVO> delayedDeque = redisService.getDelayedQueue(blockingDeque);
        //推送到延迟队列中
        delayedDeque.offer(build, 3, TimeUnit.SECONDS);
    }

    @Override
    public StrategyAwardStockVO takeQueueValue() {
        String caCheKey = Commons.RedisKey.STRATEGY_AWARD_QUERY_KEY;
        RBlockingQueue<StrategyAwardStockVO> destinationQueue = redisService.getBlockingDeque(caCheKey);
        return destinationQueue.poll();
    }

    @Override
    public void updateStrategyAwardStock(Long strategyId, Integer awardId) {
        StrategyAward strategyAward = new StrategyAward();
        strategyAward.setAwardId(awardId);
        strategyAward.setStrategyId(strategyId);
        strategyAwardDAO.updateStrategyAwardStock(strategyAward);
    }

    @Override
    public Long queryStrategyIdByActivityId(Integer activityId) {
        return strategyDAO.queryStrategyIdByActivityId(activityId);
    }
}
