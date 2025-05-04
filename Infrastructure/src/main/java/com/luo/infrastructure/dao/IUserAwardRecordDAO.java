package com.luo.infrastructure.dao;

import cn.bugstack.middleware.db.router.annotation.DBRouterStrategy;
import com.luo.infrastructure.pojo.UserAwardRecord;
import org.apache.ibatis.annotations.Mapper;

@Mapper
@DBRouterStrategy(splitTable = true)
public interface IUserAwardRecordDAO {


    void insert(UserAwardRecord userAwardRecord);


}
