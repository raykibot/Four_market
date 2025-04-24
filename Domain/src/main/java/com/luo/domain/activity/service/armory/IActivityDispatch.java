package com.luo.domain.activity.service.armory;

import java.util.Date;

public interface IActivityDispatch {


    /**
     * 根据sku和结束时间加锁 扣减库存
     * @param sku
     * @param endTime
     * @return
     */
    boolean subtractionActivityStockSku(Integer sku, Date endTime);

}
