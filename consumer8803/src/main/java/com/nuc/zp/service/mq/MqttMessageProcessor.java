package com.nuc.zp.service.mq;

import com.nuc.zp.service.mq.mqtt.*;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.mqtt.MqttConnectReturnCode;
import io.netty.handler.codec.mqtt.MqttQoS;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.concurrent.Future;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Slf4j
public class MqttMessageProcessor implements IMessageProcessor {
    private MqConfig mqConfig;
    private MqttClient mqttClient;
    private final static Object initLock = new Object();

    @Override
    public void init(@NonNull MqConfig config) {
        if(mqConfig != null) {
            return;
        }
        synchronized (initLock) {
            if(mqConfig != null) return;
            mqConfig = new MqConfig();
            mqConfig.setBroker(config.getBroker());
            mqConfig.registerConsumers(config.getTopicConsumers());
            mqConfig.setUsername(config.getUsername());
            mqConfig.setPassword(config.getPassword());
            MqttClientConfig mqttClientConfig = new MqttClientConfig();
            mqttClientConfig.setUsername(mqConfig.getUsername());
            mqttClientConfig.setPassword(mqConfig.getPassword());
            mqttClient = new MqttClientImpl(mqttClientConfig, new LogMqttHandler());
        }
    }

    @Override
    public MqConfig getMqConfig() {
        return mqConfig;
    }

    @Override
    public void connectToBroker() {
        checkInit();
        MqConfig.Broker broker = mqConfig.getBroker();
        try {
            Future<MqttConnectResult> future = mqttClient.connect(broker.getHost(), broker.getPort());
            MqttConnectResult connectResult = future.get(5, TimeUnit.SECONDS);
            if(!connectResult.isSuccess()) {
                MqttConnectReturnCode returnCode = connectResult.getReturnCode();
                throw new ConnectionFailureException(returnCode.byteValue());
            }
            for(Map.Entry<String,List<IMessageConsumer>> consumerEntry : mqConfig.getTopicConsumers().entrySet()) {
                String topic = consumerEntry.getKey();
                List<IMessageConsumer> consumers = consumerEntry.getValue();
                for(IMessageConsumer consumer : consumers) {
                    mqttClient.on(topic, (consumerTopic, payload) -> {
                        byte[] datas = new byte[payload.readableBytes()];
                        try {
                            payload.retain();
                            payload.readBytes(datas, 0, datas.length);
                        } finally {
                            ReferenceCountUtil.release(payload);
                        }
                        consumer.consume(consumerTopic, datas);
                    });
                }
            }
        } catch (TimeoutException | InterruptedException| ExecutionException ite) {
            throw new ConnectionFailureException(-1, "connect timeout , resource exhausted or system exit ", ite);
        }
    }

    @Override
    public boolean isConnected() {
        checkInit();
        return mqttClient.isConnected();
    }

    @Override
    public void disconnect() {
        if(isConnected()) {
            mqttClient.disconnect();
        }
    }

    @Override
    public void publish(@NonNull IMessage message) {
        if(!isConnected()) {
            connectToBroker();
        }
        if(message instanceof MqttMessage) {
            MqttMessage mqttMessage = (MqttMessage)message;
            ByteBuf payload = Unpooled.wrappedBuffer(mqttMessage.getContent());
            String topic = mqttMessage.getTopic().routeKey();
            MqttQoS qos = mqttMessage.getQos();
            boolean retained = mqttMessage.isRetained();
            mqttClient.publish(topic, payload, qos, retained);
        }
    }


    private void checkInit() {
        if(mqConfig == null) {
            throw new IllegalStateException("!!! mqConfig has not inited");
        }
    }


    class LogMqttHandler implements MqttHandler {
        @Override
        public void onMessage(String topic, ByteBuf payload) {
            if(log.isDebugEnabled()) {
                try {
                    payload.retain();
                    StringBuilder message = new StringBuilder(payload.getCharSequence(0, payload.readableBytes(), Charset.defaultCharset()));
                    log.debug("receive message,'{}' => '{}' ", topic, message.toString());
                } finally {
                    if(payload.refCnt() > 0) {
                        ReferenceCountUtil.safeRelease(payload);
                    }
                }
            }
        }

    }
}
