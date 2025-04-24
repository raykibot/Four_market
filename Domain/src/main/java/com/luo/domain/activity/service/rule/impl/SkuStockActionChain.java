package com.luo.domain.activity.service.rule.impl;

import com.luo.domain.activity.model.entity.ActivityCountEntity;
import com.luo.domain.activity.model.entity.ActivityEntity;
import com.luo.domain.activity.model.entity.ActivitySkuEntity;
import com.luo.domain.activity.model.vo.ActivitySkuStockVO;
import com.luo.domain.activity.repository.IActivityRepository;
import com.luo.domain.activity.service.armory.IActivityDispatch;
import com.luo.domain.activity.service.rule.AbstractActionChain;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("activity_sku_stock_action")
@Slf4j
public class SkuStockActionChain extends AbstractActionChain {


    @Autowired
    private IActivityDispatch activityDispatch;

    @Autowired
    private IActivityRepository activityRepository;


    @Override
    public Boolean actionChain(ActivitySkuEntity activitySkuEntity, ActivityEntity activityEntity, ActivityCountEntity activityCountEntity) {


        log.info("活动责任链-库存扣减检验 开始  activityId:{} sku:{}"
                , activityEntity.getActivityId(), activitySkuEntity.getSku());

        //库存扣减
        boolean status = activityDispatch.subtractionActivityStockSku(activitySkuEntity.getSku(), activityEntity.getEndDateTime());
        if (status) {
            log.info("活动责任链-库存扣减检验 成功  activityId:{} sku:{}"
                    , activityEntity.getActivityId(), activitySkuEntity.getSku());

            //写入延迟队列
            activityRepository.activitySkuStockConsumeSendQueue(
                    ActivitySkuStockVO.builder()
                            .sku(activitySkuEntity.getSku())
                            .activityId(activitySkuEntity.getActivityId())
                            .build());

            return true;
        }
        throw new RuntimeException("库存扣减失败");
    }
}
