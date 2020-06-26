package com.nuc.zp.service;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

public interface CustomInput {

    String TESTINPUT = "testInput";


    @Input(TESTINPUT)
    SubscribableChannel testInput();

}
