package com.luo.infrastructure.pojo;

import com.luo.domain.activity.model.vo.UserRaffleOrderStateVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRaffleOrder {

    private Integer id;

    private String userId;

    private Integer activityId;

    private String activityName;

    private Long strategyId;

    private String orderId;

    // 下单时时间
    private Date orderTime;

    // 订单状态
    private String orderState;

    private Date createTime;

    private Date updateTime;

}
