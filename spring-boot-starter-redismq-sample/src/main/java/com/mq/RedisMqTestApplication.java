package com.mq;


import com.redis.mq.RedisMessage;
import com.redis.mq.RedisMessagePublisher;
import com.redis.mq.configuration.EnableRedisMQ;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.redis.core.RedisTemplate;

@EnableRedisMQ
@ComponentScan({"com.mq.test"})
@SpringBootApplication
public class RedisMqTestApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(RedisMqTestApplication.class, args);

        RedisMessagePublisher messagePublisher = applicationContext.getBean(RedisMessagePublisher.class);
        messagePublisher.publish("test.mq.queue", new RedisMessage("test"));
        messagePublisher.publish("test.mq.queue.str", "test-str");
    }

}
