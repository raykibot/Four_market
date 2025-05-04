package com.luo.trigger.http;


import com.alibaba.fastjson.JSON;
import com.luo.domain.strategy.model.entity.RaffleAwardEntity;
import com.luo.domain.strategy.model.entity.RaffleFactorEntity;
import com.luo.domain.strategy.model.entity.StrategyAwardEntity;
import com.luo.domain.strategy.service.armory.IAssembleArmory;
import com.luo.domain.strategy.service.rule.IRaffleAward;
import com.luo.domain.strategy.service.rule.IRaffleStrategy;
import com.luo.trigger.api.IRaffleStrategyService;
import com.luo.trigger.api.dto.RaffleAwardListRequestDTO;
import com.luo.trigger.api.dto.RaffleRequestDTO;
import com.luo.trigger.api.vo.RaffleAwardListResponseVO;
import com.luo.trigger.api.vo.RaffleResponseVO;
import com.luo.type.enums.ResponseCode;
import com.luo.type.model.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@Slf4j
@CrossOrigin("${app.config.cross-origin}")
@RestController
@RequestMapping("/api/${app.config.api-version}/raffle/")
public class RaffleStrategyController implements IRaffleStrategyService {

    @Autowired
    private IAssembleArmory assembleArmory;

    @Autowired
    private IRaffleAward raffleAward;

    @Autowired
    private IRaffleStrategy raffleStrategy;


    @RequestMapping(value = "/strategy_armory", method = RequestMethod.GET)
    @Override
    public Response<Boolean> strategyArmory(@RequestParam Long strategyId) {

        try {

            log.info("开始抽奖策略装配 策略id:{}", strategyId);
            Boolean status = assembleArmory.assembleRaffleStrategy(strategyId);

            log.info("抽奖策略装配成功 策略id:{}", strategyId);
            return Response.<Boolean>builder()
                    .code(ResponseCode.SUCCESS.getCode())
                    .msg(ResponseCode.SUCCESS.getInfo())
                    .data(status)
                    .build();

        } catch (Exception e) {
            log.error("抽奖策略装配失败 策略id:{}", strategyId);
            return Response.<Boolean>builder()
                    .code(ResponseCode.FAIL.getCode())
                    .msg(ResponseCode.FAIL.getInfo())
                    .build();
        }
    }

    @RequestMapping(value = "/query_strategy_award_list", method=RequestMethod.POST)
    @Override
    public Response<List<RaffleAwardListResponseVO>> queryRaffleAwardList(@RequestBody RaffleAwardListRequestDTO requestDTO) {
        try {
            log.info("开始查询奖品列表 策略id:{}", requestDTO.getStrategyId());
            List<StrategyAwardEntity> strategyAwardEntities = raffleAward.queryRaffleStaregyAwardList(requestDTO.getStrategyId());
            List<RaffleAwardListResponseVO> raffleAwardResponseVOList = new ArrayList<>(strategyAwardEntities.size());

            for (StrategyAwardEntity strategyAwardEntity : strategyAwardEntities) {
                RaffleAwardListResponseVO responseVO = RaffleAwardListResponseVO.builder()
                        .awardId(strategyAwardEntity.getAwardId())
                        .awardTitle(strategyAwardEntity.getAwardTitle())
                        .awardSubTitle(strategyAwardEntity.getAwardSubtitle())
                        .sort(strategyAwardEntity.getSort())
                        .build();

                raffleAwardResponseVOList.add(responseVO);
            }
            log.info("查询奖品列表成功 策略id:{}", requestDTO.getStrategyId());
            return Response.<List<RaffleAwardListResponseVO>>builder()
                    .code(ResponseCode.SUCCESS.getCode())
                    .msg(ResponseCode.SUCCESS.getInfo())
                    .data(raffleAwardResponseVOList)
                    .build();
        } catch (Exception e) {
            log.error("查询奖品列表失败 策略id:{}", requestDTO.getStrategyId());
            return Response.<List<RaffleAwardListResponseVO>>builder()
                    .code(ResponseCode.FAIL.getCode())
                    .msg(ResponseCode.FAIL.getInfo())
                    .build();
        }
    }

    @RequestMapping(value = "/random_raffle", method = RequestMethod.POST)
    @Override
    public Response<RaffleResponseVO> randomRaffle(@RequestBody RaffleRequestDTO requestDTO) {

        try {
            RaffleFactorEntity raffleFactorEntity = RaffleFactorEntity.builder()
                    .userId("user")
                    .strategyId(requestDTO.getStrategyId())
                    .build();

            log.info("开始随机抽奖 策略id:{}", requestDTO.getStrategyId());
            RaffleAwardEntity raffleAwardEntity = raffleStrategy.performRaffle(raffleFactorEntity);

            log.info("随机抽奖成功 策略id:{} response:{}", requestDTO.getStrategyId(), JSON.toJSONString(raffleAwardEntity));
            return Response.<RaffleResponseVO>builder()
                    .code(ResponseCode.SUCCESS.getCode())
                    .msg(ResponseCode.SUCCESS.getInfo())
                    .data(RaffleResponseVO.builder()
                            .awardId(raffleAwardEntity.getAwardId())
                            .awardIndex(raffleAwardEntity.getSort())
                            .build())
                    .build();
        } catch (Exception e) {
            log.info("随机抽奖失败 策略id:{} ", requestDTO.getStrategyId());
            return Response.<RaffleResponseVO>builder()
                    .code(ResponseCode.FAIL.getCode())
                    .msg(ResponseCode.FAIL.getInfo())
                    .build();
        }
    }
}
