package com.luo.domain.activity.repository;

import com.luo.domain.activity.model.aggregate.CreateAggregateOrder;
import com.luo.domain.activity.model.entity.ActivityCountEntity;
import com.luo.domain.activity.model.entity.ActivityEntity;
import com.luo.domain.activity.model.entity.ActivitySkuEntity;
import com.luo.domain.activity.model.vo.ActivitySkuStockVO;

import java.util.Date;

public interface IActivityRepository {


    ActivitySkuEntity queryActivitySku(Integer sku);

    ActivityEntity queryRaffleActivityByActivityId(Integer activityId);

    ActivityCountEntity queryActivityCountByActivityCountId(Integer activityCountId);

    void doSaveOrder(CreateAggregateOrder createAggregateOrder);

    void cacheActivityStockSkuCount(String cacheKey, Integer stockCount);

    boolean subtractionActivityStockSkuCount(Integer sku, String cacheKey, Date endTime);

    void activitySkuStockConsumeSendQueue(ActivitySkuStockVO build);

    ActivitySkuStockVO takeQueueValue();

    void clearQueue();

    void updateActivitySkuStock(Integer sku);

    void clearActivitySkuStock(Integer sku);
}
