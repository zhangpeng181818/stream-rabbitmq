package com.nuc.zp.mq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Component
@Slf4j
public class MsgConsumer {

    @RabbitListener(
            bindings =
                    {@QueueBinding(value = @Queue(value = RabbitConfig.PUMP_EVENT_TOPIC_QUEUE_NAME, durable = "true",
                            arguments = @Argument(name = "x-message-ttl", value = "10000", type = "java.lang.Integer")),
                            exchange = @Exchange(value = RabbitConfig.PUMP_EVENT_TOPIC_EXCHANGE, type = "topic"))
                    })
    @RabbitHandler
    public void processFanoutMsg(Message massage) {
        String msg = new String(massage.getBody(), StandardCharsets.UTF_8);
        log.info("received Fanout message : " + msg);
    }
}
