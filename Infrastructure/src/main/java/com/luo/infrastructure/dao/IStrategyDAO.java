package com.luo.infrastructure.dao;

import com.luo.infrastructure.pojo.Strategy;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IStrategyDAO {

    Strategy queryStrategyEntityByStrategyId(Long strategyId);

    Long queryStrategyIdByActivityId(Integer activityId);
}
