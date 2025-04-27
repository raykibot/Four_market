package com.luo.infrastructure.dao;

import cn.bugstack.middleware.db.router.annotation.DBRouter;
import com.luo.domain.activity.model.entity.ActivityAccountEntity;
import com.luo.infrastructure.pojo.RaffleActivityAccount;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IRaffleActivityAccountDAO {
    int updateAccount(RaffleActivityAccount raffleActivityAccount);

    void insert(RaffleActivityAccount raffleActivityAccount);

    int updateActivityAccountQuota(RaffleActivityAccount build);

    void updateActvityAccountMonthSurplus(RaffleActivityAccount build);

    @DBRouter
    RaffleActivityAccount queryActivityAccountByUserId(RaffleActivityAccount req);
}
