package com.luo.domain.activity.service.armory;

public interface IActivityArmory {


    /**
     * 装配并预热库存
     * @param sku
     * @return
     */
    boolean assembleActivitySku(Integer sku);


    boolean assembleActivitySkuByActivityId(Integer activityId);


}
