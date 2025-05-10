package com.luo.domain.strategy.service.rule.tree.factory.engine;


import com.luo.domain.strategy.service.rule.tree.factory.DefaultTreeFactory;

import java.util.Date;

public interface IDecisionTreeEngine {

    DefaultTreeFactory.StrategyAwardVO process (String userID, Long strategyId, Integer awardId, Date endDateTime);

}
