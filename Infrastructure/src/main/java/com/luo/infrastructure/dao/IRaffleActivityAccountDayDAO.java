package com.luo.infrastructure.dao;

import cn.bugstack.middleware.db.router.annotation.DBRouter;
import com.luo.infrastructure.pojo.RaffleActivityAccount;
import com.luo.infrastructure.pojo.RaffleActivityAccountDay;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IRaffleActivityAccountDayDAO {
    int updateActivityAccountDayQuota(RaffleActivityAccountDay build);

    void insertActivityAccountDay(RaffleActivityAccountDay build);

    void updateActvityAccountDaySurplus(RaffleActivityAccount build);

    @DBRouter
    RaffleActivityAccountDay queryActivityAccountDayByUserId(RaffleActivityAccountDay req);

    @DBRouter
    Integer queryActivityAccountDayPartakeCount(RaffleActivityAccountDay req);
}
