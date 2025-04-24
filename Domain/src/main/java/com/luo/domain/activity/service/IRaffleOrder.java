package com.luo.domain.activity.service;

import com.luo.domain.activity.model.entity.ActivityOrderEntity;
import com.luo.domain.activity.model.entity.ActivityShopCarEntity;
import com.luo.domain.activity.model.entity.SkuRechargeEntity;

/**
 * 抽奖订单接口
 */
public interface IRaffleOrder {



    String createSkuRechargeOrder(SkuRechargeEntity skuRechargeEntity);


}
