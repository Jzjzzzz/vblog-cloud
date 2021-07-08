package team.ark.core.cache.config;

import lombok.Data;
import team.ark.core.cache.store.InMemoryCacheStore;
import team.ark.core.cache.store.RedisCacheStore;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

/**
 * 缓存配置
 *
 * @author Ashinch
 * @date 2020/12/16
 */
@Data
public class CacheProperties implements Serializable {
    private static final long serialVersionUID = -7688700534094851079L;
    /**
     * 缓存仓库实现类型, 默认Memory, 可选Redis
     *
     * @see InMemoryCacheStore
     * @see RedisCacheStore
     */
    private String type = "memory";
    /**
     * 默认缓存有效期: 10
     */
    private long timeout = 10;
    /**
     * 默认时间单位: 分钟, 可选{@linkplain TimeUnit}枚举中的时间单位
     */
    private String timeunit = "MINUTES";
}
