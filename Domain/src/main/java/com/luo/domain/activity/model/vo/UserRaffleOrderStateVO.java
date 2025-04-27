package com.luo.domain.activity.model.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum UserRaffleOrderStateVO {

    create("create","已创建"),
    used("used","已使用"),
    cancel("cancel","已作废"),;


    private final String code;

    private final String desc;

}
