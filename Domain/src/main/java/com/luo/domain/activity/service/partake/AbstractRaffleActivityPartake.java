package com.luo.domain.activity.service.partake;

import com.luo.domain.activity.model.aggregate.CreatePartakeOrderAggregate;
import com.luo.domain.activity.model.entity.ActivityEntity;
import com.luo.domain.activity.model.entity.PartakeRaffleActivityEntity;
import com.luo.domain.activity.model.entity.UserRaffleOrderEntity;
import com.luo.domain.activity.model.vo.ActivityStateVO;
import com.luo.domain.activity.repository.IActivityRepository;
import com.luo.domain.activity.service.IRaffleActivityPartakeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;


@Service
@Slf4j
public abstract class AbstractRaffleActivityPartake implements IRaffleActivityPartakeService {

    protected IActivityRepository activityRepository;

    public AbstractRaffleActivityPartake(IActivityRepository activityRepository) {
        this.activityRepository = activityRepository;
    }


    @Override
    public UserRaffleOrderEntity createOrder(String userId, Integer activityId){
        PartakeRaffleActivityEntity partakeRaffleActivity = new PartakeRaffleActivityEntity();
        partakeRaffleActivity.setUserId(userId);
        partakeRaffleActivity.setActivityId(activityId);
        return createOrder(partakeRaffleActivity);
    }


    @Override
    public UserRaffleOrderEntity createOrder(PartakeRaffleActivityEntity partakeRaffleActivity) {

        //1. 基础信息
        String userId = partakeRaffleActivity.getUserId();
        Integer activityId = partakeRaffleActivity.getActivityId();
        Date currentTime = new Date();

        //2. 活动查询
        ActivityEntity activityEntity = activityRepository.queryRaffleActivityByActivityId(activityId);

        // 2.1  校验活动状态
        if (!activityEntity.getState().equals(ActivityStateVO.open)){
            throw new RuntimeException("活动未开启");
        }

        // 2.2 校验活动时间
        if (activityEntity.getBeginDateTime().after(currentTime) || activityEntity.getEndDateTime().before(currentTime)){
            throw new RuntimeException("活动过期");
        }

        // 查询未被使用的活动订单参与记录
        UserRaffleOrderEntity userRaffleOrderEntity = activityRepository.queryUnUsedUserRaffleOrder(partakeRaffleActivity);
        if ( userRaffleOrderEntity != null){
            log.info("已存在订单记录 直接返回");
            return userRaffleOrderEntity;
        }

        //3. 额度账户过滤和&返回账户构建对象
        CreatePartakeOrderAggregate createPartakeOrderAggregate = this.doFilterAccount(userId, activityId, currentTime);

        //4. 构建订单
        UserRaffleOrderEntity userRaffleOrder = this.buildUserRaffleOrderEntity(userId, activityId, currentTime);

        //5. 填充抽奖单实体对象
        createPartakeOrderAggregate.setUserRaffleOrderEntity(userRaffleOrder);

        // 6. 保存聚合对象 事务操作
        activityRepository.saveCreatePartakeOrderAggregate(createPartakeOrderAggregate);

        // 7. 返回订单
        return userRaffleOrder;

    }

    protected abstract CreatePartakeOrderAggregate doFilterAccount(String userId, Integer activityId, Date currentTime);

    protected abstract UserRaffleOrderEntity buildUserRaffleOrderEntity(String userId, Integer activityId, Date currentTime) ;
}
