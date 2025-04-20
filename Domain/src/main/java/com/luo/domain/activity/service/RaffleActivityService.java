package com.luo.domain.activity.service;

import com.luo.domain.activity.repository.IActivityRepository;
import org.springframework.stereotype.Service;

@Service
public class RaffleActivityService extends AbstractRaffleActivity {


    public RaffleActivityService(IActivityRepository activityRepository) {
        super(activityRepository);
    }
}
