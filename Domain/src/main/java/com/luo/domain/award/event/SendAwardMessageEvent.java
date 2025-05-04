package com.luo.domain.award.event;

import com.luo.type.envent.BaseEvent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class SendAwardMessageEvent extends BaseEvent<SendAwardMessageEvent.SendMessage> {

    @Value("${spring.rabbitmq.topic.send_award}")
    private String topic;

    @Override
    public EventMessage<SendMessage> buildMessage(SendMessage data) {
        return EventMessage.<SendMessage>builder()
                .id(RandomStringUtils.randomNumeric(12))
                .timeStamp(new Date())
                .data(data)
                .build();
    }

    @Override
    public String topic() {
        return topic;
    }


    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SendMessage{
        private String userId;

        private String awardTitle;

        private Integer awardId;
    }
}
