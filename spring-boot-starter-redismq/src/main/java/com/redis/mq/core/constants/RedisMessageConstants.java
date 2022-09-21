package com.redis.mq.core.constants;

import java.time.format.DateTimeFormatter;

/**
 * @author shanlingshi
 * @since 2021-09-03
 */
public class RedisMessageConstants {

    public static final String MSG_ID = "MQ_MSG_ID";
    public static final String MSG_BODY = "MQ_MSG_BODY";
    public static final String MSG_DELAY_TIME = "MQ_MSG_DELAY_TIME";
    public static final DateTimeFormatter MSG_DELAY_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

}
