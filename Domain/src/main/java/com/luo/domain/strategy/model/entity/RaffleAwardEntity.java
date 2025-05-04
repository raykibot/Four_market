package com.luo.domain.strategy.model.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RaffleAwardEntity {


    private Integer awardId;

    private String awardTitle;

    private String awardConfig;

    private Integer sort;


}
