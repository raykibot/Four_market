package com.luo.infrastructure.dao;

import com.luo.infrastructure.pojo.RaffleActivity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IRaffleActivityDAO {
    RaffleActivity queryRaffleActivityByActivityId(long activityId);
}
