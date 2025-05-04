package com.luo.trigger.api;


import com.luo.trigger.api.dto.ActivityDrawRequestDTO;
import com.luo.trigger.api.dto.ActivityDrawResponseDTO;
import com.luo.type.model.Response;

public interface IRaffleActivityService {


    /**
     * 活动装配  数据预热缓存
     * @param activityId 活动id
     * @return 是否成功
     */
    Response<Boolean> armory(Integer activityId);


    /**
     * 活动抽奖
     * @param requestDTO 请求对象
     * @return 返回奖品信息
     */
    Response<ActivityDrawResponseDTO> draw(ActivityDrawRequestDTO requestDTO);

}
