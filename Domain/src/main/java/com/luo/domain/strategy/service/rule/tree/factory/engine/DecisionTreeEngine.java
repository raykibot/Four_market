package com.luo.domain.strategy.service.rule.tree.factory.engine;


import com.luo.domain.strategy.model.vo.*;
import com.luo.domain.strategy.service.rule.tree.ILogicTreeNode;
import com.luo.domain.strategy.service.rule.tree.factory.DefaultTreeFactory;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.luo.domain.strategy.model.vo.RuleLimitTypeVO.*;


public class DecisionTreeEngine implements IDecisionTreeEngine {

    private final Map<String, ILogicTreeNode> treeNodesGroups;

    private final RuleTreeVO ruleTreeVO;

    public DecisionTreeEngine(Map<String, ILogicTreeNode> treeNodesGroups, RuleTreeVO ruleTreeVO) {
        this.treeNodesGroups = treeNodesGroups;
        this.ruleTreeVO = ruleTreeVO;
    }

    @Override
    public DefaultTreeFactory.StrategyAwardVO process(String userID, Long strategyId, Integer awardId, Date endDateTime) {

        DefaultTreeFactory.StrategyAwardVO strategyAwardVO = null;

        //获取规则树根节点 通常为 rule_lock
        String nextNode = ruleTreeVO.getTreeRootRuleNode();

        Map<String, RuleTreeNodeVO> treeMap = ruleTreeVO.getTreeNodeMap();
        //获取起始节点
        RuleTreeNodeVO ruleTreeNodeVO = treeMap.get(nextNode);

        //只要还有下一个节点 就一直执行下去
        while (nextNode != null) {
            //获取决策节点
            ILogicTreeNode iLogicTreeNode = treeNodesGroups.get(ruleTreeNodeVO.getRuleKey());
            String ruleValue = ruleTreeNodeVO.getRuleValue();


            DefaultTreeFactory.TreeActionEntity treeActionEntity = iLogicTreeNode.logic(userID, strategyId, awardId, ruleValue, endDateTime);

            //获取过滤后的返回信息
            RuleLogicCheckTypeVO ruleLogicCheckTypeVO = treeActionEntity.getRuleLogicCheckTypeVO();

            //填充返回信息
            strategyAwardVO = treeActionEntity.getStrategyAwardVO();

            //获取下一个节点
            nextNode = nextNode(ruleLogicCheckTypeVO.getCode(), ruleTreeNodeVO.getRuleTreeNodeLineVOList());
            ruleTreeNodeVO = treeMap.get(nextNode);
        }
        return strategyAwardVO;
    }

    public String nextNode(String code, List<RuleTreeNodeLineVO> ruleTreeNodeLineVOList) {
        if (null == ruleTreeNodeLineVOList || ruleTreeNodeLineVOList.isEmpty()) return null;

        for (RuleTreeNodeLineVO ruleTreeNodeLineVO : ruleTreeNodeLineVOList) {
            if (decisionLogic(code, ruleTreeNodeLineVO)) {
                return ruleTreeNodeLineVO.getRuleNodeTo();
            }
        }
        return null;
    }

    public boolean decisionLogic(String code, RuleTreeNodeLineVO ruleTreeNodeLineVO) {
        switch (ruleTreeNodeLineVO.getRuleLimitType()) {
            case EQUAL:
                return code.equals(ruleTreeNodeLineVO.getRuleLogicCheckTypeVO().getCode());
            case GT:
            case LT:
            default:
                return false;
        }
    }
}
