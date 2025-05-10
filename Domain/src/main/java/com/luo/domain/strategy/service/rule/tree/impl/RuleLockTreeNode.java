package com.luo.domain.strategy.service.rule.tree.impl;

import com.luo.domain.strategy.model.vo.RuleLogicCheckTypeVO;
import com.luo.domain.strategy.repository.IStrategyRepository;
import com.luo.domain.strategy.service.rule.tree.ILogicTreeNode;
import com.luo.domain.strategy.service.rule.tree.factory.DefaultTreeFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;


/**
 * 规则树次数锁节点
 */
@Component("rule_lock")
@Slf4j
public class RuleLockTreeNode implements ILogicTreeNode {

    @Autowired
    private IStrategyRepository strategyRepository;

    //用户抽奖次数
    private Long userRaffleCount = 0L;


    @Override
    public DefaultTreeFactory.TreeActionEntity logic(String userId, Long strategyId, Integer awardId, String ruleValue, Date endDateTime) {

        log.info("规则树校验-次数锁");

        long raffleCount = Long.parseLong(ruleValue);

        //用户抽奖次数达到解锁条件 放行
        if (userRaffleCount >= raffleCount){
            log.info("规则树校验-次数锁放行");
            return DefaultTreeFactory.TreeActionEntity.builder()
                    .ruleLogicCheckTypeVO(RuleLogicCheckTypeVO.ALLOW)
                    .build();
        }

        log.info("规则树校验-次数锁接管");
        //用户未达到抽奖条件 拦截
        return DefaultTreeFactory.TreeActionEntity.builder()
                .ruleLogicCheckTypeVO(RuleLogicCheckTypeVO.TAKE_OVER)
                .build();
    }
}
