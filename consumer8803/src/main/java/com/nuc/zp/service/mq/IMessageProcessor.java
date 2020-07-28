package com.nuc.zp.service.mq;

public interface IMessageProcessor extends IMessagePublisher {
    void init(MqConfig config);

    MqConfig getMqConfig();

    void connectToBroker();

    boolean isConnected();

    void disconnect();


}
