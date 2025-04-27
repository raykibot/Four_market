package com.luo.domain.activity.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ActivityAccountDayEntity {

    private String userId;

    private Integer activityId;

    private String day;

    private Integer dayCount;

    private Integer dayCountSurplus;

}
