package com.luo.domain.activity.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SkuRechargeEntity {


    private String userId;

    private Integer sku;

    /**
     * 业务单号
     */
    private String outBusinessNo;

}
