package com.luo.domain.strategy.service.armory;

public interface IAssembleArmory {


    /**
     * 装配抽奖策略
     * @param strategyId
     * @return boolean
     */
     Boolean assembleRaffleStrategy(Long strategyId);


    Boolean assembleRaffleStrategyByActivityId(Integer activityId);
}
