package com.luo.trigger.listener;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import com.luo.domain.activity.service.IRaffleActivitySkuStockService;
import com.luo.type.envent.BaseEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ActivityStockSkuZeroConsumer {

    @Value("${spring.rabbitmq.topic.activity_sku_stock_zero}")
    private String topic ;

    @Autowired
    private IRaffleActivitySkuStockService skuStock ;


    @RabbitListener(queuesToDeclare = @Queue(value = "activity_sku_stock_zero"))
    public void listener(String message) {

        try {
            log.info("监听sku消息 库存消耗为0 topic:{} message:{}", topic, message);
            BaseEvent.EventMessage<Integer> eventMessage = JSON.parseObject(message, new TypeReference<BaseEvent.EventMessage<Integer>>() {
            }.getType());

            Integer sku = eventMessage.getData();
            //清空库存
            skuStock.clearActivitySkuStock(sku);

            //清空队列
            skuStock.clearQueue();
        } catch (Exception e) {
            log.error("监听sku消息 库存消耗为0 error:{}",e.getMessage());
            throw new RuntimeException(e);
        }

    }







}
