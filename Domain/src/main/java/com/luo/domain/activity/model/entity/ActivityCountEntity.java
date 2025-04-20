package com.luo.domain.activity.model.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ActivityCountEntity {

    /**
     * 活动次数编号
     */
    private Integer activityCountId;

    /**
     * 总次数
     */
    private Integer totalCount;

    /**
     * 日次数
     */
    private Integer dayCount;

    /**
     * 月次数
     */
    private Integer monthCount;

}
