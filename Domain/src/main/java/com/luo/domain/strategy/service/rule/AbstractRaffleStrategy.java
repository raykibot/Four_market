package com.luo.domain.strategy.service.rule;

import com.luo.domain.strategy.model.entity.RaffleAwardEntity;
import com.luo.domain.strategy.model.entity.RaffleFactorEntity;
import com.luo.domain.strategy.model.entity.StrategyAwardEntity;
import com.luo.domain.strategy.repository.IStrategyRepository;
import com.luo.domain.strategy.service.armory.IRaffleDispatch;
import com.luo.domain.strategy.service.rule.chain.ILogicChain;
import com.luo.domain.strategy.service.rule.chain.factory.DefaultChainFactory;
import com.luo.domain.strategy.service.rule.tree.factory.DefaultTreeFactory;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;


public abstract class AbstractRaffleStrategy implements IRaffleStrategy{


    protected IStrategyRepository strategyRepository;

    protected IRaffleDispatch raffleDispatch;

    protected DefaultChainFactory defaultChainFactory;

    protected DefaultTreeFactory defaultTreeFactory;

    public AbstractRaffleStrategy(IStrategyRepository strategyRepository, IRaffleDispatch raffleDispatch, DefaultChainFactory defaultChainFactory, DefaultTreeFactory defaultTreeFactory) {
        this.strategyRepository = strategyRepository;
        this.raffleDispatch = raffleDispatch;
        this.defaultChainFactory = defaultChainFactory;
        this.defaultTreeFactory = defaultTreeFactory;
    }

    @Override
    public RaffleAwardEntity performRaffle(RaffleFactorEntity raffleFactorEntity) {

        //1. 参数校验
        String userId = raffleFactorEntity.getUserId();
        Long strategyId = raffleFactorEntity.getStrategyId();
        if ( StringUtils.isBlank(userId) || null == strategyId ){
        throw new RuntimeException("传参错误");
        }

        //2. 责任链抽奖计算
        DefaultChainFactory.StrategyAwardVO chainAwardVO = raffleLogicChain(userId, strategyId);
        //权重和黑名单直接返回 不用进入规则树  默认抽奖需要进入后续流程
        if (!chainAwardVO.getRuleModel().equals(DefaultChainFactory.LogicModel.DEFAULT.getCode())){
            return buildRaffleAwardEntity(chainAwardVO.getAwardId(), strategyId, null);
        }

        //3. 规则树抽奖计算
        DefaultTreeFactory.StrategyAwardVO treeAwardVO = raffleLogicTree(userId, strategyId, chainAwardVO.getAwardId());

        return buildRaffleAwardEntity(treeAwardVO.getAwardId(),strategyId, treeAwardVO.getAwardRuleValue());
    }

    private RaffleAwardEntity buildRaffleAwardEntity(Integer awardId, Long strategyId, String awardConfig) {
        StrategyAwardEntity strategyAwardEntity = strategyRepository.queryStrategyAwardEntity(awardId,strategyId);
        return RaffleAwardEntity.builder()
                .awardId(strategyAwardEntity.getAwardId())
                .awardConfig(awardConfig)
                .awardTitle(strategyAwardEntity.getAwardTitle())
                .sort(strategyAwardEntity.getSort())
                .build();
    }

    public abstract DefaultChainFactory.StrategyAwardVO raffleLogicChain(String userId, Long strategyId);

    public abstract DefaultTreeFactory.StrategyAwardVO raffleLogicTree(String userId, Long strategyId, Integer awardId);
}
