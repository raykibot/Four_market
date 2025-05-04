package com.luo.domain.award.service;

import com.luo.domain.award.event.SendAwardMessageEvent;
import com.luo.domain.award.model.aggregate.AggregateAwardRecordEntity;
import com.luo.domain.award.model.entity.TaskEntity;
import com.luo.domain.award.model.entity.UserAwardRecordEntity;
import com.luo.domain.award.model.vo.TaskStateVO;
import com.luo.domain.award.repository.IAwardRepository;
import com.luo.type.envent.BaseEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class AwardService implements IAwardService{


    @Autowired
    private IAwardRepository awardRepository;

    @Autowired
    private SendAwardMessageEvent sendAwardMessageEvent;


    @Override
    public void saveUserAwardRecord(UserAwardRecordEntity userAwardRecordEntity) {


        //构建消息对象
        SendAwardMessageEvent.SendMessage sendMessage = new SendAwardMessageEvent.SendMessage();
        sendMessage.setUserId(userAwardRecordEntity.getUserId());
        sendMessage.setAwardId(userAwardRecordEntity.getAwardId());
        sendMessage.setAwardTitle(userAwardRecordEntity.getAwardTitle());

        BaseEvent.EventMessage<SendAwardMessageEvent.SendMessage> sendMessageEventMessage = sendAwardMessageEvent.buildMessage(sendMessage);


        //构建任务对象
        TaskEntity taskEntity = new TaskEntity();
        taskEntity.setUserId(sendMessage.getUserId());
        taskEntity.setTopic(sendAwardMessageEvent.topic());
        taskEntity.setMessage(sendMessageEventMessage);
        taskEntity.setMessageId(sendMessageEventMessage.getId());
        taskEntity.setState(TaskStateVO.create);


        //构建聚合对象

        AggregateAwardRecordEntity aggregateAwardRecordEntity = new AggregateAwardRecordEntity();
        aggregateAwardRecordEntity.setTaskEntity(taskEntity);
        aggregateAwardRecordEntity.setUserAwardRecordEntity(userAwardRecordEntity);


        //存储聚合对象 一个事务操作

        awardRepository.saveAggregateAwardRecordEntity(aggregateAwardRecordEntity);

    }



}
