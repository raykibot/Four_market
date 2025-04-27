package com.luo.domain.activity.service.partake;

import com.luo.domain.activity.model.aggregate.CreatePartakeOrderAggregate;
import com.luo.domain.activity.model.entity.*;
import com.luo.domain.activity.model.vo.UserRaffleOrderStateVO;
import com.luo.domain.activity.repository.IActivityRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;


@Service
public class RaffleActivityPartakeService extends AbstractRaffleActivityPartake {

    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM");
    private final SimpleDateFormat simpleDateFormatDay = new SimpleDateFormat("yyyy-MM-dd");

    public RaffleActivityPartakeService(IActivityRepository activityRepository) {
        super(activityRepository);
    }

    @Override
    protected CreatePartakeOrderAggregate doFilterAccount(String userId, Integer activityId, Date currentTime) {

        //查询总账户额度
        ActivityAccountEntity activityAccountEntity = activityRepository.queryActivityAccountByUserId(userId, activityId);

        // 额度判断 只判断总额度
        if (activityAccountEntity == null || activityAccountEntity.getTotalCount() <= 0){
            throw new RuntimeException("总额度不足");
        }

        String month = simpleDateFormat.format(currentTime);
        String day = simpleDateFormatDay.format(currentTime);

        //查询月账户
        ActivityAccountMonthEntity activityAccountMonthEntity = activityRepository.queryActivityAccountMonthByUserId(userId, activityId, month);
        if (activityAccountMonthEntity != null && activityAccountMonthEntity.getMonthCountSurplus() <= 0){
            throw new RuntimeException("账户月账户余额不足");
        }

        boolean isExistAccountMonth = activityAccountMonthEntity != null;
        if (activityAccountMonthEntity == null){
            activityAccountMonthEntity = new ActivityAccountMonthEntity();
            activityAccountMonthEntity.setUserId(userId);
            activityAccountMonthEntity.setActivityId(activityId);
            activityAccountMonthEntity.setMonth(month);
            activityAccountMonthEntity.setMonthCount(activityAccountEntity.getMonthCount());
            activityAccountMonthEntity.setMonthCountSurplus(activityAccountEntity.getMonthCount());
        }

        //查询日账户
        ActivityAccountDayEntity activityAccountDayEntity = activityRepository.queryActivityAccountDayByUserId(userId, activityId, day);
        if (activityAccountDayEntity != null && activityAccountDayEntity.getDayCountSurplus() <= 0){
            throw new RuntimeException("账户日账户余额不足");
        }

        boolean isExistAccountDay = activityAccountDayEntity != null;
        if (activityAccountDayEntity == null){
            activityAccountDayEntity = new ActivityAccountDayEntity();
            activityAccountDayEntity.setUserId(userId);
            activityAccountDayEntity.setActivityId(activityId);
            activityAccountDayEntity.setDay(day);
            activityAccountDayEntity.setDayCount(activityAccountEntity.getDayCount());
            activityAccountDayEntity.setDayCountSurplus(activityAccountEntity.getDayCount());
        }

        //构建对象
        return CreatePartakeOrderAggregate.builder()
                .userId(userId)
                .activityId(activityId)
                .accountEntity(activityAccountEntity)
                .accountMonthEntity(activityAccountMonthEntity)
                .accountDayEntity(activityAccountDayEntity)
                .isExistAccountMonth(isExistAccountMonth)
                .isExistAccountDay(isExistAccountDay)
                .build();
    }

    @Override
    protected UserRaffleOrderEntity buildUserRaffleOrderEntity(String userId, Integer activityId, Date currentTime) {

        ActivityEntity activityEntity = activityRepository.queryRaffleActivityByActivityId(activityId);

        UserRaffleOrderEntity userRaffleOrderEntity = new UserRaffleOrderEntity();
        userRaffleOrderEntity.setUserId(userId);
        userRaffleOrderEntity.setActivityId(activityId);
        userRaffleOrderEntity.setActivityName(activityEntity.getActivityName());
        userRaffleOrderEntity.setOrderState(UserRaffleOrderStateVO.create);
        userRaffleOrderEntity.setOrderId(RandomStringUtils.randomNumeric(12));
        userRaffleOrderEntity.setOrderTime(currentTime);
        userRaffleOrderEntity.setStrategyId(activityEntity.getStrategyId());

        return userRaffleOrderEntity;
    }

}
