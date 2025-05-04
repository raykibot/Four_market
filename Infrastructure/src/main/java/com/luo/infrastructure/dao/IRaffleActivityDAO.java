package com.luo.infrastructure.dao;

import com.luo.infrastructure.pojo.RaffleActivity;
import com.luo.infrastructure.pojo.RaffleActivitySku;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IRaffleActivityDAO {
    RaffleActivity queryRaffleActivityByActivityId(long activityId);

    List<RaffleActivitySku> queryActivitySkuByActivityId(Integer activityId);
}
