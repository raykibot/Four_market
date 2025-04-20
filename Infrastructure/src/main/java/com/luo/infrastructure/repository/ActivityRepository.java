package com.luo.infrastructure.repository;

import com.luo.domain.activity.model.entity.ActivityCountEntity;
import com.luo.domain.activity.model.entity.ActivityEntity;
import com.luo.domain.activity.model.entity.ActivitySkuEntity;
import com.luo.domain.activity.model.vo.ActivityStateVO;
import com.luo.domain.activity.repository.IActivityRepository;
import com.luo.infrastructure.dao.IRaffleActivityCountDAO;
import com.luo.infrastructure.dao.IRaffleActivityDAO;
import com.luo.infrastructure.dao.IRaffleActivitySkuDAO;
import com.luo.infrastructure.pojo.RaffleActivity;
import com.luo.infrastructure.pojo.RaffleActivityCount;
import com.luo.infrastructure.pojo.RaffleActivitySku;
import com.luo.infrastructure.redis.IRedisService;
import com.luo.type.constants.Commons;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

@Repository
public class ActivityRepository implements IActivityRepository {


    @Resource
    private IRaffleActivitySkuDAO raffleActivitySkuDAO;

    @Resource
    private IRaffleActivityCountDAO raffleActivityCountDAO;

    @Resource
    private IRaffleActivityDAO raffleActivityDAO;

    @Resource
    private IRedisService redisService;


    @Override
    public ActivitySkuEntity queryActivitySku(Integer sku) {

        RaffleActivitySku raftActivitySku = raffleActivitySkuDAO.queryRaffleActivitySkuBySku(sku);

        return ActivitySkuEntity.builder()
                .sku(raftActivitySku.getSku())
                .activityId(raftActivitySku.getActivityId())
                .activityCountId(raftActivitySku.getActivityCountId())
                .stockCount(raftActivitySku.getStockCount())
                .stockCountSurplus(raftActivitySku.getStockCountSurplus())
                .build();
    }

    @Override
    public ActivityEntity queryRaffleActivityByActivityId(Integer activityId) {

        String cacheKey = Commons.RedisKey.ACTIVITY_KEY + activityId;
        ActivityEntity activityEntity = redisService.getValue(cacheKey);
        if (activityEntity != null) {
            return activityEntity;
        }

        RaffleActivity raffleActivity = raffleActivityDAO.queryRaffleActivityByActivityId(activityId);
        activityEntity = ActivityEntity.builder()
                .activityId(raffleActivity.getActivityId())
                .activityName(raffleActivity.getActivityName())
                .activityDesc(raffleActivity.getActivityDesc())
                .beginDateTime(raffleActivity.getBeginDateTime())
                .endDateTime(raffleActivity.getEndDateTime())
                .strategyId(raffleActivity.getStrategyId())
                .state(ActivityStateVO.valueOf(raffleActivity.getState()))
                .build();

        redisService.setValue(cacheKey,activityEntity);

        return activityEntity;
    }

    @Override
    public ActivityCountEntity queryActivityCountByActivityCountId(Integer activityCountId) {

        String cacheKey = Commons.RedisKey.ACTIVITY_COUNT_KEY + activityCountId;
        ActivityCountEntity activityCountEntity = redisService.getValue(cacheKey);
        if (activityCountEntity != null) return activityCountEntity;

        RaffleActivityCount raffleActivityCount = raffleActivityCountDAO.queryRaffleActivityCountByActivityCountId(activityCountId);
        activityCountEntity = ActivityCountEntity.builder()
                .activityCountId(raffleActivityCount.getActivityCountId())
                .totalCount(raffleActivityCount.getTotalCount())
                .dayCount(raffleActivityCount.getDayCount())
                .monthCount(raffleActivityCount.getMonthCount())
                .build();

        redisService.setValue(cacheKey,activityCountEntity);

        return activityCountEntity;
    }
}
