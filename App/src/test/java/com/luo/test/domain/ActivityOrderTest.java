package com.luo.test.domain;

import com.luo.domain.activity.service.IRaffleActivityAccountQuotaService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j
public class ActivityOrderTest {


    @Autowired
    private IRaffleActivityAccountQuotaService raffleOrder;

    @Test
    public void test_order(){

//
//        SkuRechargeEntity skuRechargeEntity = new SkuRechargeEntity();
//        skuRechargeEntity.setUserId("luojiakeng");
//        skuRechargeEntity.setSku(9011);
//
//
//        String orderId = raffleOrder.createSkuRechargeOrder(skuRechargeEntity);
//
//        log.info("订单信息:{}", JSON.toJSONString());
    }

}
