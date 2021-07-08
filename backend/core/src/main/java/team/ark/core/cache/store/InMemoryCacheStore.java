package team.ark.core.cache.store;

import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.springframework.lang.NonNull;
import team.ark.core.lock.Lock;

import javax.annotation.PreDestroy;
import java.util.Collection;
import java.util.Date;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 内存缓存仓库实现
 *
 * @author Ashinch
 * @date 2020/12/15
 * @see AbstractCacheStore
 */
@Slf4j
public class InMemoryCacheStore extends AbstractCacheStore {
    /**
     * 计划周期
     */
    private final static long PERIOD = 60;
    /**
     * 计划周期单位: 秒
     */
    private final static TimeUnit TIME_UNIT = TimeUnit.SECONDS;
    /**
     * 缓存容器
     */
    private final static ConcurrentHashMap<String, CacheWrapper> CACHE_CONTAINER = new ConcurrentHashMap<>();
    /**
     * 计划执行器
     */
    private final static ScheduledExecutorService EXECUTOR_SERVICE = new ScheduledThreadPoolExecutor(1,
            new BasicThreadFactory.Builder()
                    .namingPattern("CacheExpiryCleaner-schedule-pool-%d")
                    .daemon(true)
                    .build()
    );

    /**
     * 初始化计划执行器, 定时删除过期缓存
     */
    public InMemoryCacheStore(long defaultTimeout, TimeUnit defaultTimeunit) {
        super(defaultTimeout, defaultTimeunit);
        EXECUTOR_SERVICE.scheduleAtFixedRate(() ->
                CACHE_CONTAINER.keySet().forEach(key -> {
                    if (CACHE_CONTAINER.get(key).getExpireAt() < System.currentTimeMillis()) {
                        CACHE_CONTAINER.remove(key);
                        log.debug("删除过期缓存: [{}]", key);
                    }
                }), 0, PERIOD, TIME_UNIT
        );
    }

    @Override
    protected Optional<Object> getInternal(@NonNull String key) {
        return Optional.ofNullable(CACHE_CONTAINER.get(key)).map(v -> {
            if (v.getExpireAt() < System.currentTimeMillis()) {
                CACHE_CONTAINER.remove(key);
                return null;
            }
            return v.getValue();
        });
    }

    @Override
    protected void putInternal(@NonNull String key, @NonNull Object value, long timeout, @NonNull TimeUnit timeUnit) {
        CACHE_CONTAINER.put(key, new CacheWrapper()
                .setValue(value)
                .setExpireAt(System.currentTimeMillis() + timeUnit.toMillis(timeout))
        );
    }

    @Lock
    @Override
    protected Boolean putIfAbsentInternal(@NonNull String key, @NonNull Object value, long timeout, @NonNull TimeUnit timeUnit) {
        if (isExistInternal(key)) {
            return Boolean.FALSE;
        }
        putInternal(key, value, timeout, timeUnit);
        return Boolean.TRUE;
    }

    @Override
    protected void deleteInternal(@NonNull Collection<String> keys) {
        keys.forEach(CACHE_CONTAINER::remove);
    }

    @Override
    protected Boolean isExistInternal(@NonNull String key) {
        return CACHE_CONTAINER.containsKey(key)
                && CACHE_CONTAINER.get(key).getExpireAt() >= System.currentTimeMillis();
    }

    @Override
    protected void renameInternal(@NonNull String oldKey, @NonNull String newKey) {
        CACHE_CONTAINER.put(newKey, CACHE_CONTAINER.remove(oldKey));
    }

    @Lock
    @Override
    protected Boolean renameIfAbsentInternal(@NonNull String oldKey, @NonNull String newKey) {
        if (isExistInternal(newKey)) {
            return Boolean.FALSE;
        }
        renameInternal(oldKey, newKey);
        return Boolean.TRUE;
    }

    @Override
    protected Long getTimeoutInternal(@NonNull String key, @NonNull TimeUnit timeUnit) {
        CacheWrapper v = CACHE_CONTAINER.get(key);
        if (v == null) {
            return -2L;
        }
        if (v.getExpireAt() == Long.MAX_VALUE) {
            return -1L;
        }
        return v.getExpireAt() >= System.currentTimeMillis()
                ? timeUnit.convert(v.getExpireAt() - System.currentTimeMillis(), TimeUnit.MILLISECONDS)
                : -2L;
    }

    @Override
    protected void setTimeoutInternal(@NonNull Collection<String> keys, long timeout, @NonNull TimeUnit timeUnit) {
        keys.forEach(k -> {
            CacheWrapper v = CACHE_CONTAINER.get(k);
            if (v != null) {
                CACHE_CONTAINER.put(k, v.setExpireAt(System.currentTimeMillis() + timeUnit.toMillis(timeout)));
            }
        });
    }

    @Override
    protected void setExpireAtInternal(@NonNull Collection<String> keys, @NonNull Date date) {
        keys.forEach(k -> {
            CacheWrapper v = CACHE_CONTAINER.get(k);
            if (v != null) {
                CACHE_CONTAINER.put(k, v.setExpireAt(date.getTime()));
            }
        });
    }

    @Override
    protected void setPersistInternal(@NonNull Collection<String> keys) {
        keys.forEach(k -> {
            CacheWrapper v = CACHE_CONTAINER.get(k);
            if (v != null) {
                CACHE_CONTAINER.put(k, v.setExpireAt(Long.MAX_VALUE));
            }
        });
    }

    @Override
    protected Set<String> getKeySetInternal() {
        Date now = new Date();
        return CACHE_CONTAINER.keySet().stream()
                .filter(k -> CACHE_CONTAINER.get(k).getExpireAt() >= System.currentTimeMillis())
                .collect(Collectors.toSet());
    }

    /**
     * 析构时停止计划执行器, 清空缓存容器
     */
    @PreDestroy
    public void preDestroy() {
        log.debug("计划执行器: [{}] 关闭", EXECUTOR_SERVICE);
        EXECUTOR_SERVICE.shutdown();
        CACHE_CONTAINER.clear();
    }

    /**
     * 缓存内部包装类
     */
    @Data
    @Accessors(chain = true)
    private static class CacheWrapper {
        /**
         * 缓存值
         */
        private Object value;
        /**
         * 过期时间戳
         */
        private long expireAt;
    }
}
