package com.luo.domain.award.model.entity;

import com.luo.domain.award.model.vo.AwardStateVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserAwardRecordEntity {


    private String userId;

    private Integer activityId;

    private Long strategyId;

    private Integer awardId;

    private String orderId;

    private String awardTitle;

    private Date orderTime;

    private Date awardTime;

    private AwardStateVO awardState;


}
