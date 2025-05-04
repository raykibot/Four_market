package com.luo.infrastructure.repository;

import cn.bugstack.middleware.db.router.strategy.IDBRouterStrategy;
import com.luo.domain.activity.event.ActivityStockZeroMessageEvent;
import com.luo.domain.activity.model.aggregate.CreatePartakeOrderAggregate;
import com.luo.domain.activity.model.aggregate.CreateQuotaOrderAggregate;
import com.luo.domain.activity.model.entity.*;
import com.luo.domain.activity.model.vo.ActivitySkuStockVO;
import com.luo.domain.activity.model.vo.ActivityStateVO;
import com.luo.domain.activity.model.vo.UserRaffleOrderStateVO;
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
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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

    @Resource
    private IUserRaffleOrderDAO userRaffleOrderDAO;

    @Resource
    private IRaffleActivityAccountMonthDAO raffleActivityAccountMonthDAO;

    @Resource
    private IRaffleActivityAccountDayDAO raffleActivityAccountDayDAO;


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
    public void doSaveOrder(CreateQuotaOrderAggregate createQuotaOrderAggregate) {

        try{
            ActivityOrderEntity activityOrderEntity = createQuotaOrderAggregate.getActivityOrderEntity();
            RaffleActivityOrder raffleActivityOrder = new RaffleActivityOrder();
            raffleActivityOrder.setUserId(createQuotaOrderAggregate.getUserId());
            raffleActivityOrder.setSku(activityOrderEntity.getSku());
            raffleActivityOrder.setActivityId(activityOrderEntity.getActivityId());
            raffleActivityOrder.setActivityName(activityOrderEntity.getActivityName());
            raffleActivityOrder.setStrategyId(activityOrderEntity.getStrategyId());
            raffleActivityOrder.setOrderId(activityOrderEntity.getOrderId());
            raffleActivityOrder.setTotalCount(activityOrderEntity.getTotalCount());
            raffleActivityOrder.setDayCount(activityOrderEntity.getDayCount());
            raffleActivityOrder.setMonthCount(activityOrderEntity.getMonthCount());
            raffleActivityOrder.setTotalCount(createQuotaOrderAggregate.getTotalCount());
            raffleActivityOrder.setDayCount(createQuotaOrderAggregate.getDayCount());
            raffleActivityOrder.setMonthCount(createQuotaOrderAggregate.getMonthCount());
            raffleActivityOrder.setOrderTime(activityOrderEntity.getOrderTime());
            raffleActivityOrder.setState(activityOrderEntity.getState().getCode());
            raffleActivityOrder.setOutBusinessNo(activityOrderEntity.getOutBusinessNo());

            //账户对象
            RaffleActivityAccount raffleActivityAccount = new RaffleActivityAccount();
            raffleActivityAccount.setUserId(createQuotaOrderAggregate.getUserId());
            raffleActivityAccount.setActivityId(activityOrderEntity.getActivityId());
            raffleActivityAccount.setTotalCount(createQuotaOrderAggregate.getTotalCount());
            raffleActivityAccount.setTotalCountSurplus(createQuotaOrderAggregate.getTotalCount());
            raffleActivityAccount.setDayCount(createQuotaOrderAggregate.getDayCount());
            raffleActivityAccount.setDayCountSurplus(createQuotaOrderAggregate.getDayCount());
            raffleActivityAccount.setMonthCount(createQuotaOrderAggregate.getMonthCount());
            raffleActivityAccount.setMonthCountSurplus(createQuotaOrderAggregate.getMonthCount());

            dbRouter.doRouter(createQuotaOrderAggregate.getUserId());

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

    @Override
    public UserRaffleOrderEntity queryUnUsedUserRaffleOrder(PartakeRaffleActivityEntity partakeRaffleActivity) {
        UserRaffleOrder req = new UserRaffleOrder();
        req.setUserId(partakeRaffleActivity.getUserId());
        req.setActivityId(partakeRaffleActivity.getActivityId());

        UserRaffleOrder userRaffleOrder = userRaffleOrderDAO.queryUserRaffleOrder(req);

        if (userRaffleOrder != null){

            return UserRaffleOrderEntity.builder()
                    .userId(userRaffleOrder.getUserId())
                    .activityId(userRaffleOrder.getActivityId())
                    .strategyId(userRaffleOrder.getStrategyId())
                    .orderId(userRaffleOrder.getOrderId())
                    .orderTime(userRaffleOrder.getOrderTime())
                    .orderState(UserRaffleOrderStateVO.valueOf(userRaffleOrder.getOrderState()))
                    .build();
        }

        return null;
    }

    @Override
    public void saveCreatePartakeOrderAggregate(CreatePartakeOrderAggregate createPartakeOrderAggregate) {

        try {
            String userId = createPartakeOrderAggregate.getUserId();
            Integer activityId = createPartakeOrderAggregate.getActivityId();
            ActivityAccountEntity accountEntity = createPartakeOrderAggregate.getAccountEntity();
            ActivityAccountMonthEntity accountMonthEntity = createPartakeOrderAggregate.getAccountMonthEntity();
            ActivityAccountDayEntity accountDayEntity = createPartakeOrderAggregate.getAccountDayEntity();
            UserRaffleOrderEntity userRaffleOrderEntity = createPartakeOrderAggregate.getUserRaffleOrderEntity();

            dbRouter.doRouter(userId);
            transactionTemplate.execute(status -> {

                try {
                    // 1. 更新总账户
                    int totalCount = raffleActivityAccountDAO.updateActivityAccountQuota
                            (RaffleActivityAccount.builder()
                                    .userId(userId)
                                    .activityId(activityId)
                                    .build());
                    if (totalCount != 1){
                        status.setRollbackOnly();
                        throw new RuntimeException("更新总账户失败");
                    }

                    // 2. 创建或更新月账户，true - 存在则更新，false - 不存在则插入
                    if (createPartakeOrderAggregate.isExistAccountMonth()){
                        int updateMonthCount = raffleActivityAccountMonthDAO.updateActivityAccountMonthQuota
                                (RaffleActivityAccountMonth.builder()
                                        .userId(userId)
                                        .activityId(activityId)
                                        .month(accountMonthEntity.getMonth())
                                        .build());

                        if (updateMonthCount != 1){
                            //未更新成功 回滚事务
                            status.setRollbackOnly();
                            throw new RuntimeException("更新月账户失败");
                        }
                    } else {
                        raffleActivityAccountMonthDAO.insertActivityAccountMonth
                                (RaffleActivityAccountMonth.builder()
                                        .userId(accountMonthEntity.getUserId())
                                        .activityId(accountMonthEntity.getActivityId())
                                        .month(accountMonthEntity.getMonth())
                                        .monthCount(accountMonthEntity.getMonthCount())
                                        .monthCountSurplus(accountMonthEntity.getMonthCountSurplus() - 1)
                                        .build());

                        //新建月账户 则更新总账表中月镜像额度
                        raffleActivityAccountDAO.updateActvityAccountMonthSurplus
                                (RaffleActivityAccount.builder()
                                        .userId(userId)
                                        .activityId(activityId)
                                        .monthCountSurplus(accountEntity.getMonthCountSurplus())
                                        .build());
                    }




                    // 3. 创建或更新日账户，true - 存在则更新，false - 不存在则插入
                    if (createPartakeOrderAggregate.isExistAccountDay()){
                        int updateDayCount = raffleActivityAccountDayDAO.updateActivityAccountDayQuota
                                (RaffleActivityAccountDay.builder()
                                        .userId(userId)
                                        .activityId(activityId)
                                        .day(accountDayEntity.getDay())
                                        .build());

                        if (updateDayCount != 1){
                            status.setRollbackOnly();
                            throw new RuntimeException("更新日账户失败");
                        }
                    } else {
                        raffleActivityAccountDayDAO.insertActivityAccountDay
                                (RaffleActivityAccountDay.builder()
                                        .userId(accountDayEntity.getUserId())
                                        .activityId(accountDayEntity.getActivityId())
                                        .day(accountDayEntity.getDay())
                                        .dayCount(accountDayEntity.getDayCount())
                                        .dayCountSurplus(accountDayEntity.getDayCountSurplus() - 1)
                                        .build());
                        // 新建日账户 则更新总账表中日镜像额度
                        raffleActivityAccountDayDAO.updateActvityAccountDaySurplus
                                (RaffleActivityAccount.builder()
                                        .userId(userId)
                                        .activityId(activityId)
                                        .dayCountSurplus(accountEntity.getDayCountSurplus())
                                        .build());
                    }

                    // 写入参与活动订单
                    userRaffleOrderDAO.insert(UserRaffleOrder.builder()
                            .userId(userRaffleOrderEntity.getUserId())
                            .activityId(userRaffleOrderEntity.getActivityId())
                            .activityName(userRaffleOrderEntity.getActivityName())
                            .strategyId(userRaffleOrderEntity.getStrategyId())
                            .orderId(userRaffleOrderEntity.getOrderId())
                            .orderTime(userRaffleOrderEntity.getOrderTime())
                            .orderState(userRaffleOrderEntity.getOrderState().getCode())
                            .build());

                    return 1;
                } catch (DuplicateKeyException e){
                    status.setRollbackOnly();
                    throw new RuntimeException("唯一索引冲突");
                }
            });
        } finally {
            dbRouter.clear();
        }
    }

    @Override
    public ActivityAccountEntity queryActivityAccountByUserId(String userId, Integer activityId) {

        RaffleActivityAccount req = new RaffleActivityAccount();
        req.setUserId(userId);
        req.setActivityId(activityId);

        RaffleActivityAccount activityAccountEntity = raffleActivityAccountDAO.queryActivityAccountByUserId(req);

        if (activityAccountEntity == null) return null;

        return ActivityAccountEntity.builder()
                .userId(activityAccountEntity.getUserId())
                .activityId(activityAccountEntity.getActivityId())
                .totalCount(activityAccountEntity.getTotalCount())
                .totalCountSurplus(activityAccountEntity.getTotalCountSurplus())
                .monthCount(activityAccountEntity.getMonthCount())
                .monthCountSurplus(activityAccountEntity.getMonthCountSurplus())
                .dayCount(activityAccountEntity.getDayCount())
                .dayCountSurplus(activityAccountEntity.getDayCountSurplus())
                .build();
    }

    @Override
    public ActivityAccountMonthEntity queryActivityAccountMonthByUserId(String userId, Integer activityId, String month) {

        RaffleActivityAccountMonth req = new RaffleActivityAccountMonth();
        req.setUserId(userId);
        req.setActivityId(activityId);
        req.setMonth(month);

         RaffleActivityAccountMonth raffleActivityAccountMonth =  raffleActivityAccountMonthDAO.queryActivityAccountMonthByUserId(req);

         if (raffleActivityAccountMonth == null) return null;

        return ActivityAccountMonthEntity.builder()
                .userId(raffleActivityAccountMonth.getUserId())
                .activityId(raffleActivityAccountMonth.getActivityId())
                .month(raffleActivityAccountMonth.getMonth())
                .monthCount(raffleActivityAccountMonth.getMonthCount())
                .monthCountSurplus(raffleActivityAccountMonth.getMonthCountSurplus())
                .build();
    }

    @Override
    public ActivityAccountDayEntity queryActivityAccountDayByUserId(String userId, Integer activityId, String day) {

        RaffleActivityAccountDay req = new RaffleActivityAccountDay();
        req.setActivityId(activityId);
        req.setUserId(userId);
        req.setDay(day);
        RaffleActivityAccountDay raffleActivityAccountDay = raffleActivityAccountDayDAO.queryActivityAccountDayByUserId(req);

        if (raffleActivityAccountDay == null) return null;

        return ActivityAccountDayEntity.builder()
                .userId(raffleActivityAccountDay.getUserId())
                .activityId(raffleActivityAccountDay.getActivityId())
                .day(raffleActivityAccountDay.getDay())
                .dayCount(raffleActivityAccountDay.getDayCount())
                .dayCountSurplus(raffleActivityAccountDay.getDayCountSurplus())
                .build();
    }

    @Override
    public List<ActivitySkuEntity> queryActivitySkuByActivityId(Integer activityId) {

        List<RaffleActivitySku> raffleActivitySkuList = raffleActivityDAO.queryActivitySkuByActivityId(activityId);

        List<ActivitySkuEntity> activitySkuEntityList = new ArrayList<>();

        for (RaffleActivitySku raffleActivitySku : raffleActivitySkuList) {
            ActivitySkuEntity activitySkuEntity = ActivitySkuEntity.builder()
                    .sku(raffleActivitySku.getSku())
                    .activityId(raffleActivitySku.getActivityId())
                    .activityCountId(raffleActivitySku.getActivityCountId())
                    .stockCount(raffleActivitySku.getStockCount())
                    .stockCountSurplus(raffleActivitySku.getStockCountSurplus())
                    .build();
            activitySkuEntityList.add(activitySkuEntity);
        }
        return activitySkuEntityList;
    }
}
