package com.luo.domain.activity.model.entity;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class ActivityOrderEntity {

    private String userId;
    private Integer sku;
    private Integer activityId;
    private String activityName;
    private Integer strategyId;
    private String orderId;
    private Date orderTime;
    private Integer totalCount;
    private Integer dayCount;
    private Integer monthCount;
    private Integer state;
    private Date createTime;
}
