package com.luo.domain.award.repository;

import com.luo.domain.award.model.aggregate.AggregateAwardRecordEntity;

public interface IAwardRepository {
    void saveAggregateAwardRecordEntity(AggregateAwardRecordEntity aggregateAwardRecordEntity);
}
