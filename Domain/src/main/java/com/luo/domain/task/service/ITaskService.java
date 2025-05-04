package com.luo.domain.task.service;




import com.luo.domain.task.model.entity.TaskEntity;

import java.util.List;

public interface ITaskService {



    List<TaskEntity> queryUnSendMessageTaskList();

    void senMessage(TaskEntity taskEntity);

    void updateTaskCompleted(String userId, String messageId);

    void updateTaskFailed(String userId, String messageId);




}
