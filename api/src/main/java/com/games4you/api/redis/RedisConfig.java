package com.games4you.api.redis;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.context.annotation.Bean;
import com.games4you.api.domain.model.game.Game;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;

@Configuration
public class RedisConfig {

    private static final Logger log = LoggerFactory.getLogger(RedisConfig.class);

    @Bean
    public ChannelTopic topic() { return new ChannelTopic("websocket:messages"); }

    @Bean
    public ObjectMapper mapper() {
	    ObjectMapper objectMapper = new ObjectMapper();
	    return objectMapper;
    }
    @Bean
    public RedisMessageListenerContainer redisContainer(RedisConnectionFactory cf,
	MessageListenerAdapter listenerAdapter, ChannelTopic topic) {
	    
        log.info("Configuring redis message listener container...");
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(cf);
        container.addMessageListener(listenerAdapter, topic);
        return container;
    }

    @Bean
    public RedisReceiver receiver(SimpMessagingTemplate msgTemplate, ObjectMapper mapper) {
        return new RedisReceiver(msgTemplate, mapper);
    }

    @Bean
    public MessageListenerAdapter listenerAdapter(RedisReceiver receiver) {
        return new MessageListenerAdapter(receiver, "receiveMessage");
    }
    
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
	log.info("Configuring redis template...");
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        // Use String serializer for keys
        template.setKeySerializer(new StringRedisSerializer());

        // Use JSON serializer for POJO values
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        template.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());

        return template;
    }
}
