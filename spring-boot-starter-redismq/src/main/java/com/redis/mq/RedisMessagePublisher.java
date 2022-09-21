package com.redis.mq;

import com.redis.mq.core.constants.RedisMessageConstants;
import com.redis.mq.core.constants.RedisMessageSerializerConstants;
import com.redis.mq.core.utils.MsgIdUtils;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * @author shanlingshi
 * @since 2021-09-03
 */
public class RedisMessagePublisher {

    private RedisTemplate<String, Object> redisTemplate;

    public RedisMessagePublisher(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.redisTemplate.setKeySerializer(RedisMessageSerializerConstants.STR_SERIALIZER);
        this.redisTemplate.setValueSerializer(RedisMessageSerializerConstants.VALUE_SERIALIZER);
    }

    public void publish(String topic, String message) {
        publish(topic, new RedisMessage(message), null);
    }

    public void publish(String topic, RedisMessage message) {
        publish(topic, message, null);
    }

    public void publish(String topic, RedisMessage message, LocalDateTime sendTime) {
        Map<String, String> rawBody = new HashMap<>();
        rawBody.put(RedisMessageConstants.MSG_ID, MsgIdUtils.msgId(message));
        rawBody.put(RedisMessageConstants.MSG_BODY, message.getBody());
        if (sendTime != null) {
            rawBody.put(RedisMessageConstants.MSG_DELAY_TIME, RedisMessageConstants.MSG_DELAY_TIME_FORMATTER.format(sendTime));
        }

        redisTemplate.convertAndSend(topic, rawBody);
    }

}
