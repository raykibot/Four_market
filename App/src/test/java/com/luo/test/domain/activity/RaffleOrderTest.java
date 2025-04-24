package com.luo.test.domain.activity;

import com.luo.domain.activity.model.entity.SkuRechargeEntity;
import com.luo.domain.activity.service.IRaffleOrder;
import com.luo.domain.activity.service.armory.IActivityArmory;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.CountDownLatch;

@SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j
public class RaffleOrderTest {

    @Autowired
    private IRaffleOrder raffleOrder;

    @Autowired
    private IActivityArmory activityArmory;


    @Before
    public void setup(){

        boolean status = activityArmory.assembleActivitySku(9011);
        log.info("装配结果:{}", status);
    }


    @Test
    public void test_createSkuRechargeOrder_duplicate() {


        SkuRechargeEntity skuRechargeEntity = new SkuRechargeEntity();
        skuRechargeEntity.setSku(9011);
        skuRechargeEntity.setUserId("luojiakeng");
        skuRechargeEntity.setOutBusinessNo("700091009111");

        String orderId = raffleOrder.createSkuRechargeOrder(skuRechargeEntity);

        log.info("orderId:{}", orderId);
    }


    @Test
    public void test_createSkuRechargeOrder() throws InterruptedException {

        for (int i = 0; i < 20; i++) {
            try {
                SkuRechargeEntity skuRechargeEntity = new SkuRechargeEntity();
                skuRechargeEntity.setSku(9011);
                skuRechargeEntity.setUserId("luojiakeng");
                skuRechargeEntity.setOutBusinessNo(RandomStringUtils.randomNumeric(12));

                String orderId = raffleOrder.createSkuRechargeOrder(skuRechargeEntity);
                log.info("订单id:{}", orderId);
            } catch (Exception e) {
                log.warn(e.getMessage());
            }
        }
        new CountDownLatch(1).await();
    }
}
