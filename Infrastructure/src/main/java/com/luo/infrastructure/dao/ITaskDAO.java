package com.luo.infrastructure.dao;


import cn.bugstack.middleware.db.router.annotation.DBRouter;
import com.luo.infrastructure.pojo.Task;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ITaskDAO {
    void insert(Task task);

    @DBRouter
    void updateTaskSendMessageCompleted(Task task);

    List<Task> queryUnSendMessageTaskList();

    void updateTaskSendMessageFailed(Task task);
}
