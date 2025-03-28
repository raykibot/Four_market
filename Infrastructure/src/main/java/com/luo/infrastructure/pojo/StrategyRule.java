package com.luo.infrastructure.pojo;

import lombok.Data;

import java.util.Date;

@Data
public class StrategyRule {


    private Long strategyId;

    private Integer awardId;

    private String ruleModel;

    private String ruleValue;

    private String ruleDesc;

    private Date createTime;

    private Date updateTime;
}
