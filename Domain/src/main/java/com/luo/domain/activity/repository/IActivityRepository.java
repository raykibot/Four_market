package com.luo.domain.activity.repository;

import com.luo.domain.activity.model.aggregate.CreatePartakeOrderAggregate;
import com.luo.domain.activity.model.aggregate.CreateQuotaOrderAggregate;
import com.luo.domain.activity.model.entity.*;
import com.luo.domain.activity.model.vo.ActivitySkuStockVO;

import java.util.Date;
import java.util.List;

public interface IActivityRepository {


    ActivitySkuEntity queryActivitySku(Integer sku);

    ActivityEntity queryRaffleActivityByActivityId(Integer activityId);

    ActivityCountEntity queryActivityCountByActivityCountId(Integer activityCountId);

    void doSaveOrder(CreateQuotaOrderAggregate createQuotaOrderAggregate);

    void cacheActivityStockSkuCount(String cacheKey, Integer stockCount);

    boolean subtractionActivityStockSkuCount(Integer sku, String cacheKey, Date endTime);

    void activitySkuStockConsumeSendQueue(ActivitySkuStockVO build);

    ActivitySkuStockVO takeQueueValue();

    void clearQueue();

    void updateActivitySkuStock(Integer sku);

    void clearActivitySkuStock(Integer sku);

    UserRaffleOrderEntity queryUnUsedUserRaffleOrder(PartakeRaffleActivityEntity partakeRaffleActivity);

    void saveCreatePartakeOrderAggregate(CreatePartakeOrderAggregate createPartakeOrderAggregate);

    ActivityAccountEntity queryActivityAccountByUserId(String userId, Integer activityId);

    ActivityAccountMonthEntity queryActivityAccountMonthByUserId(String userId, Integer activityId, String month);

    ActivityAccountDayEntity queryActivityAccountDayByUserId(String userId, Integer activityId, String day);

    List<ActivitySkuEntity> queryActivitySkuByActivityId(Integer activityId);
}
