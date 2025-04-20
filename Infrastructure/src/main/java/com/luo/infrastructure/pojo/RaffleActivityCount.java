package com.luo.infrastructure.pojo;

import lombok.Data;

import java.util.Date;

@Data
public class RaffleActivityCount {


    private Long id;

    private Integer activityCountId;

    private Integer totalCount;

    private Integer dayCount;

    private Integer monthCount;

    private Date createTime;

    private Date updateTime;

}
