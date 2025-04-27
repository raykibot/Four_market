package com.luo.domain.activity.service.quota.rule.impl;

import com.luo.domain.activity.model.entity.ActivityCountEntity;
import com.luo.domain.activity.model.entity.ActivityEntity;
import com.luo.domain.activity.model.entity.ActivitySkuEntity;
import com.luo.domain.activity.model.vo.ActivityStateVO;
import com.luo.domain.activity.service.quota.rule.AbstractActionChain;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component("activity_base_action")
@Slf4j
public class SkuBaseActionChain extends AbstractActionChain {
    @Override
    public Boolean actionChain(ActivitySkuEntity activitySkuEntity, ActivityEntity activityEntity, ActivityCountEntity activityCountEntity) {

        log.info("活动责任链-基础信息 开始【有效期、状态 库存信息校验】 activityId:{} sku:{}"
        , activityEntity.getActivityId(), activitySkuEntity.getSku());

        //活动状态校验
        if(!ActivityStateVO.open.equals(activityEntity.getState())){
            throw new RuntimeException("活动未开启");
        }

        //活动时间检验
        Date current = new Date();
        if(activityEntity.getBeginDateTime().after(current) || activityEntity.getEndDateTime().before(current)){
            throw new RuntimeException("活动过期");
        }

        //活动库存检验
        if (activitySkuEntity.getStockCountSurplus() <= 0){
            throw new RuntimeException("库存不足");
        }
        return next().actionChain(activitySkuEntity, activityEntity, activityCountEntity);
    }
}
