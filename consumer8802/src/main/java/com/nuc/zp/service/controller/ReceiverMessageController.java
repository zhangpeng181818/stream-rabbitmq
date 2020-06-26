package com.nuc.zp.service.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

@Slf4j
@EnableBinding({Sink.class})
@Component
public class ReceiverMessageController {

    @Value("${server.port}")
    private Integer port;


    @StreamListener(Sink.INPUT)
    public void input(Message<String> message) {
        log.info("我是消费者1：port={} , {}", port, message.getPayload());
    }


}
