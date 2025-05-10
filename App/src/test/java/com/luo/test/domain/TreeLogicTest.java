//package com.luo.test.domain;
//
//import com.luo.domain.strategy.model.vo.*;
//import com.luo.domain.strategy.service.armory.IAssembleArmory;
//import com.luo.domain.strategy.service.rule.chain.factory.DefaultChainFactory;
//import com.luo.domain.strategy.service.rule.tree.factory.DefaultTreeFactory;
//import com.luo.domain.strategy.service.rule.tree.factory.engine.IDecisionTreeEngine;
//import com.luo.domain.strategy.service.rule.tree.impl.RuleLockTreeNode;
//import lombok.extern.slf4j.Slf4j;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.util.ReflectionTestUtils;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//
//@SpringBootTest
//@Slf4j
//public class TreeLogicTest {
//
//
//    @Autowired
//    private IAssembleArmory assembleArmory;
//
//    @Autowired
//    private DefaultTreeFactory defaultTreeFactory;
//
//    @Autowired
//    private RuleLockTreeNode ruleLockTreeNode;;
//
//    @Test
//    public void assembleTest() {
//        log.info("装配结果:{}",assembleArmory.assembleRaffleStrategy(100001L));
//    }
//
//
//    @Test
//    public void treeTest(){
//
//        ReflectionTestUtils.setField(ruleLockTreeNode,"userRaffleCount", 2L);
//
//        // 构建参数
//        RuleTreeNodeVO rule_lock = RuleTreeNodeVO.builder()
//                .treeId("100000001")
//                .ruleKey("rule_lock")
//                .ruleDesc("限定用户已完成N次抽奖后解锁")
//                .ruleValue("2")
//                .ruleTreeNodeLineVOList(new ArrayList<RuleTreeNodeLineVO>() {{
//                    add(RuleTreeNodeLineVO.builder()
//                            .treeId("100000001")
//                            .ruleNodeFrom("rule_lock")
//                            .ruleNodeTo("rule_luck_award")
//                            .ruleLimitType(RuleLimitTypeVO.EQUAL)
//                            .ruleLogicCheckTypeVO(RuleLogicCheckTypeVO.TAKE_OVER)
//                            .build());
//
//                    add(RuleTreeNodeLineVO.builder()
//                            .treeId("100000001")
//                            .ruleNodeFrom("rule_lock")
//                            .ruleNodeTo("rule_stock")
//                            .ruleLimitType(RuleLimitTypeVO.EQUAL)
//                            .ruleLogicCheckTypeVO(RuleLogicCheckTypeVO.ALLOW)
//                            .build());
//                }})
//                .build();
//
//        RuleTreeNodeVO rule_luck_award = RuleTreeNodeVO.builder()
//                .treeId("100000001")
//                .ruleKey("rule_luck_award")
//                .ruleDesc("限定用户已完成N次抽奖后解锁")
//                .ruleValue("101:1,100")
//                .ruleTreeNodeLineVOList(null)
//                .build();
//
//        RuleTreeNodeVO rule_stock = RuleTreeNodeVO.builder()
//                .treeId("100000001")
//                .ruleKey("rule_stock")
//                .ruleDesc("库存处理规则")
//                .ruleValue(null)
//                .ruleTreeNodeLineVOList(new ArrayList<RuleTreeNodeLineVO>() {{
//                    add(RuleTreeNodeLineVO.builder()
//                            .treeId("100000001")
//                            .ruleNodeFrom("rule_lock")
//                            .ruleNodeTo(null)
//                            .ruleLimitType(RuleLimitTypeVO.EQUAL)
//                            .ruleLogicCheckTypeVO(RuleLogicCheckTypeVO.TAKE_OVER)
//                            .build());
//
//
//                    add(RuleTreeNodeLineVO.builder()
//                            .treeId("100000001")
//                            .ruleNodeFrom("rule_lock")
//                            .ruleNodeTo("rule_luck_award")
//                            .ruleLimitType(RuleLimitTypeVO.EQUAL)
//                            .ruleLogicCheckTypeVO(RuleLogicCheckTypeVO.ALLOW)
//                            .build());
//                }})
//                .build();
//
//        RuleTreeVO ruleTreeVO = new RuleTreeVO();
//        ruleTreeVO.setTreeId("100000001");
//        ruleTreeVO.setTreeName("决策树规则；增加dall-e-3画图模型");
//        ruleTreeVO.setTreeDesc("决策树规则；增加dall-e-3画图模型");
//        ruleTreeVO.setTreeRootRuleNode("rule_lock");
//
//        ruleTreeVO.setTreeNodeMap(new HashMap<String, RuleTreeNodeVO>() {{
//            put("rule_lock", rule_lock);
//            put("rule_stock", rule_stock);
//            put("rule_luck_award", rule_luck_award);
//        }});
//
//        IDecisionTreeEngine decisionTreeEngine = defaultTreeFactory.openLogicTree(ruleTreeVO);
//        DefaultTreeFactory.StrategyAwardVO awardVO = decisionTreeEngine.process("user", 100001L,109 );
//        log.info("结果:{}",awardVO.getAwardId());
//
//    }
//
//
//
//
//
//
//
//}
