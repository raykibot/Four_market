package com.luo.test.controller;

import com.luo.domain.strategy.service.armory.IAssembleArmory;
import com.luo.trigger.api.IRaffleActivityService;
import com.luo.trigger.api.dto.ActivityDrawRequestDTO;
import com.luo.trigger.api.dto.ActivityDrawResponseDTO;
import com.luo.type.model.Response;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j
public class RaffleActivityTest {


    @Autowired
    private IRaffleActivityService raffleActivityService;



    @Test
    public void test_armory(){

        Response<Boolean> armory = raffleActivityService.armory(100301);
        log.info("测试结果:{}",armory);
    }


    @Test
    public void test_draw(){

        ActivityDrawRequestDTO activityDrawRequestDTO = new ActivityDrawRequestDTO("luojiakeng", 100301);
        Response<ActivityDrawResponseDTO> orderEntity = raffleActivityService.draw(activityDrawRequestDTO);

        log.info("测试结果:{}",orderEntity);

    }




}
