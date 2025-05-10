package com.luo.test.domain;

import com.luo.domain.strategy.model.entity.RaffleAwardEntity;
import com.luo.domain.strategy.model.entity.RaffleFactorEntity;
import com.luo.domain.strategy.service.armory.IAssembleArmory;
import com.luo.domain.strategy.service.armory.IRaffleDispatch;
import com.luo.domain.strategy.service.rule.IRaffleStrategy;
import com.luo.domain.strategy.service.rule.chain.impl.RuleWeightLogicChain;
import com.luo.domain.strategy.service.rule.tree.impl.RuleLockTreeNode;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;

import javax.annotation.Resource;

@SpringBootTest
@Slf4j
@RunWith(SpringRunner.class)
public class PerformanceTest {


    @Autowired
    private IAssembleArmory assembleArmory;

    @Resource
    private IRaffleStrategy raffleStrategy;

    @Autowired
    private RuleWeightLogicChain ruleWeightLogicChain;

    @Autowired
    private RuleLockTreeNode ruleLockTreeNode;


    @Before
    public void assemble() {

        //默认装配策略
//        log.info("100001装配结果：{}", assembleArmory.assembleRaffleStrategy(100001L));
        //次数锁装配策略
        log.info("100002装配结果：{}", assembleArmory.assembleRaffleStrategy(100002L));
    }

//    @Test
//    public void performanceTest() {
////        ReflectionTestUtils.setField(ruleWeightLogicChain,"userScore",3000L);
////        ReflectionTestUtils.setField(ruleLockTreeNode, "userRaffleCount", 1L);
//        RaffleFactorEntity raffleFactorEntity = new RaffleFactorEntity("user", 100002L);
//        RaffleAwardEntity raffleAwardEntity = raffleStrategy.performRaffle(raffleFactorEntity);
//        log.info("抽奖结果 awardId:{} awardConfig:{} sort:{}", raffleAwardEntity.getAwardId(), raffleAwardEntity.getAwardConfig(), raffleAwardEntity.getSort());
//
////        for (int i = 0; i < 20; i++) {
////            RaffleAwardEntity raffleAwardEntity = raffleStrategy.performRaffle(raffleFactorEntity);
////            log.info("抽奖结果 awardId:{} awardConfig:{} sort:{}", raffleAwardEntity.getAwardId(), raffleAwardEntity.getAwardConfig(), raffleAwardEntity.getSort());
////        }
//    }


}
