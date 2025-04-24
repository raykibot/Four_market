package com.luo.domain.activity.event;

import com.luo.type.envent.BaseEvent;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class ActivityStockZeroMessageEvent extends BaseEvent<Integer> {


    @Value("${spring.rabbitmq.topic.activity_sku_stock_zero}")
    String topic ;


    @Override
    public EventMessage<Integer> buildMessage(Integer sku) {
        return EventMessage.<Integer>builder()
                .id(RandomStringUtils.randomNumeric(11))
                .timeStamp(new Date())
                .data(sku)
                .build();
    }

    @Override
    public String topic() {
        return topic;
    }
}
