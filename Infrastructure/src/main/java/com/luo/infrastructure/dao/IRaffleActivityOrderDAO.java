package com.luo.infrastructure.dao;

import cn.bugstack.middleware.db.router.annotation.DBRouter;
import cn.bugstack.middleware.db.router.annotation.DBRouterStrategy;
import com.luo.infrastructure.pojo.RaffleActivity;
import com.luo.infrastructure.pojo.RaffleActivityOrder;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
@DBRouterStrategy(splitTable = true)
public interface IRaffleActivityOrderDAO {



    @DBRouter(key = "userId")
    void insert(RaffleActivityOrder raffleActivityorder);

    @DBRouter
    List<RaffleActivityOrder> queryActivityOrderList(String userId);

}
