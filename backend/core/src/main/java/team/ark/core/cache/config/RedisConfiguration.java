package team.ark.core.cache.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import team.ark.core.util.JsonUtils;

/**
 * Redis默认序列化配置
 *
 * @author Ashinch
 * @date 2020/12/15
 */
@Configuration
public class RedisConfiguration extends CachingConfigurerSupport {
    @Bean
    public CacheManager cacheManager(RedisConnectionFactory factory) {
        return RedisCacheManager.create(factory);
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        // 使用Jackson2JsonRedisSerializer来序列化和反序列化redis的value值（默认是使用JDK的序列化方式）
        Jackson2JsonRedisSerializer<Object> jackson = new Jackson2JsonRedisSerializer<>(Object.class);
        jackson.setObjectMapper(JsonUtils.getCompleteMapper());
        // 设置连接工厂
        template.setConnectionFactory(factory);
        // 键使用StringRedisSerializer序列化模式
        template.setKeySerializer(new StringRedisSerializer());
        // 值使用Json序列化模式
        template.setValueSerializer(jackson);
        // Hash键使用StringRedisSerializer序列化模式
        template.setHashKeySerializer(new StringRedisSerializer());
        // Hash值使用StringRedisSerializer序列化模式
        template.setHashValueSerializer(jackson);
        template.afterPropertiesSet();
        return template;
    }
}
