package com.luo.trigger.http;

import com.luo.domain.activity.model.entity.UserRaffleOrderEntity;
import com.luo.domain.activity.service.IRaffleActivityPartakeService;
import com.luo.domain.activity.service.armory.IActivityArmory;
import com.luo.domain.award.model.entity.UserAwardRecordEntity;
import com.luo.domain.award.model.vo.AwardStateVO;
import com.luo.domain.award.service.IAwardService;
import com.luo.domain.strategy.model.entity.RaffleAwardEntity;
import com.luo.domain.strategy.model.entity.RaffleFactorEntity;
import com.luo.domain.strategy.service.armory.IAssembleArmory;
import com.luo.domain.strategy.service.rule.IRaffleStrategy;
import com.luo.trigger.api.IRaffleActivityService;
import com.luo.trigger.api.dto.ActivityDrawRequestDTO;
import com.luo.trigger.api.dto.ActivityDrawResponseDTO;
import com.luo.type.enums.ResponseCode;
import com.luo.type.model.Response;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@Slf4j
@CrossOrigin("${app.config.cross-origin}")
@RestController
@RequestMapping("/api/${app.config.api-version}/raffle/activity/")
public class RaffleActivityController implements IRaffleActivityService {



    @Autowired
    private IActivityArmory activityArmory;

    @Autowired
    private IAssembleArmory strategyArmory;

    @Autowired
    private IAwardService awardService;

    @Autowired
    private IRaffleActivityPartakeService raffleActivityPartakeService;

    @Autowired
    private IRaffleStrategy raffleStrategy;




    @RequestMapping(value = "/armory", method = RequestMethod.GET)
    @Override
    public Response<Boolean> armory(@RequestParam Integer activityId) {
        try{

            //活动装配
            activityArmory.assembleActivitySkuByActivityId(activityId);

            //策略装配
            strategyArmory.assembleRaffleStrategyByActivityId(activityId);

            log.info("活动装配，数据预热，完成 activityId:{}", activityId);

            return Response.<Boolean>builder()
                    .code(ResponseCode.SUCCESS.getCode())
                    .msg(ResponseCode.SUCCESS.getInfo())
                    .data(true)
                    .build();

        } catch (Exception e){
            log.info("活动装配，数据预热，失败 activityId:{}", activityId);
            return Response.<Boolean>builder()
                    .code(ResponseCode.FAIL.getCode())
                    .msg(ResponseCode.FAIL.getInfo())
                    .build();
        }
    }

    @RequestMapping(value = "/draw", method = RequestMethod.POST)
    @Override
    public Response<ActivityDrawResponseDTO> draw(@RequestBody ActivityDrawRequestDTO requestDTO) {

        try {
            // 1. 参数校验
            if (StringUtils.isBlank(requestDTO.getUserId()) || requestDTO.getActivityId() == null){
               throw new RuntimeException("传入参数为空");
            }

            // 2. 参与活动 - 创建产于活动订单
            UserRaffleOrderEntity userRaffleOrder = raffleActivityPartakeService.createOrder(requestDTO.getUserId(), requestDTO.getActivityId());

            // 3. 抽奖策略 执行抽奖
            RaffleAwardEntity raffleAwardEntity = raffleStrategy.performRaffle(RaffleFactorEntity.builder()
                    .userId(userRaffleOrder.getUserId())
                    .strategyId(userRaffleOrder.getStrategyId())
                    .build());

            // 4. 存放结果 写入中奖记录
            UserAwardRecordEntity userAwardRecordEntity = UserAwardRecordEntity.builder()
                    .userId(userRaffleOrder.getUserId())
                    .activityId(userRaffleOrder.getActivityId())
                    .strategyId(userRaffleOrder.getStrategyId())
                    .awardId(raffleAwardEntity.getAwardId())
                    .orderId(userRaffleOrder.getOrderId())
                    .awardTitle(raffleAwardEntity.getAwardTitle())
                    .orderTime(userRaffleOrder.getOrderTime())
                    .awardState(AwardStateVO.create)
                    .awardTime(new Date())
                    .build();
            awardService.saveUserAwardRecord(userAwardRecordEntity);

            // 5. 返回结果
            return Response.<ActivityDrawResponseDTO>builder()
                    .code(ResponseCode.SUCCESS.getCode())
                    .msg(ResponseCode.SUCCESS.getInfo())
                    .data(ActivityDrawResponseDTO.builder()
                            .awardId(raffleAwardEntity.getAwardId())
                            .awardTitle(raffleAwardEntity.getAwardTitle())
                            .awardIndex(raffleAwardEntity.getSort())
                            .build())
                    .build();
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }
}
