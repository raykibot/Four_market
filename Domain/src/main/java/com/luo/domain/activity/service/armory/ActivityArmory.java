package com.luo.domain.activity.service.armory;


import com.luo.domain.activity.model.entity.ActivityEntity;
import com.luo.domain.activity.model.entity.ActivitySkuEntity;
import com.luo.domain.activity.repository.IActivityRepository;
import com.luo.type.constants.Commons;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

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
    public boolean subtractionActivityStockSku(Integer sku, Date endTime) {
        String cacheKey = Commons.RedisKey.ACTIVITY_SKU_STOCK_COUNT_KEY + sku;
        return activityRepository.subtractionActivityStockSkuCount(sku, cacheKey, endTime);
    }

    private void cacheActivityStockSkuCount(Integer sku, Integer stockCount){
        String cacheKey = Commons.RedisKey.ACTIVITY_SKU_STOCK_COUNT_KEY + sku;
        activityRepository.cacheActivityStockSkuCount(cacheKey, stockCount);
    }

}
