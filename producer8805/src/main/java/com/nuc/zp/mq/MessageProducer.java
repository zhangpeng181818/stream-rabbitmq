package com.nuc.zp.mq;

import com.nuc.zp.entity.Notice;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Date;

@EnableScheduling
@Slf4j
@Component
public class MessageProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Scheduled(cron = "0/10 * * * * ?")
    public void schedule() {
        Notice notice = new Notice(11,new Date());
        rabbitTemplate.convertAndSend("test", "icdr.warn.311.11", notice);
        notice.setUserNo(2);
        rabbitTemplate.convertAndSend("test", "icdr.warn.-1", notice);
        log.info("{}", notice);
    }

    @RabbitListener(
            bindings =
                    {@QueueBinding(value = @Queue(value = "test2", durable = "true"),
                            exchange = @Exchange(value = "test", type = "topic")
                            , key = "icdr.warn.311.11")
                    })
    @RabbitHandler
    public void processMsg(Message massage) {
        String msg = new String(massage.getBody(), StandardCharsets.UTF_8);
        log.info("received Fanout message1 : " + msg);
    }

    @RabbitListener(
            bindings =
                    {@QueueBinding(value = @Queue(value = "test4", durable = "true"),
                            exchange = @Exchange(value = "test", type = "topic")
                            , key = "icdr.warn.-1")
                    })
    @RabbitHandler
    public void processMsg2(Message massage) {
        String msg = new String(massage.getBody(), StandardCharsets.UTF_8);
        log.info("received Fanout message2 : " + msg);
    }
}
