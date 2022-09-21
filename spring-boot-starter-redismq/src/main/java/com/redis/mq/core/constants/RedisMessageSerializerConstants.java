package com.redis.mq.core.constants;

import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @author shanlingshi
 * @since 2021-09-03
 */
public class RedisMessageSerializerConstants {

    public static final StringRedisSerializer STR_SERIALIZER = new StringRedisSerializer();
    public static final Jackson2JsonRedisSerializer<Object> VALUE_SERIALIZER = new Jackson2JsonRedisSerializer<>(Object.class);

}
