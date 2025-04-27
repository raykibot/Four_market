package com.luo.test.domain.activity;

import com.alibaba.fastjson2.JSON;
import com.luo.domain.activity.model.entity.PartakeRaffleActivityEntity;
import com.luo.domain.activity.model.entity.UserRaffleOrderEntity;
import com.luo.domain.activity.service.IRaffleActivityPartakeService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j
public class RaffleActivityPartakeTest {


    @Autowired
    private IRaffleActivityPartakeService raffleActivityPartakeService;


    @Test
    public void test_createOrder(){

        PartakeRaffleActivityEntity partakeRaffleActivity = PartakeRaffleActivityEntity.builder()
                .userId("luojiakeng")
                .activityId(100301)
                .build();

        UserRaffleOrderEntity order = raffleActivityPartakeService.createOrder(partakeRaffleActivity);
        log.info("订单信息：{}", JSON.toJSONString(order));

    }








}
