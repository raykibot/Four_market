package com.luo.infrastructure.repository;



import com.luo.domain.task.model.entity.TaskEntity;
import com.luo.domain.task.repository.ITaskRepository;
import com.luo.infrastructure.dao.ITaskDAO;
import com.luo.infrastructure.envent.EventPublisher;
import com.luo.infrastructure.pojo.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class TaskRepository implements ITaskRepository {

    @Autowired
    private ITaskDAO taskDAO;
    @Autowired
    private EventPublisher publisher;

    @Override
    public List<TaskEntity> queryUnSendMessageTaskList() {

        List<Task> list  = taskDAO.queryUnSendMessageTaskList();
        List<TaskEntity> taskEntities = new ArrayList<>(list.size());
        for (Task task : list) {
            TaskEntity build = TaskEntity.builder()
                    .userId(task.getUserId())
                    .topic(task.getTopic())
                    .messageId(task.getMessageId())
                    .message(task.getMessage())
                    .build();
            taskEntities.add(build);
        }
        return taskEntities;
    }

    @Override
    public void senMessage(TaskEntity taskEntity) {
        publisher.publish(taskEntity.getTopic(), taskEntity.getMessage());
    }

    @Override
    public void updateTaskCompleted(String userId, String messageId) {
        Task task = Task.builder()
                .userId(userId)
                .messageId(messageId)
                .build();
        taskDAO.updateTaskSendMessageCompleted(task);
    }

    @Override
    public void updateTaskFailed(String userId, String messageId) {
        Task task = Task.builder()
                .userId(userId)
                .messageId(messageId)
                .build();
        taskDAO.updateTaskSendMessageFailed(task);
    }
}
