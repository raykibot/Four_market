package com.luo.domain.award.service;

import com.luo.domain.award.model.entity.UserAwardRecordEntity;

/**
 * 发奖服务接口
 */
public interface IAwardService {


    void saveUserAwardRecord(UserAwardRecordEntity userAwardRecordEntity);

}
