package team.ark.core.cache.store;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.util.Assert;
import team.ark.core.cache.config.CacheContract;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 抽象缓存仓库
 *
 * @author Ashinch
 * @date 2020/12/13
 * @see CacheStore
 */
@Slf4j
public abstract class AbstractCacheStore implements CacheStore {
    /**
     * 默认缓存有效期
     */
    public long defaultTimeout;
    /**
     * 默认时间单位
     */
    public TimeUnit defaultTimeunit;

    AbstractCacheStore(long defaultTimeout, TimeUnit defaultTimeunit) {
        this.defaultTimeout = defaultTimeout;
        this.defaultTimeunit = defaultTimeunit;
    }

    /**
     * 获取缓存, 抽象方法
     *
     * @param key 键名, 不能为空
     */
    protected abstract Optional<Object> getInternal(@NonNull String key);

    /**
     * 存入缓存, 抽象方法
     *
     * @param key      键名, 不能为空
     * @param value    缓存值, 不能为空
     * @param timeout  有效时长, 不能为空
     * @param timeUnit 时间单位, 不能为空
     */
    protected abstract void putInternal(@NonNull String key, @NonNull Object value, long timeout,
                                        @NonNull TimeUnit timeUnit);

    /**
     * 当key不存在时再存入缓存, 需要保证线程安全, 抽象方法
     *
     * @param key      键名, 不能为空
     * @param value    缓存值, 不能为空
     * @param timeout  有效时长, 不能为空
     * @param timeUnit 时间单位, 不能为空
     */
    protected abstract Boolean putIfAbsentInternal(@NonNull String key, @NonNull Object value, long timeout,
                                                   @NonNull TimeUnit timeUnit);

    /**
     * 删除缓存, 抽象方法
     *
     * @param keys 键名集合, 不能为空
     */
    protected abstract void deleteInternal(@NonNull Collection<String> keys);

    /**
     * 判断缓存是否存在, 抽象方法
     *
     * @param key 键名, 不能为空
     */
    protected abstract Boolean isExistInternal(@NonNull String key);

    /**
     * 重命名缓存, 如果新键名已存在, 则覆盖新键名与其缓存, 抽象方法
     *
     * @param oldKey 旧键名, 不能为空
     * @param newKey 新键名, 不能为空
     */
    protected abstract void renameInternal(@NonNull String oldKey, @NonNull String newKey);

    /**
     * 当key不存在时再重命名缓存, 需要保证线程安全, 抽象方法
     *
     * @param oldKey 旧键名, 不能为空
     * @param newKey 新键名, 不能为空
     */
    protected abstract Boolean renameIfAbsentInternal(@NonNull String oldKey, @NonNull String newKey);

    /**
     * 获取缓存有效时长, 返回值应根据传入的时间单位转换, 抽象方法
     *
     * @param key      键名, 不能为空
     * @param timeUnit 时间单位, 不能为空
     */
    protected abstract Long getTimeoutInternal(@NonNull String key, @NonNull TimeUnit timeUnit);

    /**
     * 设置缓存有效时长, 有效时长应根据传入的时间单位转换, 抽象方法
     *
     * @param keys     键名集合, 不能为空
     * @param timeout  有效时长
     * @param timeUnit 时间单位, 不能为空
     */
    protected abstract void setTimeoutInternal(@NonNull Collection<String> keys, long timeout,
                                               @NonNull TimeUnit timeUnit);

    /**
     * 设置缓存过期日期, 抽象方法
     *
     * @param keys 键名, 不能为空
     * @param date 过期日期, 应大于当前日期, 不能为空,
     */
    protected abstract void setExpireAtInternal(@NonNull Collection<String> keys, @NonNull Date date);

    /**
     * 设置缓存永久有效, 抽象方法
     *
     * @param keys 键名, 不能为空
     */
    protected abstract void setPersistInternal(@NonNull Collection<String> keys);

    /**
     * 获取有效缓存键名集合, 抽象方法
     */
    protected abstract Set<String> getKeySetInternal();

    @Override
    public Set<String> getKeySet() {
        return getKeySetInternal();
    }

    @Override
    public Optional<Object> get(String key) {
        Assert.hasText(key, "获取缓存的key不能为空");
        Optional<Object> result = getInternal(key);
        log.debug("获取缓存: key[{}], value[{}]", key, result.orElse(null));
        return result;
    }

    @Override
    public void put(String key, Object value) {
        Assert.hasText(key, "缓存的key不能为空");
        Assert.notNull(value, "缓存的value不能为空");
        putInternal(key, value, defaultTimeout, defaultTimeunit);
        log.debug("添加缓存: key[{}], value[{}]", key, value);
    }

    @Override
    public void put(String key, Object value, long timeout) {
        Assert.hasText(key, "缓存的key不能为空");
        Assert.notNull(value, "缓存的value不能为空");
        putInternal(key, value, timeout, CacheContract.DEFAULT_TIMEUNIT);
        log.debug("添加缓存: key[{}], value[{}], timeout[{}]", key, value, timeout);
    }

    @Override
    public void put(String key, Object value, long timeout, TimeUnit timeUnit) {
        Assert.hasText(key, "缓存的key不能为空");
        Assert.notNull(value, "缓存的value不能为空");
        Assert.notNull(timeUnit, "缓存的timeUnit不能为空");
        putInternal(key, value, timeout, timeUnit);
        log.debug("添加缓存: key[{}], value[{}], timeout[{}], timeUnit[{}]", key, value, timeout, timeUnit);
    }

    @Override
    public Boolean putIfAbsent(String key, Object value) {
        Assert.hasText(key, "缓存的key不能为空");
        Assert.notNull(value, "缓存的value不能为空");
        Boolean result = putIfAbsentInternal(key, value, CacheContract.DEFAULT_TIMEOUT, CacheContract.DEFAULT_TIMEUNIT);
        log.debug("添加缓存[{}]: key[{}], value[{}]", result, key, value);
        return result;
    }

    @Override
    public Boolean putIfAbsent(String key, Object value, long timeout) {
        Assert.hasText(key, "缓存的key不能为空");
        Assert.notNull(value, "缓存的value不能为空");
        Boolean result = putIfAbsentInternal(key, value, timeout, CacheContract.DEFAULT_TIMEUNIT);
        log.debug("添加缓存[{}]: key[{}], value[{}], timeout[{}]", result, key, value, timeout);
        return result;
    }

    @Override
    public Boolean putIfAbsent(String key, Object value, long timeout, TimeUnit timeUnit) {
        Assert.hasText(key, "缓存的key不能为空");
        Assert.notNull(value, "缓存的value不能为空");
        Assert.notNull(timeUnit, "缓存的timeUnit不能为空");
        Boolean result = putIfAbsentInternal(key, value, timeout, timeUnit);
        log.debug("添加缓存[{}]: key[{}], value[{}], timeout[{}], timeUnit[{}]", result, key, value, timeout, timeUnit);
        return result;
    }

    @Override
    public void delete(String... keys) {
        Assert.notNull(keys, "删除的缓存keys不能为空");
        deleteInternal(Stream.of(keys).collect(Collectors.toSet()));
        log.debug("删除缓存: key{}, ", Arrays.toString(keys));
    }

    @Override
    public void delete(Collection<String> keys) {
        Assert.notNull(keys, "删除的缓存keys不能为空");
        deleteInternal(keys);
        log.debug("删除缓存: key{}, ", Arrays.toString(keys.toArray()));
    }

    @Override
    public Boolean isExist(String key) {
        Assert.hasText(key, "判断是否已缓存的key不能为空");
        Boolean result = isExistInternal(key);
        log.debug("是否存在缓存: key[{}], result[{}]", key, result);
        return result;
    }

    @Override
    public void rename(String oldKey, String newKey) {
        Assert.hasText(oldKey, "重命名的缓存oldKey不能为空");
        Assert.hasText(newKey, "重命名的缓存newKey不能为空");
        renameInternal(oldKey, newKey);
        log.debug("重命名缓存: oldKey[{}], newKey[{}]", oldKey, newKey);
    }

    @Override
    public Boolean renameIfAbsent(String oldKey, String newKey) {
        Assert.hasText(oldKey, "重命名的缓存oldKey不能为空");
        Assert.hasText(newKey, "重命名的缓存newKey不能为空");
        Boolean result = renameIfAbsentInternal(oldKey, newKey);
        log.debug("重命名缓存[{}]: oldKey[{}], newKey[{}]", result, oldKey, newKey);
        return result;
    }

    @Override
    public Long getTimeout(String key) {
        Assert.hasText(key, "获取缓存过期时间的key不能为空");
        Long result = getTimeoutInternal(key, CacheContract.DEFAULT_TIMEUNIT);
        log.debug("获取缓存过期时间: key[{}], result[{}]", key, result);
        return result;
    }

    @Override
    public Long getTimeout(String key, TimeUnit timeUnit) {
        Assert.hasText(key, "获取缓存过期时间的key不能为空");
        Assert.notNull(timeUnit, "获取缓存过期时间的timeUnit不能为空");
        Long result = getTimeoutInternal(key, timeUnit);
        log.debug("获取缓存过期时间: key[{}], result[{}], timeUnit[{}]", key, result, timeUnit);
        return result;
    }

    @Override
    public void setTimeout(String key, long timeout) {
        Assert.hasText(key, "设置缓存过期时间的key不能为空");
        setTimeoutInternal(Lists.newArrayList(key), timeout, CacheContract.DEFAULT_TIMEUNIT);
        log.debug("设置缓存过期时间: key[{}], timeout[{}]", key, timeout);
    }

    @Override
    public void setTimeout(String key, long timeout, TimeUnit timeUnit) {
        Assert.hasText(key, "设置缓存过期时间的key不能为空");
        Assert.notNull(timeUnit, "设置缓存过期时间的timeUnit不能为空");
        setTimeoutInternal(Lists.newArrayList(key), timeout, timeUnit);
        log.debug("设置缓存过期时间: key[{}], timeout[{}], timeUnit[{}]", key, timeout, timeUnit);
    }

    @Override
    public void setTimeout(Collection<String> keys, long timeout) {
        Assert.notNull(keys, "设置缓存过期时间的keys不能为空");
        setTimeoutInternal(keys, timeout, CacheContract.DEFAULT_TIMEUNIT);
        log.debug("设置缓存过期时间: key{}, timeout[{}]", Arrays.toString(keys.toArray()), timeout);
    }

    @Override
    public void setTimeout(Collection<String> keys, long timeout, TimeUnit timeUnit) {
        Assert.notNull(keys, "设置缓存过期时间的keys不能为空");
        Assert.notNull(timeUnit, "设置缓存过期时间的timeUnit不能为空");
        setTimeoutInternal(keys, timeout, timeUnit);
        log.debug("设置缓存过期时间: key{}, timeout[{}], timeUnit[{}]", Arrays.toString(keys.toArray()), timeout, timeUnit);
    }

    @Override
    public void setExpireAt(String key, Date date) {
        Assert.notNull(key, "设置缓存过期日期的key不能为空");
        Assert.notNull(date, "设置缓存过期日期的date不能为空");
        setExpireAtInternal(Lists.newArrayList(key), date);
        log.debug("设置缓存过期日期: key[{}], date[{}]", key, date);
    }

    @Override
    public void setExpireAt(Collection<String> keys, Date date) {
        Assert.notNull(keys, "设置缓存过期日期的keys不能为空");
        Assert.notNull(date, "设置缓存过期日期的date不能为空");
        setExpireAtInternal(keys, date);
        log.debug("设置缓存过期日期: key{}, date[{}]", Arrays.toString(keys.toArray()), date);
    }

    @Override
    public void setPersist(String key) {
        Assert.notNull(key, "设置缓存永久有效的key不能为空");
        setPersistInternal(Lists.newArrayList(key));
        log.debug("设置缓存永久有效: key[{}]", key);
    }

    @Override
    public void setPersist(Collection<String> keys) {
        Assert.notNull(keys, "设置缓存永久有效的keys不能为空");
        setPersistInternal(keys);
        log.debug("设置缓存永久有效: key{}", Arrays.toString(keys.toArray()));
    }
}
