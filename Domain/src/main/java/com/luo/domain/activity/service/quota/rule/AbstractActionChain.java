package com.luo.domain.activity.service.quota.rule;

public abstract class AbstractActionChain implements IActionChain {

    private IActionChain next;


    @Override
    public IActionChain next() {
        return next;
    }

    @Override
    public IActionChain appendNext(IActionChain actionChain) {
        this.next = actionChain;
        return next;
    }
}
