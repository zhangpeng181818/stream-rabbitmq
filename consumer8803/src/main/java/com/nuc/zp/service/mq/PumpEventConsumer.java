package com.nuc.zp.service.mq;

import org.springframework.stereotype.Component;

@Component
public class PumpEventConsumer implements IMessageConsumer {

    @Override
    public void consume(String topic, byte[] message) {
        try {

            System.out.println(topic + "---" + new String(message, "utf-8"));
        } catch (Exception e) {

        }
    }
}
