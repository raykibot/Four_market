package com.luo.trigger.job;

import com.luo.domain.activity.model.vo.ActivitySkuStockVO;
import com.luo.domain.activity.service.ISkuStock;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class UpdateActivitySkuStockJob {


    @Autowired
    private ISkuStock skuStock;

    @Scheduled(cron = "0/5 * * * * ?")
    public void updateActivitySkuStock(){
        try {
            log.info("定时任务 更新sku库存");
            ActivitySkuStockVO activitySkuStockVO = skuStock.takeQueueValue();
            if (activitySkuStockVO == null) return;
            log.info("定时任务 更新sku库存 activityId:{} sku:{}",activitySkuStockVO.getActivityId(),activitySkuStockVO.getSku());
            skuStock.updateActivitySkuStock(activitySkuStockVO.getSku());
        } catch (Exception e) {
            log.error("定时任务 更新sku库存 error:{}",e.getMessage());
        }
    }
}
