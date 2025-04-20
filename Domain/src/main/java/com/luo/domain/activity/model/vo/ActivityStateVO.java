package com.luo.domain.activity.model.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ActivityStateVO {


    create("create","创建"),
    open("open","开启"),
    close("close","关闭");


    private final String code;

    private final String desc;

}
