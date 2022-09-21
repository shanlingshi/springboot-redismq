package com.redis.mq.core.listener;

import com.redis.mq.RedisMessage;
import com.redis.mq.core.constants.RedisMessageConstants;
import com.redis.mq.core.constants.RedisMessageSerializerConstants;
import com.redis.mq.core.repository.RedisListenerDefinition;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.listener.adapter.RedisListenerExecutionFailedException;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.util.ObjectUtils;

/**
 * @author shanlingshi
 * @since 2021-09-03
 */
public class RedisMessageListener implements MessageListener {

    private static final Logger logger = LoggerFactory.getLogger(RedisMessageListener.class);

    private final RedisListenerDefinition listenDefinition;
    private final Jackson2JsonRedisSerializer<Object> valueSerializer;

    public RedisMessageListener(RedisListenerDefinition listenDefinition) {
        this.listenDefinition = listenDefinition;
        this.valueSerializer = RedisMessageSerializerConstants.VALUE_SERIALIZER;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        // Invoke the handler method with appropriate arguments.
        Map<String, String> rawMessage = (Map<String, String>) valueSerializer.deserialize(message.getBody());
        String body = rawMessage.get(RedisMessageConstants.MSG_BODY);

        Object redisMessage = new RedisMessage(body);
        if (String.class.isAssignableFrom(listenDefinition.getMethod().getParameterTypes()[0])) {
            redisMessage = body;
        }

        try {
            listenDefinition.getMethod().invoke(listenDefinition.getBean(), redisMessage);
        } catch (InvocationTargetException ex) {
            Throwable targetEx = ex.getTargetException();
            if (targetEx instanceof DataAccessException) {
                throw (DataAccessException) targetEx;
            } else {
                throw new RedisListenerExecutionFailedException("Listener method '" + listenDefinition.getMethodId() + "' threw exception", targetEx);
            }
        } catch (Throwable ex) {
            throw new RedisListenerExecutionFailedException("Failed to invoke target method '" + listenDefinition.getMethodId() + "' with arguments " + ObjectUtils.nullSafeToString(redisMessage), ex);
        }
    }

}
