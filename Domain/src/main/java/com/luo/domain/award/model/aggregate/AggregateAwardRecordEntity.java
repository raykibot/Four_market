package com.luo.domain.award.model.aggregate;

import com.luo.domain.award.model.entity.TaskEntity;
import com.luo.domain.award.model.entity.UserAwardRecordEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AggregateAwardRecordEntity {


    private UserAwardRecordEntity userAwardRecordEntity;

    private TaskEntity taskEntity;



}
