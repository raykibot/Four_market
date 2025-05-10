package com.luo.domain.activity.service;

import com.luo.domain.activity.model.entity.SkuRechargeEntity;

/**
 * 抽奖订单接口
 */
public interface IRaffleActivityAccountQuotaService {


    /**
     * 下单充值sku创建订单 增加抽奖次数
     * @param skuRechargeEntity 充值sku 数据库标记唯一充值索引
     * @return  用户id
     */
    String createOrder(SkuRechargeEntity skuRechargeEntity);


    /**
     * 查询用户抽奖参与次数 -日参与次数
     * @param activityId 活动id
     * @param userId 用户id
     * @return 用户参与次数
     */
    Integer queryRaffleActivityAccountDayPartakeCount(Integer activityId, String userId);

}
