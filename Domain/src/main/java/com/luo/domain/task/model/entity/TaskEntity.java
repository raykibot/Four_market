package com.luo.domain.task.model.entity;


import com.luo.domain.award.event.SendAwardMessageEvent;
import com.luo.domain.award.model.vo.TaskStateVO;
import com.luo.type.envent.BaseEvent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TaskEntity {


    private String userId;

    private String topic;

    private String messageId;

    private String message;

}
