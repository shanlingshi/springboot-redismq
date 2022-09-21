package com.redis.mq.core.repository;

import java.lang.reflect.Method;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.Topic;

/**
 * @author shanlingshi
 * @since 2021-09-03
 */
public class RedisListenerDefinition {
    private Topic[] topic;

    private Object bean;

    private Method method;

    private String methodId;

    public RedisListenerDefinition() {
    }

    public RedisListenerDefinition(String... topic) {
        this.topic = new Topic[topic.length];

        for (int i = 0; i < topic.length; i++) {
            this.topic[i] = PatternTopic.of(topic[i]);
        }
    }

    public Topic[] getTopic() {
        return topic;
    }

    public RedisListenerDefinition setTopic(Topic... topic) {
        this.topic = topic;
        return this;
    }

    public Object getBean() {
        return bean;
    }

    public RedisListenerDefinition setBean(Object bean) {
        this.bean = bean;
        return this;
    }

    public Method getMethod() {
        return method;
    }

    public RedisListenerDefinition setMethod(Method method) {
        this.method = method;
        return this;
    }

    public String getMethodId() {
        return methodId;
    }

    public RedisListenerDefinition setMethodId(String methodId) {
        this.methodId = methodId;
        return this;
    }

}
