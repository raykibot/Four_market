package com.luo.domain.activity.service;


import com.luo.domain.activity.model.entity.PartakeRaffleActivityEntity;
import com.luo.domain.activity.model.entity.UserRaffleOrderEntity;

/**
 * 抽奖活动参与服务
 */
public interface IRaffleActivityPartakeService {


    /**
     * 消耗抽奖次数
     * 创建抽奖单；用户参与抽奖活动，扣减活动账户库存，产生抽奖单。如存在未被使用的抽奖单则直接返回已存在的抽奖单。
     * @return 用户抽奖订单  含有订单使用状态 对应策略id 订单id 用户id等
     */
    UserRaffleOrderEntity createOrder(PartakeRaffleActivityEntity partakeRaffleActivity);

    UserRaffleOrderEntity createOrder(String userId, Integer activityId);
}
