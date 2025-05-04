package com.luo.domain.award.model.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum AwardStateVO {



    create("create","创建"),
    completed("completed","发奖完成"),
    fail("fail","发送失败");


    private final String code;

    private final String desc;



}

