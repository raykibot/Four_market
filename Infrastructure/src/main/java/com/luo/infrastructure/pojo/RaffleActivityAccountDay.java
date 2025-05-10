package com.luo.infrastructure.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.text.SimpleDateFormat;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RaffleActivityAccountDay {

    private final SimpleDateFormat dateFormatDay = new SimpleDateFormat("yyyy-MM-dd");

    private Integer id;

    private String userId;

    private Integer activityId;

    private String day;

    private Integer dayCount;

    private Integer dayCountSurplus;

    private Date createTime;

    private Date updateTime;

    public String currentDay(){
        return dateFormatDay.format(new Date());
    }

}
