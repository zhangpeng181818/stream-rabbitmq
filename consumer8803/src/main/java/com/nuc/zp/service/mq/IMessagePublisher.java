package com.nuc.zp.service.mq;

/**
 * @author helongyun
 * @date 2020/6/23 0:05
 */
public interface IMessagePublisher {

    void publish(IMessage message);

}
