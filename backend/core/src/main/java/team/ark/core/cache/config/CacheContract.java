package team.ark.core.cache.config;

import java.util.concurrent.TimeUnit;

/**
 * 缓存常量
 *
 * @author Ashinch
 * @date 2020/12/15
 */
public interface CacheContract {
    /**
     * 默认缓存有效期: 10
     */
    long DEFAULT_TIMEOUT = 10;
    /**
     * 默认时间单位: 分钟
     */
    TimeUnit DEFAULT_TIMEUNIT = TimeUnit.MINUTES;
}
