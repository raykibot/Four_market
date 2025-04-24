package com.luo.infrastructure.repository;

import cn.bugstack.middleware.db.router.strategy.IDBRouterStrategy;
import com.luo.domain.activity.event.ActivityStockZeroMessageEvent;
import com.luo.domain.activity.model.aggregate.CreateAggregateOrder;
import com.luo.domain.activity.model.entity.ActivityCountEntity;
import com.luo.domain.activity.model.entity.ActivityEntity;
import com.luo.domain.activity.model.entity.ActivityOrderEntity;
import com.luo.domain.activity.model.entity.ActivitySkuEntity;
import com.luo.domain.activity.model.vo.ActivitySkuStockVO;
import com.luo.domain.activity.model.vo.ActivityStateVO;
import com.luo.domain.activity.repository.IActivityRepository;
import com.luo.infrastructure.dao.*;
import com.luo.infrastructure.envent.EventPublisher;
import com.luo.infrastructure.pojo.*;
import com.luo.infrastructure.redis.IRedisService;
import com.luo.type.constants.Commons;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBlockingQueue;
import org.redisson.api.RDelayedQueue;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Resource;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Repository
@Slf4j
public class ActivityRepository implements IActivityRepository {


    @Resource
    private IRaffleActivitySkuDAO raffleActivitySkuDAO;

    @Resource
    private IRaffleActivityCountDAO raffleActivityCountDAO;

    @Resource
    private IRaffleActivityDAO raffleActivityDAO;

    @Resource
    private IRaffleActivityOrderDAO raffleActivityOrderDAO;

    @Resource
    private IRaffleActivityAccountDAO raffleActivityAccountDAO;

    @Resource
    private IRedisService redisService;

    @Resource
    private TransactionTemplate transactionTemplate;

    @Resource
    private IDBRouterStrategy dbRouter;

    @Resource
    private EventPublisher eventPublisher;

    @Resource
    private ActivityStockZeroMessageEvent ackZeroMessageEvent;


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

    @Override
    public void doSaveOrder(CreateAggregateOrder createAggregateOrder) {

        try{
            ActivityOrderEntity activityOrderEntity = createAggregateOrder.getActivityOrderEntity();
            RaffleActivityOrder raffleActivityOrder = new RaffleActivityOrder();
            raffleActivityOrder.setUserId(createAggregateOrder.getUserId());
            raffleActivityOrder.setSku(activityOrderEntity.getSku());
            raffleActivityOrder.setActivityId(activityOrderEntity.getActivityId());
            raffleActivityOrder.setActivityName(activityOrderEntity.getActivityName());
            raffleActivityOrder.setStrategyId(activityOrderEntity.getStrategyId());
            raffleActivityOrder.setOrderId(activityOrderEntity.getOrderId());
            raffleActivityOrder.setTotalCount(activityOrderEntity.getTotalCount());
            raffleActivityOrder.setDayCount(activityOrderEntity.getDayCount());
            raffleActivityOrder.setMonthCount(activityOrderEntity.getMonthCount());
            raffleActivityOrder.setTotalCount(createAggregateOrder.getTotalCount());
            raffleActivityOrder.setDayCount(createAggregateOrder.getDayCount());
            raffleActivityOrder.setMonthCount(createAggregateOrder.getMonthCount());
            raffleActivityOrder.setOrderTime(activityOrderEntity.getOrderTime());
            raffleActivityOrder.setState(activityOrderEntity.getState().getCode());
            raffleActivityOrder.setOutBusinessNo(activityOrderEntity.getOutBusinessNo());

            //账户对象
            RaffleActivityAccount raffleActivityAccount = new RaffleActivityAccount();
            raffleActivityAccount.setUserId(createAggregateOrder.getUserId());
            raffleActivityAccount.setActivityId(activityOrderEntity.getActivityId());
            raffleActivityAccount.setTotalCount(createAggregateOrder.getTotalCount());
            raffleActivityAccount.setTotalCountSurplus(createAggregateOrder.getTotalCount());
            raffleActivityAccount.setDayCount(createAggregateOrder.getDayCount());
            raffleActivityAccount.setDayCountSurplus(createAggregateOrder.getDayCount());
            raffleActivityAccount.setMonthCount(createAggregateOrder.getMonthCount());
            raffleActivityAccount.setMonthCountSurplus(createAggregateOrder.getMonthCount());

            dbRouter.doRouter(createAggregateOrder.getUserId());

            transactionTemplate.execute(status -> {
                try {
                    // 1.写入订单
                    raffleActivityOrderDAO.insert(raffleActivityOrder);

                    // 2.更新账户
                    int count = raffleActivityAccountDAO.updateAccount(raffleActivityAccount);

                    // 3.创建账户 -更新为0 -则说明账户不存在 创建新账户
                    if (count == 0){
                        raffleActivityAccountDAO.insert(raffleActivityAccount);
                    }
                    return 1;
                }
                catch (DuplicateKeyException e){
                    status.setRollbackOnly();
                    log.error("写入订单记录,唯一索引冲突 userId：{} activityId:{} sku:{}",activityOrderEntity.getUserId(),activityOrderEntity.getActivityId(),activityOrderEntity.getSku());
                    throw new RuntimeException("订单已存在");
                }
            });
        } finally {
            dbRouter.clear();
        }
    }

    @Override
    public void cacheActivityStockSkuCount(String cacheKey, Integer stockCount) {
        log.info("缓存活动库存 key:{} value:{}",cacheKey,stockCount);
        redisService.setActomicLongValue(cacheKey,stockCount);
    }

    @Override
    public boolean subtractionActivityStockSkuCount(Integer sku, String cacheKey, Date endTime) {

        long count = redisService.decr(cacheKey);
        if (count < 0){
            redisService.setValue(cacheKey, 0);
            return false;
        }
        else if(count == 0){
            // 扣减后库存为0 发送mq消息清空库存
            eventPublisher.publish(ackZeroMessageEvent.topic(), ackZeroMessageEvent.buildMessage(sku));
            return false;
        }

        String lockKey = cacheKey + Commons.UNDERLINE + count;
        long expire = endTime.getTime() - System.currentTimeMillis() + TimeUnit.DAYS.toMillis(1);
        boolean lock = redisService.setNx(lockKey, expire, TimeUnit.MILLISECONDS);
        if (!lock){
            log.info("sku上锁失败");
        }
        return lock;
    }

    @Override
    public void activitySkuStockConsumeSendQueue(ActivitySkuStockVO build) {
        String cacheKey = Commons.RedisKey.ACTIVITY_SKU_STOCK_COUNT_KEY;
        RBlockingQueue<ActivitySkuStockVO> blockingDeque = redisService.getBlockingDeque(cacheKey);
        RDelayedQueue<ActivitySkuStockVO> delayedQueue = redisService.getDelayedQueue(blockingDeque);
        delayedQueue.offer(build, 3, TimeUnit.SECONDS);
    }

    @Override
    public ActivitySkuStockVO takeQueueValue() {
        String cacheKey = Commons.RedisKey.ACTIVITY_SKU_STOCK_COUNT_KEY;
        RBlockingQueue<ActivitySkuStockVO> blockingDeque = redisService.getBlockingDeque(cacheKey);
        return blockingDeque.poll();
    }

    @Override
    public void clearQueue() {
        String cacheKey = Commons.RedisKey.ACTIVITY_SKU_STOCK_COUNT_KEY;
        RBlockingQueue<ActivitySkuStockVO> blockingDeque = redisService.getBlockingDeque(cacheKey);
        blockingDeque.clear();
    }

    @Override
    public void updateActivitySkuStock(Integer sku) {
        raffleActivitySkuDAO.updateActivitySkuStock(sku);
    }

    @Override
    public void clearActivitySkuStock(Integer sku) {
        raffleActivitySkuDAO.clearActivitySkuStock(sku);
    }
}
