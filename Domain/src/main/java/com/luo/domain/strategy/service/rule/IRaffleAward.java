package com.luo.domain.strategy.service.rule;

import com.luo.domain.strategy.model.entity.StrategyAwardEntity;

import java.util.List;
import java.util.Map;

public interface IRaffleAward {


    /**
     * 根据策略id查询奖品列表
     * @param strategyId 策略id
     * @return 奖品列表
     */
    List<StrategyAwardEntity> queryRaffleStaregyAwardList(Long strategyId);

    /**
     * 根据活动id查询奖品列表
     * @param activityId 活动id
     * @return 策略奖品集合
     */
    List<StrategyAwardEntity> queryRaffleStrategyAwardListByActivityId(Integer activityId);

    Map<String, Integer> queryAwardRuleLockCount(String[] treeIds);
}
