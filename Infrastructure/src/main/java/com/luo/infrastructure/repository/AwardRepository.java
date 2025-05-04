package com.luo.infrastructure.repository;

import cn.bugstack.middleware.db.router.annotation.DBRouter;
import cn.bugstack.middleware.db.router.strategy.IDBRouterStrategy;
import com.alibaba.fastjson.JSON;
import com.luo.domain.activity.model.entity.UserRaffleOrderEntity;
import com.luo.domain.activity.model.vo.UserRaffleOrderStateVO;
import com.luo.domain.award.model.aggregate.AggregateAwardRecordEntity;
import com.luo.domain.award.model.entity.TaskEntity;
import com.luo.domain.award.model.entity.UserAwardRecordEntity;
import com.luo.domain.award.repository.IAwardRepository;
import com.luo.infrastructure.dao.ITaskDAO;
import com.luo.infrastructure.dao.IUserAwardRecordDAO;
import com.luo.infrastructure.dao.IUserRaffleOrderDAO;
import com.luo.infrastructure.envent.EventPublisher;
import com.luo.infrastructure.pojo.Task;
import com.luo.infrastructure.pojo.UserAwardRecord;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Resource;

@Slf4j
@Repository
public class AwardRepository implements IAwardRepository {


    @Autowired
    private IDBRouterStrategy dbRouter;
    @Autowired
    private TransactionTemplate transactionTemplate;
    @Autowired
    private IUserAwardRecordDAO userAwardRecordDao;
    @Autowired
    private ITaskDAO taskDao;
    @Autowired
    private IUserRaffleOrderDAO userRaffleOrderDAO;
    @Autowired
    private EventPublisher publisher;


    @Override
    public void saveAggregateAwardRecordEntity(AggregateAwardRecordEntity aggregateAwardRecordEntity) {

        TaskEntity taskEntity = aggregateAwardRecordEntity.getTaskEntity();
        UserAwardRecordEntity userAwardRecordEntity = aggregateAwardRecordEntity.getUserAwardRecordEntity();

        String userId = userAwardRecordEntity.getUserId();
        Integer activityId = userAwardRecordEntity.getActivityId();
        Integer awardId = userAwardRecordEntity.getAwardId();

        //转化存储对象
        UserAwardRecord userAwardRecord = new UserAwardRecord();
        userAwardRecord.setUserId(userId);
        userAwardRecord.setActivityId(activityId);
        userAwardRecord.setAwardId(awardId);
        userAwardRecord.setStrategyId(userAwardRecordEntity.getStrategyId());
        userAwardRecord.setOrderId(userAwardRecordEntity.getOrderId());
        userAwardRecord.setAwardTitle(userAwardRecordEntity.getAwardTitle());
        userAwardRecord.setOrderTime(userAwardRecordEntity.getOrderTime());
        userAwardRecord.setAwardState(userAwardRecordEntity.getAwardState().getCode());

        Task task = new Task();
        task.setUserId(userId);
        task.setTopic(taskEntity.getTopic());
        task.setMessageId(taskEntity.getMessageId());
        task.setMessage(JSON.toJSONString(taskEntity.getMessage()));
        task.setState(taskEntity.getState().getCode());

        UserRaffleOrderEntity req = new UserRaffleOrderEntity();
        req.setUserId(userAwardRecordEntity.getUserId());
        req.setOrderId(userAwardRecordEntity.getOrderId());

        try {
            dbRouter.doRouter(userId);
            transactionTemplate.execute(status -> {
                try {
                    //写入记录
                    userAwardRecordDao.insert(userAwardRecord);
                    //写入任务
                    taskDao.insert(task);
                    //更新抽奖单状态
                    int count = userRaffleOrderDAO.updateUserRaffleOrderStateUsed(req);
                    return 1;
                }
                catch (DuplicateKeyException e){
                    throw new RuntimeException("唯一索引冲突");
                }
            });
        } finally {
            dbRouter.clear();
        }

        try {
            //发送消息
            publisher.publish(task.getTopic(), task.getMessage());

            //更新数据库
            taskDao.updateTaskSendMessageCompleted(task);
        }
        catch (Exception e){
            throw new RuntimeException("写入中奖记录，发送MQ消息失败");
        }
    }
}
