package com.luo.test.domain.AwardServiceTest;

import com.luo.domain.award.model.entity.UserAwardRecordEntity;
import com.luo.domain.award.model.vo.AwardStateVO;
import com.luo.domain.award.service.IAwardService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

@SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j
public class AwardTest {



    @Autowired
    private IAwardService awardService;



    @Test
    public void test_saveUserAwardRecord() throws InterruptedException {


            UserAwardRecordEntity userAwardRecordEntity = new UserAwardRecordEntity();
            userAwardRecordEntity.setUserId("luojiakeng");
            userAwardRecordEntity.setActivityId(100301);
            userAwardRecordEntity.setStrategyId(100006L);
            userAwardRecordEntity.setAwardId(101);
            userAwardRecordEntity.setOrderId(RandomStringUtils.randomNumeric(12));
            userAwardRecordEntity.setAwardTitle("用户随机积分");
            userAwardRecordEntity.setAwardState(AwardStateVO.create);
            userAwardRecordEntity.setOrderTime(new Date());

            awardService.saveUserAwardRecord(userAwardRecordEntity);

            Thread.sleep(500);

    }

}
