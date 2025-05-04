package com.luo.domain.activity.service;


import com.luo.domain.activity.model.entity.PartakeRaffleActivityEntity;
import com.luo.domain.activity.model.entity.UserRaffleOrderEntity;

/**
 * 抽奖活动参与服务
 */
public interface IRaffleActivityPartakeService {


    /**
     * 创建抽奖单；用户参与抽奖活动，扣减活动账户库存，产生抽奖单。如存在未被使用的抽奖单则直接返回已存在的抽奖单。
     * @return
     */
    UserRaffleOrderEntity createOrder(PartakeRaffleActivityEntity partakeRaffleActivity);

    UserRaffleOrderEntity createOrder(String userId, Integer activityId);
}
