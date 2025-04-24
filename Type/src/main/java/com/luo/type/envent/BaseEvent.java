package com.luo.type.envent;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
public abstract class BaseEvent <T>{


    public abstract EventMessage<T> buildMessage(T data);

    public abstract String topic();




        @Data
        @AllArgsConstructor
        @NoArgsConstructor
        @Builder
        public static class EventMessage<T>{

        private String id ;

        private Date timeStamp;

        private T data;
        }
}
