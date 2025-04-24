package com.luo.infrastructure.pojo;

import lombok.Data;

import java.util.Date;

@Data
public class RaffleActivityAccount {
    private Long id;
    private String userId;
    private Integer activityId;
    private Integer totalCount;
    private Integer totalCountSurplus;
    private Integer dayCount;
    private Integer dayCountSurplus; // 注意 SQL 字段名 day_count_surplus 可能存在拼写错误
    private Integer monthCount;
    private Integer monthCountSurplus;
    private Date createTime;
    private Date updateTime;
}
