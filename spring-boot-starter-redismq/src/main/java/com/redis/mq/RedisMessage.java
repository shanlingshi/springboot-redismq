package com.redis.mq;

import com.redis.mq.core.constants.RedisMessageSerializerConstants;
import java.nio.charset.StandardCharsets;

public class RedisMessage {

    private String body;

    public RedisMessage() {
    }

    public RedisMessage(String body) {
        this.body = body;
    }

    public String getBody() {
        return body;
    }

    public RedisMessage setBody(String body) {
        this.body = body;
        return this;
    }

    @Override
    public String toString() {
        byte[] bytes = RedisMessageSerializerConstants.VALUE_SERIALIZER.serialize(this);
        return new String(bytes, StandardCharsets.UTF_8);
    }

}
