package team.ark.core.cache.store;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.lang.NonNull;
import team.ark.core.util.StringUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Redis缓存仓库实现
 *
 * @author Ashinch
 * @date 2020/12/15
 * @see AbstractCacheStore
 */
public class RedisCacheStore extends AbstractCacheStore {
    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    public RedisCacheStore(long defaultTimeout, TimeUnit defaultTimeunit) {
        super(defaultTimeout, defaultTimeunit);
    }

    @Override
    protected Optional<Object> getInternal(@Nonnull String key) {
        Object v = redisTemplate.boundValueOps(key).get();
        return StringUtils.isEmpty(v) ? Optional.empty() : Optional.of(v);
    }

    @Override
    protected void putInternal(@NonNull String key, @NonNull Object value, long timeout, @NonNull TimeUnit timeUnit) {
        redisTemplate.boundValueOps(key).set(value, timeout, timeUnit);
    }

    @Override
    protected Boolean putIfAbsentInternal(@NonNull String key, @NonNull Object value, long timeout, @NonNull TimeUnit timeUnit) {
        return redisTemplate.boundValueOps(key).setIfAbsent(value, timeout, timeUnit);
    }

    @Override
    protected void deleteInternal(@NonNull Collection<String> keys) {
        Set<String> kSet = new HashSet<>(keys);
        redisTemplate.delete(kSet);
    }

    @Override
    protected Boolean isExistInternal(@NonNull String key) {
        return redisTemplate.hasKey(key);
    }

    @Override
    protected void renameInternal(@NonNull String oldKey, @NonNull String newKey) {
        redisTemplate.rename(oldKey, newKey);
    }

    @Override
    protected Boolean renameIfAbsentInternal(@NonNull String oldKey, @NonNull String newKey) {
        return redisTemplate.renameIfAbsent(oldKey, newKey);
    }

    @Override
    protected Long getTimeoutInternal(@NonNull String key, @NonNull TimeUnit timeUnit) {
        return redisTemplate.getExpire(key, timeUnit);
    }

    @Override
    protected void setTimeoutInternal(@NonNull Collection<String> keys, long timeout, @NonNull TimeUnit timeUnit) {
        keys.forEach(k -> redisTemplate.expire(k, timeout, timeUnit));
    }

    @Override
    protected void setExpireAtInternal(@NonNull Collection<String> keys, @NonNull Date date) {
        keys.forEach(k -> redisTemplate.expireAt(k, date));
    }

    @Override
    protected void setPersistInternal(@NonNull Collection<String> keys) {
        keys.forEach(k -> redisTemplate.persist(k));
    }

    @Override
    protected Set<String> getKeySetInternal() {
        return new HashSet<>(Optional.ofNullable(redisTemplate.keys("*")).orElse(new HashSet<>()));
    }
}
