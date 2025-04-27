package com.luo.domain.activity.service.quota.rule.factory;

import com.luo.domain.activity.service.quota.rule.IActionChain;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class DefaultActionChainFactory {

    private final IActionChain actionChain;

    public DefaultActionChainFactory(Map<String, IActionChain> actionChainMap) {
        actionChain = actionChainMap.get(ActionModel.activity_base_action.code);
        actionChain.appendNext(actionChainMap.get(ActionModel.activity_sku_stock_action.getCode()));
    }


    public IActionChain openChain() {
        return this.actionChain;
    }


    @Getter
    @AllArgsConstructor
    public enum ActionModel {

        activity_base_action("activity_base_action", "活动库存、时间校验"),
        activity_sku_stock_action("activity_sku_stock_action", "活动sku库存校验"),;

        private final String code;

        private final String desc;
        }

}

