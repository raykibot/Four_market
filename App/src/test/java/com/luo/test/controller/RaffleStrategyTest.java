package com.luo.test.controller;

import com.alibaba.fastjson.JSON;
import com.luo.trigger.api.IRaffleStrategyService;
import com.luo.trigger.api.dto.RaffleAwardListRequestDTO;
import com.luo.trigger.api.vo.RaffleAwardListResponseVO;
import com.luo.type.model.Response;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j
public class RaffleStrategyTest {

    @Autowired
    private IRaffleStrategyService raffleStrategyService;




    @Test
    public void test_queryStrategyAward() {

        RaffleAwardListRequestDTO requestDTO = RaffleAwardListRequestDTO.builder()
                .userId("luojiakeng")
                .activityId(100301)
                .build();
        Response<List<RaffleAwardListResponseVO>> listResponse = raffleStrategyService.queryRaffleAwardList(requestDTO);
        log.info("查询奖品列表:{}", JSON.toJSONString(listResponse));
    }



}
