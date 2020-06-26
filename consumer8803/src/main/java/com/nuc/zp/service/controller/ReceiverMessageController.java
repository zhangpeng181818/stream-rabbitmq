package com.nuc.zp.service.controller;

import com.nuc.zp.service.CustomInput;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

@Slf4j
@EnableBinding({CustomInput.class})
@Component
public class ReceiverMessageController {

    @Value("${server.port}")
    private Integer port;

    @StreamListener(CustomInput.TESTINPUT)
    public void input2(Message<String> message) {
        log.info("我是消费者2：port={} , {}", port, message.getPayload());

        throw new RuntimeException("失败重试");
    }
}
