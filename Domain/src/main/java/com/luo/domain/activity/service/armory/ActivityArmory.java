package com.luo.domain.activity.service.armory;


import com.luo.domain.activity.model.entity.ActivitySkuEntity;
import com.luo.domain.activity.repository.IActivityRepository;
import com.luo.type.constants.Commons;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;


@Service
public class ActivityArmory implements IActivityDispatch, IActivityArmory{


    @Autowired
    private IActivityRepository activityRepository;

    @Override
    public boolean assembleActivitySku(Integer sku) {

        ActivitySkuEntity activitySkuEntity = activityRepository.queryActivitySku(sku);
        cacheActivityStockSkuCount(sku, activitySkuEntity.getStockCount());


        //查询时会将数据库中的数据缓存到redis中
        activityRepository.queryRaffleActivityByActivityId(activitySkuEntity.getActivityId());

        //同上
        activityRepository.queryActivityCountByActivityCountId(activitySkuEntity.getActivityCountId());

        return true;
    }

    @Override
    public boolean assembleActivitySkuByActivityId(Integer activityId) {

        List<ActivitySkuEntity> activitySkuEntityList = activityRepository.queryActivitySkuByActivityId(activityId);
        for (ActivitySkuEntity activitySkuEntity : activitySkuEntityList) {
            cacheActivityStockSkuCount(activitySkuEntity.getSku(), activitySkuEntity.getStockCountSurplus());

            //查询时预热数据到缓存
            activityRepository.queryActivityCountByActivityCountId(activitySkuEntity.getActivityCountId());
        }
        //查询时预热数据到缓存
        activityRepository.queryRaffleActivityByActivityId(activityId);

        return true;
    }

    @Override
    public boolean subtractionActivityStockSku(Integer sku, Date endTime) {
        String cacheKey = Commons.RedisKey.ACTIVITY_SKU_STOCK_COUNT_KEY + sku;
        return activityRepository.subtractionActivityStockSkuCount(sku, cacheKey, endTime);
    }

    private void cacheActivityStockSkuCount(Integer sku, Integer stockCount){
        String cacheKey = Commons.RedisKey.ACTIVITY_SKU_STOCK_COUNT_KEY + sku;
        activityRepository.cacheActivityStockSkuCount(cacheKey, stockCount);
    }

}
