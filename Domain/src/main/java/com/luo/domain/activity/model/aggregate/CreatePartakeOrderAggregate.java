package com.luo.domain.activity.model.aggregate;

import com.luo.domain.activity.model.entity.ActivityAccountDayEntity;
import com.luo.domain.activity.model.entity.ActivityAccountEntity;
import com.luo.domain.activity.model.entity.ActivityAccountMonthEntity;
import com.luo.domain.activity.model.entity.UserRaffleOrderEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreatePartakeOrderAggregate {


    private String userId;

    private Integer activityId;

    private ActivityAccountEntity accountEntity;

    private boolean isExistAccountMonth = true;

    private ActivityAccountMonthEntity accountMonthEntity;

    private boolean isExistAccountDay = true;

    private ActivityAccountDayEntity accountDayEntity;


    private UserRaffleOrderEntity userRaffleOrderEntity;

}
