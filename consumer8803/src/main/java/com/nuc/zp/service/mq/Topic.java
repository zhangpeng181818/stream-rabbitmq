package com.nuc.zp.service.mq;

import lombok.Data;
import lombok.NonNull;

import java.io.Serializable;

/**
 * @author helongyun
 * @date 2020/6/22 23:33
 */
@Data
public abstract class Topic implements Serializable {
    @NonNull
    private String platform;
    private String module;
    private String vendor;
    private String sn;
    private String bedNo;
    private TopicDirection direction;
    private String action;

    public Topic() {
        this.platform = DEFAULT_PLATFORM;
        this.module = MODULE_PUMP;
    }

    public abstract String routeKey();

    enum TopicDirection {
        DOWN("down"),
        UP("up");

        private String value;

        TopicDirection(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public static TopicDirection parse(String value) {
            for(TopicDirection topicDirection : values()) {
                if(topicDirection.getValue().equals(value)) {
                    return topicDirection;
                }
            }
            throw new IllegalArgumentException("Invalid TopicDirection value:" + value);
        }
    }

    static final String DEFAULT_PLATFORM = "icdr";
    static final String MODULE_PUMP = "pump";

}
