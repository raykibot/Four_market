package com.luo.domain.activity.model.entity;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class ActivitySkuEntity {


    private Integer sku;
    private Integer activityId;
    private Integer activityCountId;
    private Integer stockCount;
    private Integer stockCountSurplus;

}
