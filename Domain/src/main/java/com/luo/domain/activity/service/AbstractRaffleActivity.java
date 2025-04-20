package com.luo.domain.activity.service;

import com.alibaba.fastjson.JSON;
import com.luo.domain.activity.model.entity.*;
import com.luo.domain.activity.repository.IActivityRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractRaffleActivity implements IRaffleOrder{

    protected IActivityRepository activityRepository;

    public AbstractRaffleActivity(IActivityRepository activityRepository){
        this.activityRepository = activityRepository;
    }

    @Override
    public ActivityOrderEntity createRaffleActivityOrder(ActivityShopCarEntity activityShopCarEntity) {
        ActivitySkuEntity activitySkuEntity = activityRepository.queryActivitySku(activityShopCarEntity.getSku());
        ActivityEntity activityEntity = activityRepository.queryRaffleActivityByActivityId(activitySkuEntity.getActivityId());
        ActivityCountEntity activityCountEntity = activityRepository.queryActivityCountByActivityCountId(activitySkuEntity.getActivityCountId());

        log.info("活动信息:{} {} {}", JSON.toJSONString(activityCountEntity),JSON.toJSONString(activityEntity),JSON.toJSONString(activityCountEntity));

        return ActivityOrderEntity.builder().build();
    }
}
