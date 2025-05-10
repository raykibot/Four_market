package com.luo.domain.activity.service.quota;

import com.alibaba.fastjson.JSON;
import com.luo.domain.activity.model.aggregate.CreateQuotaOrderAggregate;
import com.luo.domain.activity.model.entity.*;
import com.luo.domain.activity.repository.IActivityRepository;
import com.luo.domain.activity.service.IRaffleActivityAccountQuotaService;
import com.luo.domain.activity.service.quota.rule.IActionChain;
import com.luo.domain.activity.service.quota.rule.factory.DefaultActionChainFactory;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractRaffleActivityAccountQuota extends RaffleActivityAccountQuotaSupport implements IRaffleActivityAccountQuotaService {


    public AbstractRaffleActivityAccountQuota(DefaultActionChainFactory defaultActionChainFactory, IActivityRepository activityRepository) {
        super(defaultActionChainFactory, activityRepository);
    }

    @Override
    public String createOrder(SkuRechargeEntity skuRechargeEntity) {

        //1.参数校验
        String userId = skuRechargeEntity.getUserId();
        Integer sku = skuRechargeEntity.getSku();
        String outBusinessNo = skuRechargeEntity.getOutBusinessNo();
        if (userId == null || sku == null || outBusinessNo == null){
            throw new RuntimeException("参数null");
        }

        //2.查询活动基本信息

        //2.1 查询活动sku信息
        ActivitySkuEntity activitySkuEntity = queryActivitySku(sku);
        //2.2 查询活动信息
        ActivityEntity activityEntity = queryRaffleActivityByActivityId(activitySkuEntity.getActivityId());
        //2.3 查询活动次数信息 (用户在活动上可参与的次数)
        ActivityCountEntity activityCountEntity = queryActivityCountByActivityCountId(activitySkuEntity.getActivityCountId());


        // 活动规则动作校验
        IActionChain actionChain = defaultActionChainFactory.openChain();
        Boolean success = actionChain.actionChain(activitySkuEntity, activityEntity, activityCountEntity);


        //4. 构建订单聚合对象
        CreateQuotaOrderAggregate createQuotaOrderAggregate = buildAggregateOrder(skuRechargeEntity, activitySkuEntity, activityEntity, activityCountEntity);


        //5. 保存订单
        doSaveOrder(createQuotaOrderAggregate);

        log.info("活动信息:{} {} {}", JSON.toJSONString(activitySkuEntity),JSON.toJSONString(activityEntity),JSON.toJSONString(activityCountEntity));

        return createQuotaOrderAggregate.getActivityOrderEntity().getOrderId();
    }


    protected abstract CreateQuotaOrderAggregate buildAggregateOrder(SkuRechargeEntity skuRechargeEntity, ActivitySkuEntity activitySkuEntity, ActivityEntity activityEntity, ActivityCountEntity activityCountEntity);

    protected abstract void doSaveOrder(CreateQuotaOrderAggregate createQuotaOrderAggregate);
}
