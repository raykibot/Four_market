package com.luo.domain.activity.repository;

import com.luo.domain.activity.model.entity.ActivityCountEntity;
import com.luo.domain.activity.model.entity.ActivityEntity;
import com.luo.domain.activity.model.entity.ActivitySkuEntity;

public interface IActivityRepository {


    ActivitySkuEntity queryActivitySku(Integer sku);

    ActivityEntity queryRaffleActivityByActivityId(Integer activityId);

    ActivityCountEntity queryActivityCountByActivityCountId(Integer activityCountId);
}
