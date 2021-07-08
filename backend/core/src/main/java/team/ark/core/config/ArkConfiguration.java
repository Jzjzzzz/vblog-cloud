package team.ark.core.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import team.ark.core.cache.config.CacheProperties;
import team.ark.core.cache.store.CacheStore;
import team.ark.core.cache.store.InMemoryCacheStore;
import team.ark.core.cache.store.RedisCacheStore;
import team.ark.core.exception.ArkConfigException;
import team.ark.core.security.JwtProperties;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * 解析配置文件
 *
 * @author Ashinch
 * @date 2020/12/15
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(ArkProperties.class)
public class ArkConfiguration {
    @Resource
    private ArkProperties arkProperties;

    @Bean
    public JwtProperties jwtProperties() {
        return arkProperties.getJwt();
    }

    @Bean
    public CacheStore cacheStore() throws ArkConfigException {
        CacheProperties cacheProperties = arkProperties.getCache();
        long timeout = cacheProperties.getTimeout();
        TimeUnit timeunit;
        switch (cacheProperties.getTimeunit().toUpperCase().trim()) {
            case "NANOSECONDS":
                timeunit = TimeUnit.NANOSECONDS;
                break;
            case "MICROSECONDS":
                timeunit = TimeUnit.MICROSECONDS;
                break;
            case "MILLISECONDS":
                timeunit = TimeUnit.MILLISECONDS;
                break;
            case "SECONDS":
                timeunit = TimeUnit.SECONDS;
                break;
            case "MINUTES":
                timeunit = TimeUnit.MINUTES;
                break;
            case "HOURS":
                timeunit = TimeUnit.HOURS;
                break;
            case "DAYS":
                timeunit = TimeUnit.DAYS;
                break;
            default:
                throw new ArkConfigException("[ark.cache.timeunit] 配置错误");
        }
        CacheStore cacheStore;
        switch (cacheProperties.getType()) {
            case "redis":
                cacheStore = new RedisCacheStore(timeout, timeunit);
                break;
            case "memory":
                cacheStore = new InMemoryCacheStore(timeout, timeunit);
                break;
            default:
                throw new ArkConfigException("[ark.cache] 配置错误");
        }
        return cacheStore;
    }
}
