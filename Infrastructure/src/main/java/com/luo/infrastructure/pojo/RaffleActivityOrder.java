package com.luo.infrastructure.pojo;

import com.luo.domain.activity.model.vo.OrderStateVO;
import lombok.Data;

import java.util.Date;

@Data
public class RaffleActivityOrder {

    private Long    id;
    private String userId;
    private Integer sku;
    private Integer activityId;
    private String  activityName;
    private Long strategyId;
    private String orderId;
    private Integer totalCount;
    private Integer dayCount;
    private Integer monthCount;
    private Date    orderTime;
    private String state;
    private String outBusinessNo;
    private Date createTime;
    private Date updateTime;
}
