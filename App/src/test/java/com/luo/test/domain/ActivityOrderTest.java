package com.luo.test.domain;

import com.alibaba.fastjson.JSON;
import com.luo.domain.activity.model.entity.ActivityOrderEntity;
import com.luo.domain.activity.model.entity.ActivityShopCarEntity;
import com.luo.domain.activity.service.IRaffleOrder;
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
    private IRaffleOrder raffleOrder;

    @Test
    public void test_order(){

        ActivityShopCarEntity activityShopCarEntity = new ActivityShopCarEntity();
        activityShopCarEntity.setSku(9011);
        activityShopCarEntity.setUserId("luojiakeng");

        ActivityOrderEntity raffleActivityOrder = raffleOrder.createRaffleActivityOrder(activityShopCarEntity);

        log.info("订单信息:{}", JSON.toJSONString(raffleActivityOrder));
    }

}
