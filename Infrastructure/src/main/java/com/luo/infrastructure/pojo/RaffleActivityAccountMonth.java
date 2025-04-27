package com.luo.infrastructure.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RaffleActivityAccountMonth {



    private Integer id;

    private String userId;

    private Integer activityId;

    private String month;

    private Integer monthCount;

    private Integer monthCountSurplus;

    private Date createTime;

    private Date updateTime;


}
