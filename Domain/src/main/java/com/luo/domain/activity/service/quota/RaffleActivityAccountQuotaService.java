package com.luo.domain.activity.service.quota;

import com.luo.domain.activity.model.aggregate.CreateQuotaOrderAggregate;
import com.luo.domain.activity.model.entity.*;
import com.luo.domain.activity.model.vo.ActivitySkuStockVO;
import com.luo.domain.activity.model.vo.OrderStateVO;
import com.luo.domain.activity.repository.IActivityRepository;
import com.luo.domain.activity.service.IRaffleActivitySkuStockService;
import com.luo.domain.activity.service.quota.rule.factory.DefaultActionChainFactory;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class RaffleActivityAccountQuotaService extends AbstractRaffleActivityAccountQuota implements IRaffleActivitySkuStockService {


    public RaffleActivityAccountQuotaService(DefaultActionChainFactory defaultActionChainFactory, IActivityRepository activityRepository) {
        super(defaultActionChainFactory, activityRepository);
    }

    @Override
    protected CreateQuotaOrderAggregate buildAggregateOrder(SkuRechargeEntity skuRechargeEntity, ActivitySkuEntity activitySkuEntity, ActivityEntity activityEntity, ActivityCountEntity activityCountEntity) {

        //订单实体对象
        ActivityOrderEntity activityOrder = new ActivityOrderEntity();
        activityOrder.setUserId(skuRechargeEntity.getUserId());
        activityOrder.setSku(skuRechargeEntity.getSku());
        activityOrder.setActivityId(activityEntity.getActivityId());
        activityOrder.setActivityName(activityEntity.getActivityName());
        activityOrder.setStrategyId(activityEntity.getStrategyId());

        activityOrder.setOrderId(RandomStringUtils.randomNumeric(12));
        activityOrder.setOrderTime(new Date());
        activityOrder.setTotalCount(activityCountEntity.getTotalCount());
        activityOrder.setDayCount(activityCountEntity.getDayCount());
        activityOrder.setMonthCount(activityCountEntity.getMonthCount());
        activityOrder.setState(OrderStateVO.completed);
        activityOrder.setOutBusinessNo(skuRechargeEntity.getOutBusinessNo());

        return CreateQuotaOrderAggregate.builder()
                .userId(skuRechargeEntity.getUserId())
                .activityId(activityEntity.getActivityId())
                .totalCount(activityCountEntity.getTotalCount())
                .dayCount(activityCountEntity.getDayCount())
                .monthCount(activityCountEntity.getMonthCount())
                .activityOrderEntity(activityOrder)
                .build();
    }

    @Override
    protected void doSaveOrder(CreateQuotaOrderAggregate createQuotaOrderAggregate) {
        activityRepository.doSaveOrder(createQuotaOrderAggregate);
    }

    @Override
    public ActivitySkuStockVO takeQueueValue() {
        return activityRepository.takeQueueValue();
    }

    @Override
    public void clearQueue() {
        activityRepository.clearQueue();
    }

    @Override
    public void updateActivitySkuStock(Integer sku) {
        activityRepository.updateActivitySkuStock(sku);
    }

    @Override
    public void clearActivitySkuStock(Integer sku) {
        activityRepository.clearActivitySkuStock(sku);
    }

    @Override
    public Integer queryRaffleActivityAccountDayPartakeCount(Integer activityId, String userId) {
        return activityRepository.queryRaffleActivityAccountDayPartakeCount(activityId, userId);
    }
}
