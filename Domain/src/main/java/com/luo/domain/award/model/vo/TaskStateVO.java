package com.luo.domain.award.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
public enum TaskStateVO {


    create("create","创建"),
    completed("completed","发送完成"),
    fail("fail","发送失败");


    private final String code;

    private final String desc;

}
