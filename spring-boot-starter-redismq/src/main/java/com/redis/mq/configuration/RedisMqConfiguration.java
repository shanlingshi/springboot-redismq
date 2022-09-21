package com.redis.mq.configuration;

import com.redis.mq.RedisMessagePublisher;
import com.redis.mq.core.constants.RedisMessageSerializerConstants;
import com.redis.mq.core.listener.RedisMessageListener;
import com.redis.mq.core.repository.RedisListenerDefinition;
import com.redis.mq.core.repository.RedisMessageListenerRepository;
import java.util.Arrays;
import java.util.Map;
import java.util.Map.Entry;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

@Configuration
@ComponentScan(basePackages = "com.redis.mq")
public class RedisMqConfiguration {

    @Bean
    public RedisMessageListenerRepository redisMessageListenRepository() {
        return new RedisMessageListenerRepository();
    }

    @Bean
    @ConditionalOnBean({RedisMessageListenerRepository.class, RedisTemplate.class})
    public RedisMessageListenerContainer redisMessageListenerContainer(RedisConnectionFactory connectionFactory) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        // 订阅
        Map<String, RedisListenerDefinition> listenDefinitionMap = RedisMessageListenerRepository.getListenerDefinition();
        for (Entry<String, RedisListenerDefinition> listenDefinitionEntry : listenDefinitionMap.entrySet()) {
            RedisListenerDefinition listenDefinition = listenDefinitionEntry.getValue();

            container.addMessageListener(new RedisMessageListener(listenDefinition), Arrays.asList(listenDefinition.getTopic()));
        }

        // 序列化对象（特别注意：发布的时候需要设置序列化；订阅方也需要设置序列化）
        container.setTopicSerializer(RedisMessageSerializerConstants.STR_SERIALIZER);
        return container;
    }

    @Bean
    public RedisMessagePublisher redisMessagePublisher(RedisTemplate redisTemplate) {
        return new RedisMessagePublisher(redisTemplate);
    }

}
