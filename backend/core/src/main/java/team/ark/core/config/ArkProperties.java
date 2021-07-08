package team.ark.core.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import team.ark.core.cache.config.CacheProperties;
import team.ark.core.security.JwtProperties;

/**
 * 自定义application.yml配置文件属性
 *
 * @author ash
 * @date 2020/12/15
 */
@Data
@ConfigurationProperties("ark")
public class ArkProperties {
    private JwtProperties jwt;
    private CacheProperties cache;
}
