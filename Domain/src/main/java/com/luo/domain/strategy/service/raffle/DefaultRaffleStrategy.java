package com.luo.domain.strategy.service.raffle;

import com.luo.domain.strategy.model.entity.StrategyAwardEntity;
import com.luo.domain.strategy.model.entity.StrategyAwardRuleModelVO;
import com.luo.domain.strategy.model.vo.RuleTreeVO;
import com.luo.domain.strategy.model.vo.StrategyAwardStockVO;
import com.luo.domain.strategy.repository.IStrategyRepository;
import com.luo.domain.strategy.service.armory.IRaffleDispatch;
import com.luo.domain.strategy.service.rule.AbstractRaffleStrategy;
import com.luo.domain.strategy.service.rule.IRaffleAward;
import com.luo.domain.strategy.service.rule.IRaffleStock;
import com.luo.domain.strategy.service.rule.chain.ILogicChain;
import com.luo.domain.strategy.service.rule.chain.factory.DefaultChainFactory;
import com.luo.domain.strategy.service.rule.tree.factory.DefaultTreeFactory;
import com.luo.domain.strategy.service.rule.tree.factory.engine.IDecisionTreeEngine;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DefaultRaffleStrategy extends AbstractRaffleStrategy implements IRaffleStock, IRaffleAward {


    public DefaultRaffleStrategy(IStrategyRepository strategyRepository, IRaffleDispatch raffleDispatch, DefaultChainFactory defaultChainFactory, DefaultTreeFactory defaultTreeFactory) {
        super(strategyRepository, raffleDispatch, defaultChainFactory, defaultTreeFactory);
    }

    /**
     * 责任链具体实现
     * @param userId
     * @param strategyId
     * @return StrategyAwardVO : awardId ruleModel
     */
    @Override
    public DefaultChainFactory.StrategyAwardVO raffleLogicChain(String userId, Long strategyId) {

        //工厂根据strategyId获取责任链
        ILogicChain iLogicChain = defaultChainFactory.openChain(strategyId);

        //责任链过滤结果 awardId ruleModel
        return iLogicChain.logic(userId, strategyId);

    }

    @Override
    public DefaultTreeFactory.StrategyAwardVO raffleLogicTree(String userId, Long strategyId, Integer awardId, Date endDateTime) {

        //查询奖品ruleModel
        StrategyAwardRuleModelVO strategyAwardRuleModelVO = strategyRepository.queryStrategyAwardRuleModel(strategyId, awardId);
        if (strategyAwardRuleModelVO == null){
            return DefaultTreeFactory.StrategyAwardVO.builder().awardId(awardId).build();
        }
        RuleTreeVO ruleTreeVO = strategyRepository.queryRuleTreeVOByTreeId(strategyAwardRuleModelVO.getRuleModel());
        if (null == ruleTreeVO){
            throw new RuntimeException("rule_tree rule_tree_node rule_tree_node_line 配置错误");
        }
        IDecisionTreeEngine decisionTreeEngine = defaultTreeFactory.openLogicTree(ruleTreeVO);
        return decisionTreeEngine.process(userId,strategyId,awardId,endDateTime);
    }

    @Override
    public StrategyAwardStockVO takeQueueValue() {
        return strategyRepository.takeQueueValue();
    }

    @Override
    public void updateStrategyAwardStock(Long strategyId, Integer awardId) {
        strategyRepository.updateStrategyAwardStock(strategyId,awardId);
    }

    @Override
    public List<StrategyAwardEntity> queryRaffleStaregyAwardList(Long strategyId) {
        List<StrategyAwardEntity> strategyAwardEntities = strategyRepository.queryStrategyAwardList(strategyId);
        return strategyAwardEntities;
    }

    @Override
    public List<StrategyAwardEntity> queryRaffleStrategyAwardListByActivityId(Integer activityId) {
        Long strategyIdByActivityId = strategyRepository.queryStrategyIdByActivityId(activityId);
        return strategyRepository.queryStrategyAwardList(strategyIdByActivityId);
    }

    @Override
    public Map<String, Integer> queryAwardRuleLockCount(String[] treeIds) {
        return strategyRepository.queryAwardRuleLockCount(treeIds);
    }
}
