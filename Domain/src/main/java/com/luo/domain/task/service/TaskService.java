package com.luo.domain.task.service;


import com.luo.domain.task.model.entity.TaskEntity;
import com.luo.domain.task.repository.ITaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.config.Task;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService implements ITaskService{

    @Autowired
    private ITaskRepository taskRepository;


    @Override
    public List<TaskEntity> queryUnSendMessageTaskList() {
        return taskRepository.queryUnSendMessageTaskList();
    }

    @Override
    public void senMessage(TaskEntity taskEntity) {
        taskRepository.senMessage(taskEntity);
    }

    @Override
    public void updateTaskCompleted(String userId, String messageId) {
        taskRepository.updateTaskCompleted(userId,messageId);
    }

    @Override
    public void updateTaskFailed(String userId, String messageId) {
        taskRepository.updateTaskFailed(userId,messageId);
    }
}
