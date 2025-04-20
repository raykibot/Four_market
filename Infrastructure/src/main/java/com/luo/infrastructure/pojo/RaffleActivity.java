package com.luo.infrastructure.pojo;

import lombok.Data;

import java.util.Date;

@Data
public class RaffleActivity {

    private Long id;
    private Integer activityId;
    private String activityName;
    private String activityDesc;
    private Date beginDateTime;
    private Date endDateTime;
    private Long strategyId;
    private String state;
    private Date createTime;
    private Date updateTime;
}
