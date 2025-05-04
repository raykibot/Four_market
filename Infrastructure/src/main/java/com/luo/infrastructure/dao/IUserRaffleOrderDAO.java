package com.luo.infrastructure.dao;

import cn.bugstack.middleware.db.router.annotation.DBRouter;
import cn.bugstack.middleware.db.router.annotation.DBRouterStrategy;
import com.luo.domain.activity.model.entity.UserRaffleOrderEntity;
import com.luo.infrastructure.pojo.UserRaffleOrder;
import org.apache.ibatis.annotations.Mapper;

@Mapper
@DBRouterStrategy(splitTable = true)
public interface IUserRaffleOrderDAO {


    @DBRouter
    UserRaffleOrder queryUserRaffleOrder(UserRaffleOrder req);

    void insert(UserRaffleOrder build);

    int updateUserRaffleOrderStateUsed(UserRaffleOrderEntity req);
}
