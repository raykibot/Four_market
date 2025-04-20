package com.luo.domain.activity.service;

import com.luo.domain.activity.model.entity.ActivityOrderEntity;
import com.luo.domain.activity.model.entity.ActivityShopCarEntity;

/**
 * 抽奖订单接口
 */
public interface IRaffleOrder {



    ActivityOrderEntity createRaffleActivityOrder(ActivityShopCarEntity activityShopCarEntity);


}
