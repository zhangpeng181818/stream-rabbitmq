package com.nuc.zp.service.controller;

import com.nuc.zp.service.CustomInput;
import com.nuc.zp.service.mq.IMessageProcessor;
import com.nuc.zp.service.mq.MqConfig;
import com.nuc.zp.service.mq.MqttMessageProcessor;
import com.nuc.zp.service.mq.PumpEventConsumer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Slf4j
@EnableBinding({Sink.class})
@Component
public class ReceiverMessageController {

    @Value("${server.port}")
    private Integer port;

    @StreamListener(Sink.INPUT)
    public void input2(Message<String> message) {
        log.info("我是消费者2：port={} , {}", port, message.getPayload());

//        throw new RuntimeException("失败重试");
    }

    @Autowired
    private PumpEventConsumer pumpEventConsumer;

//    @PostConstruct
    public void test(){
        MqConfig config = new MqConfig();
        config.setBroker("192.168.0.11", 1883) ; // mqtt;
        config.setUsername("rabbit");
        config.setPassword("rabbit");
        config.registerConsumer("icdr/pump/#",pumpEventConsumer);
        IMessageProcessor messageProcessor = new MqttMessageProcessor();
        messageProcessor.init(config);
        messageProcessor.connectToBroker();
        System.out.println("start testPublishMessage");


    }
}
