package com.luo.domain.strategy.model.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RuleLimitTypeVO {



    EQUAL(1,"等于"),
    GT(2,"大于"),
    LT(3,"小于"),
    ;



    private final Integer code;

    private final String info;




}
