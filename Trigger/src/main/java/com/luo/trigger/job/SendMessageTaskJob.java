package com.luo.trigger.job;

import cn.bugstack.middleware.db.router.strategy.IDBRouterStrategy;
import com.luo.domain.task.model.entity.TaskEntity;
import com.luo.domain.task.service.ITaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

@Slf4j
@Component()
public class SendMessageTaskJob {


    @Autowired
    private ITaskService taskService;

    @Autowired
    private IDBRouterStrategy dbRouter;

    @Autowired
    private ThreadPoolExecutor executor;


    @Scheduled(cron = "0/5 * * * * ?")
    public void execute() {

        try{
            //获取分库数量
            int count = dbRouter.dbCount();

            for (int dbCount = 1; dbCount <= count; dbCount++) {
                int dbCount1 = dbCount;
                executor.execute(() -> {
                    try {
                        dbRouter.setDBKey(dbCount1);
                        dbRouter.setTBKey(0);
                        List<TaskEntity> taskEntities = taskService.queryUnSendMessageTaskList();
                        if (taskEntities.isEmpty()) return;
                        //发送MQ消息
                        for (TaskEntity taskEntity : taskEntities) {
                            executor.execute(() -> {
                                try {
                                    taskService.senMessage(taskEntity);
                                    taskService.updateTaskCompleted(taskEntity.getUserId(), taskEntity.getMessageId());
                                } catch (Exception e) {
                                    throw new RuntimeException("定时任务发送任务mq失败");
                                }
                            });
                        }
                    } finally {
                        dbRouter.clear();
                    }
                });
            }
        } catch (Exception e){
            throw new RuntimeException("定时任务 扫描发送任务mq失败");
        } finally {
            dbRouter.clear();
        }
    }
}
