package com.redis.mq.core.repository;

import com.redis.mq.RedisSubscribe;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.MethodIntrospector;
import org.springframework.core.MethodIntrospector.MetadataLookup;
import org.springframework.core.annotation.AnnotatedElementUtils;

/**
 * @author shanlingshi
 * @since 2021-09-03
 */
public class RedisMessageListenerRepository implements ApplicationContextAware, InitializingBean, DisposableBean {

    private static final Logger logger = LoggerFactory.getLogger(RedisMessageListenerRepository.class);

    /**
     * redisMessageListenDefinition cache
     */
    private static Map<String, RedisListenerDefinition> LISTENER_DEFINITION = new ConcurrentHashMap<>();

    @Override
    public void afterPropertiesSet() {

        initRepository(applicationContext);
    }

    private void initRepository(ApplicationContext applicationContext) {
        if (applicationContext == null) {
            return;
        }

        // init redis message listener
        String[] beanDefinitionNames = applicationContext.getBeanNamesForType(Object.class, false, true);
        for (String beanDefinitionName : beanDefinitionNames) {
            Object bean = applicationContext.getBean(beanDefinitionName);

            Map<Method, RedisSubscribe> annotatedMethods = null;   // referred to ：org.springframework.context.event
            // .EventListenerMethodProcessor.processBean
            try {
                annotatedMethods = MethodIntrospector.selectMethods(bean.getClass(), redisListenerMetadataLookup());
            } catch (Throwable ex) {
                logger.error("redismq method-handler resolve error for bean[" + beanDefinitionName + "].", ex);
            }

            if (annotatedMethods==null || annotatedMethods.isEmpty()) {
                continue;
            }

            for (Map.Entry<Method, RedisSubscribe> methodEntry : annotatedMethods.entrySet()) {
                Method listenMethod = methodEntry.getKey();
                RedisSubscribe redisSubscribe = methodEntry.getValue();

                RedisListenerDefinition listenDefinition = new RedisListenerDefinition(redisSubscribe.topic());
                listenDefinition.setBean(bean);
                listenDefinition.setMethod(listenMethod);
                listenDefinition.setMethodId(bean.getClass().getName() + "#" + listenMethod.getName());

                // regist
                LISTENER_DEFINITION.put(listenDefinition.getMethodId(), listenDefinition);
            }
        }
    }


    private MetadataLookup<RedisSubscribe> redisListenerMetadataLookup() {
        return new MetadataLookup<RedisSubscribe>() {
            @Override
            public RedisSubscribe inspect(Method method) {
                return AnnotatedElementUtils.findMergedAnnotation(method, RedisSubscribe.class);
            }
        };
    }

    public static Map<String, RedisListenerDefinition> getListenerDefinition() {
        return LISTENER_DEFINITION;
    }

    // ---------------------- applicationContext ----------------------
    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void destroy() throws Exception {
        // destroy LISTENER_DEFINITION
        if (LISTENER_DEFINITION.size() > 0) {
            LISTENER_DEFINITION.clear();
        }
    }

}
