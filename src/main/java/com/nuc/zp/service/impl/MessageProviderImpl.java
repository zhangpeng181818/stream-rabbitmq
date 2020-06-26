package com.nuc.zp.service.impl;

import com.nuc.zp.service.IMessageProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@EnableBinding(Source.class)
public class MessageProviderImpl implements IMessageProvider {

    @Autowired
    private MessageChannel output;

    private AtomicInteger atomicInteger = new AtomicInteger(0);

    @Override
    public String send() {
        String serial = UUID.randomUUID().toString();

        output.send(MessageBuilder.withPayload(atomicInteger.get() + "^^^^" + serial).setHeader("partitionKey", atomicInteger.get() % 2).build());
        atomicInteger.getAndIncrement();
        log.info("send  = {}", serial);
        return serial;
    }
}
