package com.luo.domain.strategy.service.armory;

import java.util.Date;

public interface IRaffleDispatch {


    /**
     * 随机获取奖品id      全量抽奖使用
     *
     * @param strategyId 策略id
     * @return 奖品id
     */
    Integer getRandomAwardId(Long strategyId);


    /**
     * 随机获取奖品id    携带权重使用
     *
     * @param strategyId 策略id
     * @param ruleWeight 权重
     * @return 奖品id
     */
    Integer getRandomAwardId(Long strategyId, String ruleWeight);


    /**
     * 获取随机奖品  权重过滤节点使用
     *
     * @param caCheKey caCheKey = strategyId + "_" + ruleWeight
     * @return 奖品id
     */
    Integer getRandomAwardId(String caCheKey);


    /**
     * 扣减库存
     * @param strategyId
     * @param awardId
     * @return boolean
     */
    Boolean subtractStock(Long strategyId, Integer awardId, Date endDateTime);

}
