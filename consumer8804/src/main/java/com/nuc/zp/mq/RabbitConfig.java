package com.nuc.zp.mq;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * 类功能描述：<br>
 * Broker:它提供一种传输服务,它的角色就是维护一条从生产者到消费者的路线，保证数据能按照指定的方式进行传输,
 * Exchange：消息交换机,它指定消息按什么规则,路由到哪个队列。
 * Queue:消息的载体,每个消息都会被投到一个或多个队列。
 * Binding:绑定，它的作用就是把exchange和queue按照路由规则绑定起来.
 * Routing Key:路由关键字,exchange根据这个关键字进行消息投递。
 * vhost:虚拟主机,一个broker里可以有多个vhost，用作不同用户的权限分离。
 * Producer:消息生产者,就是投递消息的程序.
 * Consumer:消息消费者,就是接受消息的程序.
 * Channel:消息通道,在客户端的每个连接里,可建立多个channel.
 */
@Configuration
public class RabbitConfig {

    //    @Value("${}")
    public static final String PUMP_EVENT_TOPIC_QUEUE_NAME = "fluid";

    public static final String PUMP_EVENT_TOPIC_EXCHANGE = "amq.topic";

    //    @Value("${}")
    public static final String PUMP_EVENT_TOPIC_ROUTINGKEY = "icdr.pump.#.up.deviceEvent";


    @Bean
    public Queue createTopicQueue() {
        Map<String, Object> args = new HashMap<>();
        args.put(QueueArgument.X_MESSAGE_TTL.key, QueueArgument.X_MESSAGE_TTL.value);
        return new Queue(PUMP_EVENT_TOPIC_QUEUE_NAME, true, false, false, args);
    }


    @Bean
    TopicExchange defTopicExchange() {
        return new TopicExchange(PUMP_EVENT_TOPIC_EXCHANGE);
    }

    @Bean
    Binding bindingTopic() {
        return BindingBuilder.bind(createTopicQueue()).
                to(defTopicExchange()).
                with(PUMP_EVENT_TOPIC_ROUTINGKEY);
    }

    enum QueueArgument {
        X_MESSAGE_TTL("x-message-ttl", 10000);

        String key;
        Object value;

        QueueArgument(String key, Object value) {
            this.key = key;
            this.value = value;
        }

        public String getKey() {
            return key;
        }

        public Object getValue() {
            return value;
        }
    }


}