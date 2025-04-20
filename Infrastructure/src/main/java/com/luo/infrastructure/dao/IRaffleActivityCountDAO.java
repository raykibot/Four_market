package com.luo.infrastructure.dao;

import com.luo.infrastructure.pojo.RaffleActivityCount;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IRaffleActivityCountDAO {
    RaffleActivityCount queryRaffleActivityCountByActivityCountId(Integer activityCountId);
}
