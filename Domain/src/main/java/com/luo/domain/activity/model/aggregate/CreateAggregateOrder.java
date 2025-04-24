package com.luo.domain.activity.model.aggregate;

import com.luo.domain.activity.model.entity.ActivityOrderEntity;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
public class CreateAggregateOrder {


    private String userId;

    private Integer activityId;

    private Integer totalCount;

    private Integer dayCount;

    private Integer monthCount;

    private ActivityOrderEntity activityOrderEntity;
}
