package com.nuc.zp.service.mq;

import io.netty.handler.codec.mqtt.MqttQoS;

public class MqttMessage implements IMessage {
    private MqttTopic topic;
    private byte[] content;
    private MqttQoS qos;
    private boolean retained;

    private MqttMessage(){
    }

    @Override
    public Topic getTopic() {
        return topic;
    }

    @Override
    public byte[] getContent() {
        return content;
    }

    public MqttQoS getQos() {
        return qos;
    }

    public boolean isRetained() {
        return retained;
    }

    public static MqttMessage build(MqttTopic topic, byte[] content) {
        return build(topic, content, null, false);
    }

    public static MqttMessage build(MqttTopic topic, byte[] content, MqttQoS qos) {
        return build(topic, content, qos,false);
    }

    public static MqttMessage build(MqttTopic topic, byte[] content, MqttQoS qos, boolean retained) {
        MqttMessage msg = new MqttMessage();
        msg.topic = topic;
        msg.content = content;
        msg.qos = qos == null ? MqttQoS.AT_MOST_ONCE : qos;
        msg.retained = retained;
        return msg;
    }


}
