package com.luo.infrastructure.dao;

import cn.bugstack.middleware.db.router.annotation.DBRouter;
import com.luo.infrastructure.pojo.RaffleActivityAccountMonth;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IRaffleActivityAccountMonthDAO {


    int updateActivityAccountMonthQuota(RaffleActivityAccountMonth build);

    void insertActivityAccountMonth(RaffleActivityAccountMonth build);

    @DBRouter
    RaffleActivityAccountMonth queryActivityAccountMonthByUserId(RaffleActivityAccountMonth req);
}
