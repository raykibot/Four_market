package com.luo.domain.activity.service;

import com.luo.domain.activity.model.vo.ActivitySkuStockVO;

public interface ISkuStock {


    /**
     * 获取sku库存消耗队列
     */
    ActivitySkuStockVO takeQueueValue();

    /**
     * 清空延迟队列数据
     *
     */
    void clearQueue();


    /**
     * 延迟队列更新数据库库存
     */
    void updateActivitySkuStock(Integer sku);


    /**
     * 数据库存消耗完毕 清空数据库
     */
    void clearActivitySkuStock(Integer sku);

}
