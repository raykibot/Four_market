package com.luo.domain.activity.service;

import com.luo.domain.activity.model.entity.ActivityCountEntity;
import com.luo.domain.activity.model.entity.ActivityEntity;
import com.luo.domain.activity.model.entity.ActivitySkuEntity;
import com.luo.domain.activity.repository.IActivityRepository;
import com.luo.domain.activity.service.rule.factory.DefaultActionChainFactory;

public class RaffleActivitySupport {

    protected DefaultActionChainFactory defaultActionChainFactory;

    protected IActivityRepository activityRepository;

    public RaffleActivitySupport(DefaultActionChainFactory defaultActionChainFactory, IActivityRepository activityRepository) {
        this.defaultActionChainFactory = defaultActionChainFactory;
        this.activityRepository = activityRepository;
    }

    public  ActivitySkuEntity queryActivitySku(Integer sku) {
        return activityRepository.queryActivitySku(sku);
    }

    public ActivityEntity queryRaffleActivityByActivityId(Integer activityId) {
        return activityRepository.queryRaffleActivityByActivityId(activityId);
    }

    public ActivityCountEntity queryActivityCountByActivityCountId(Integer activityCountId) {
        return activityRepository.queryActivityCountByActivityCountId(activityCountId);
    }
}
