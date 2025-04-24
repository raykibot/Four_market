package com.luo.infrastructure.dao;

import com.luo.infrastructure.pojo.RaffleActivitySku;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IRaffleActivitySkuDAO {
    RaffleActivitySku queryRaffleActivitySkuBySku(Integer sku);

    void updateActivitySkuStock(Integer sku);

    void clearActivitySkuStock(Integer sku);
}
