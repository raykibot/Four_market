package com.luo.trigger.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SendAwardConsumer {


    @Value("${spring.rabbitmq.topic.send_award}")
    private String topic;


    @RabbitListener(queuesToDeclare = @Queue(value = "send_award"))
    public void listener(String message){
        try {
            log.info("监听send_award消息 topic:{} message:{}",topic,message);
        }
        catch (Exception e) {
            log.error("监听send_award消息 error:{}",e.getMessage());
            throw new RuntimeException(e);
        }
    }

}
