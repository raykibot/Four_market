package com.luo.infrastructure.pojo;

import lombok.Data;

import java.util.Date;

@Data
public class RaffleActivityOrder {

    private Long    id;
    private String userId;
    private Integer activityId;
    private String  activityName;
    private Long strategyId;
    private Integer totalCount;
    private Integer dayCount;
    private Integer monthCount;
    private Date    orderTime;
    private String  state;
    private Date createTime;
    private Date updateTime;
}
