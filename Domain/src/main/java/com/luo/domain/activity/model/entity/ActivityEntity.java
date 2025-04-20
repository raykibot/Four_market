package com.luo.domain.activity.model.entity;

import com.luo.domain.activity.model.vo.ActivityStateVO;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class ActivityEntity {

    private Long id;
    private Integer activityId;
    private String activityName;
    private String activityDesc;
    private Date beginDateTime;
    private Date endDateTime;
    private Integer activityCountId;
    private Long strategyId;
    private ActivityStateVO state;
}
