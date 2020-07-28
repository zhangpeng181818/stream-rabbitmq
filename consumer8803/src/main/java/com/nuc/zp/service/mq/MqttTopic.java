package com.nuc.zp.service.mq;

/**
 * @author helongyun
 * @date 2020/6/27 21:29
 */
public class MqttTopic extends Topic {

    public MqttTopic() {
        super();

    }

    @Override
    public String routeKey() {
        StringBuilder sb = new StringBuilder(getPlatform());
        if(getModule() != null) {
            sb.append(SPLIT);
            sb.append(getModule());
        }
        if(getVendor() != null) {
            sb.append(SPLIT);
            sb.append(getVendor());
        }
        if(getSn() != null) {
            sb.append(SPLIT);
            sb.append(getSn());
        }
        if(getBedNo() != null) {
            sb.append(SPLIT);
            sb.append(getBedNo());
        }
        if(getDirection() != null) {
            sb.append(SPLIT);
            sb.append(getDirection().getValue());
        }
        if(getAction() != null) {
            sb.append(SPLIT);
            sb.append(getAction());
        }

        return sb.toString();
    }

    public static MqttTopic from(String rawTopic) {
        MqttTopic topic = new MqttTopic();
        if(rawTopic == null || rawTopic.isEmpty()) {
            topic.setPlatform(DEFAULT_PLATFORM);
            return topic;
        }
        String[] items = rawTopic.split(SPLIT);
        int itemLength = items.length;
        if(itemLength == 0) {
            topic.setPlatform(DEFAULT_PLATFORM);
        } else {
            topic.setPlatform(items[0]);
        }
        if(itemLength > 1) {
            topic.setModule(items[1]);
        }
        if(itemLength > 2) {
            topic.setVendor(items[2]);
        }
        if(itemLength > 3) {
            topic.setSn(items[3]);
        }
        if(itemLength > 4) {
            topic.setBedNo(items[4]);
        }
        if(itemLength > 5) {
            topic.setDirection(TopicDirection.parse(items[5]));
        }
        if(itemLength > 6) {
            topic.setAction(items[6]);
        }

        return topic;
    }

    static final String SPLIT = "/";

}
