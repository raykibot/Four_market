package com.luo.infrastructure.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
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
