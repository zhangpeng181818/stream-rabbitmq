package com.nuc.zp.service.mq;

public interface IMessageConsumer {

    void consume(String topic, byte[] message);

}
