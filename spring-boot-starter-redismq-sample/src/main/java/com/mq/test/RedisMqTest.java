package com.mq.test;

import com.redis.mq.RedisMessage;
import com.redis.mq.RedisSubscribe;
import org.springframework.stereotype.Component;

@Component
public class RedisMqTest {

    /**
     * 使用, 直接在bean里面写一个方法 写上注解，写上队列名称就能消费了
     * 测试：lpush test.mq.queue1 "{\"message\":\"hello world\", \"retryTimes\":3}"
     */
    @RedisSubscribe(topic = "test.mq.queue")
    public void testMQ1(RedisMessage message) {

        System.out.println(message.toString());
    }

    @RedisSubscribe(topic = {"test.mq.queue", "test.mq.queue.str"})
    public void testMQ2(String message) {
//        if (true) {
//            throw new RuntimeException();
//        }
        System.out.println(message);
    }

}
