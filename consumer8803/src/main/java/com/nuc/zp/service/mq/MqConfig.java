package com.nuc.zp.service.mq;

import java.io.Serializable;
import java.util.*;

public class MqConfig implements Serializable {
    private Broker broker;
    private String username;
    private String password;
    private Map<String, List<IMessageConsumer>> topicConsumers = new HashMap<>();

    public MqConfig setBroker(String host, int port) {
        broker = Broker.build(host, port);
        return this;
    }

    public MqConfig setBroker(Broker broker) {
        this.broker = broker;
        return this;
    }

    public MqConfig setUsername(String username) {
        this.username = username;
        return this;
    }

    public MqConfig setPassword(String password) {
        this.password = password;
        return this;
    }

    public MqConfig registerConsumer(String topic, IMessageConsumer consumer) {
        if(consumer == null) return this;
        List<IMessageConsumer> consumers = Arrays.asList(consumer);
        registerConsumers(topic, consumers);
        return this;
    }

    public MqConfig registerConsumers(String topic, List<IMessageConsumer> consumers) {
        Map<String, List<IMessageConsumer>> topicConsumers = new HashMap<>();
        topicConsumers.put(topic, consumers);
        registerConsumers(topicConsumers);

        return this;
    }

    public MqConfig registerConsumers(Map<String, List<IMessageConsumer>> tc) {
        for(Map.Entry<String, List<IMessageConsumer>> entry : tc.entrySet()) {
            String topic = entry.getKey();
            List<IMessageConsumer> consumers = entry.getValue();
            if(consumers == null) {
                topicConsumers.remove(topic);
            } else if(topicConsumers.containsKey(topic)) {
                List<IMessageConsumer> existConsumers = topicConsumers.get(topic);
                existConsumers.addAll(consumers);
            } else {
                topicConsumers.put(topic, consumers);
            }
        }
        return this;
    }

    public Broker getBroker() {
        return broker;
    }

    public Map<String, List<IMessageConsumer>> getTopicConsumers() {
        return Collections.unmodifiableMap(topicConsumers);
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    static class Broker {
        // boolean ssl;
        String host;
        int port;

        private Broker(String host, int port) {
            this.host = host;
            this.port = port;
        }

        public String getHost() {
            return host;
        }

        public int getPort() {
            return port;
        }

        public static Broker build(String host, int port) {
            return new Broker(host, port);
        }
    }

}
