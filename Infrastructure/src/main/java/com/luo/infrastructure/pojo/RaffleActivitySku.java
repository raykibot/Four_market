package com.luo.infrastructure.pojo;

import lombok.Data;

import java.util.Date;

@Data
public class RaffleActivitySku {


    private Long id;

    private Integer sku;

    private Integer activityId;

    private Integer activityCountId;

    private Integer stockCount;

    private Integer stockCountSurplus;

    private Date createTime;

    private Date updateTime;

}
