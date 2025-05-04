package com.luo.trigger.api;

import com.luo.trigger.api.dto.RaffleAwardListRequestDTO;
import com.luo.trigger.api.dto.RaffleRequestDTO;
import com.luo.trigger.api.vo.RaffleAwardListResponseVO;
import com.luo.trigger.api.vo.RaffleResponseVO;
import com.luo.type.model.Response;

import java.util.List;

public interface IRaffleStrategyService {


    /**
     * 策略装配接口
     * @param strategyId 策略id
     * @return boolean true:成功 false:失败
     */
     Response<Boolean> strategyArmory(Long strategyId);


    /**
     * 查询抽奖奖品列表接口
     * @param requestDTO 查询奖品列表策略id
     * @return
     */
     Response<List<RaffleAwardListResponseVO>> queryRaffleAwardList(RaffleAwardListRequestDTO requestDTO);


    /**
     * 随机抽奖接口
     * @param requestDTO 抽奖请求策略id
     * @return 返回抽奖结果 awardId 奖品ID awardIndex奖品索引
     */
     Response<RaffleResponseVO> randomRaffle(RaffleRequestDTO requestDTO);

}
