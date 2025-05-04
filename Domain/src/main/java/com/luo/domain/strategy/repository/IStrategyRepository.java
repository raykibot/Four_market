package com.luo.domain.strategy.repository;

import com.luo.domain.strategy.model.entity.StrategyAwardEntity;
import com.luo.domain.strategy.model.entity.StrategyAwardRuleModelVO;
import com.luo.domain.strategy.model.entity.StrategyEntity;
import com.luo.domain.strategy.model.entity.StrategyRuleEntity;
import com.luo.domain.strategy.model.vo.RuleTreeVO;
import com.luo.domain.strategy.model.vo.StrategyAwardStockVO;

import java.util.List;

public interface IStrategyRepository {
    void setCacheAwardIds(String strategyId, List<Integer> awardIds, int size);

    /**
     * 缓存奖品库存
     * @param caCheKey
     * @param awardCount
     */
    void setCacheAwardCount(String caCheKey, Integer awardCount);

    List<StrategyAwardEntity> queryStrategyAwardList(Long strategyId);

    StrategyEntity queryStrategyEntityByStrategyId(Long strategyId);

    StrategyRuleEntity queryStrategyRuleEntity(String ruleWeight);

    int getRateRange(Long strategyId);

    Integer getAwardRandom(int index,Long strategyId);

    Integer getAwardRandomByRuleWeight(String caCheKey);

    String queryRuleValue(Long strategyId, String ruleModel);

    Boolean subtractStock(String caCheKey);

    StrategyAwardEntity queryStrategyAwardEntity(Integer awardId, Long strategyId);

    /**
     * 根据规则树ID查询规则树结构信息
     * @param treeId
     * @return ruleTreeVO
     */
    RuleTreeVO queryRuleTreeVOByTreeId(String treeId);

    StrategyAwardRuleModelVO queryStrategyAwardRuleModel(Long strategyId, Integer awardId);

    void awardStockConsumeSendQueue(StrategyAwardStockVO build);

    StrategyAwardStockVO takeQueueValue();

    void updateStrategyAwardStock(Long strategyId, Integer awardId);

    Long queryStrategyIdByActivityId(Integer activityId);
}
