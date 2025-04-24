package com.luo.infrastructure.envent;

import com.alibaba.fastjson.JSON;
import com.luo.type.envent.BaseEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Slf4j
@Component
public class EventPublisher {


    @Autowired
    private RabbitTemplate rabbitTemplate;


    public void publish(String topic, BaseEvent.EventMessage<?> baseEvent){
        try {
            String messageJSON = JSON.toJSONString(baseEvent);
            rabbitTemplate.convertAndSend(topic, messageJSON);
            log.info("库存为0 发送mq消息");
        }
        catch (Exception e){
            log.info("发送mq失败");
            throw e;
        }
    }





}
