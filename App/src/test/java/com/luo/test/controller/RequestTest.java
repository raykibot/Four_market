package com.luo.test.controller;

import com.luo.trigger.api.IRaffleStrategyService;
import com.luo.type.model.Response;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j
public class RequestTest {

    @Autowired
    private IRaffleStrategyService raffleService;

    @Before
    public void assemble(){

        log.info("开始组装抽奖策略");
        Response<Boolean> status = raffleService.strategyArmory(100001L);

    }





}
