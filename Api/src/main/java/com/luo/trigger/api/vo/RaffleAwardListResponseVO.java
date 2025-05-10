package com.luo.trigger.api.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RaffleAwardListResponseVO {


    //奖品ID
    private Integer awardId;

    //奖品标题
    private String awardTitle;

    //奖品副标题 【还有多少次解锁】
    private String awardSubTitle;

    //排序
    private Integer sort;

    //奖品是否解锁
    private Boolean isAwardUnLock;

    //抽奖规则解锁次数 没有则配置为null
    private Integer awardRuleLockCount;

    //等待解锁次数
    private Integer waitUnLockCount;

}
