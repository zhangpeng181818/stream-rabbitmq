package com.nuc.zp.service.mq;

/**
 * @author helongyun
 * @date 2020/6/22 23:58
 */
public interface IMessage {
    Topic getTopic();

    byte[] getContent();


}
