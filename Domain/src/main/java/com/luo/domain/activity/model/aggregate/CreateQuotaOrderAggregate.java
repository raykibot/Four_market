package com.luo.domain.activity.model.aggregate;

import com.luo.domain.activity.model.entity.ActivityOrderEntity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateQuotaOrderAggregate {


    private String userId;

    private Integer activityId;

    private Integer totalCount;

    private Integer dayCount;

    private Integer monthCount;

    private ActivityOrderEntity activityOrderEntity;
}
