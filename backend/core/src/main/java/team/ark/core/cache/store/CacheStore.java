package team.ark.core.cache.store;

import java.util.Collection;
import java.util.Date;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * 缓存仓库通用接口
 *
 * @author Ashinch
 * @date 2020/12/15
 */
public interface CacheStore {
    /**
     * 返回缓存值
     *
     * @param key 键名
     */
    Optional<Object> get(String key);

    /**
     * 存入缓存
     *
     * @param key   键名
     * @param value 缓存值
     */
    void put(String key, Object value);

    /**
     * 存入缓存
     *
     * @param key     键名
     * @param value   缓存值
     * @param timeout 有效时长
     */
    void put(String key, Object value, long timeout);

    /**
     * 存入缓存
     *
     * @param key      键名
     * @param value    缓存值
     * @param timeout  有效时长
     * @param timeUnit 时间单位
     */
    void put(String key, Object value, long timeout, TimeUnit timeUnit);

    /**
     * 当缓存不存在时才存入, 返回存入结果
     *
     * @param key   键名
     * @param value 缓存值
     */
    Boolean putIfAbsent(String key, Object value);

    /**
     * 当缓存不存在时才存入, 返回存入结果
     *
     * @param key     键名
     * @param value   缓存值
     * @param timeout 有效时长
     */
    Boolean putIfAbsent(String key, Object value, long timeout);

    /**
     * 当缓存不存在时才存入, 返回存入结果
     *
     * @param key      键名
     * @param value    缓存值
     * @param timeout  有效时长
     * @param timeUnit 时间单位
     */
    Boolean putIfAbsent(String key, Object value, long timeout, TimeUnit timeUnit);

    /**
     * 删除缓存
     *
     * @param keys 批量键名
     */
    void delete(String... keys);

    /**
     * 删除缓存
     *
     * @param keys 键名集合
     */
    void delete(Collection<String> keys);

    /**
     * 返回是否存在缓存
     *
     * @param key 键名
     */
    Boolean isExist(String key);

    /**
     * 重命名缓存, 无论新键名是否存在, 都会覆盖新键名与其缓存
     *
     * @param oldKey 旧键名
     * @param newKey 新键名
     */
    void rename(String oldKey, String newKey);

    /**
     * 重命名缓存, 当新键名不存在缓存时才进行重命名, 返回重命名结果
     *
     * @param oldKey 旧键名
     * @param newKey 新键名
     */
    Boolean renameIfAbsent(String oldKey, String newKey);

    /**
     * 返回缓存有效时长
     *
     * @param key 键名
     */
    Long getTimeout(String key);

    /**
     * 根据传入时间单位转换返回缓存有效时长
     *
     * @param key      键名
     * @param timeUnit 时间单位
     */
    Long getTimeout(String key, TimeUnit timeUnit);

    /**
     * 设置缓存有效时长
     *
     * @param key     键名
     * @param timeout 有效时长
     */
    void setTimeout(String key, long timeout);

    /**
     * 设置缓存有效时长
     *
     * @param key      键名
     * @param timeout  有效时长
     * @param timeUnit 时间单位
     */
    void setTimeout(String key, long timeout, TimeUnit timeUnit);

    /**
     * 批量设置缓存有效时长
     *
     * @param keys    键名集合
     * @param timeout 有效时长
     */
    void setTimeout(Collection<String> keys, long timeout);

    /**
     * 批量设置缓存有效时长
     *
     * @param keys     键名集合
     * @param timeout  有效时长
     * @param timeUnit 时间单位
     */
    void setTimeout(Collection<String> keys, long timeout, TimeUnit timeUnit);

    /**
     * 设置缓存过期日期
     *
     * @param key  键名
     * @param date 过期日期
     */
    void setExpireAt(String key, Date date);

    /**
     * 批量设置缓存过期日期
     *
     * @param keys 键名集合
     * @param date 过期日期
     */
    void setExpireAt(Collection<String> keys, Date date);

    /**
     * 设置缓存永久有效
     *
     * @param key 键名
     */
    void setPersist(String key);

    /**
     * 批量设置缓存永久有效
     *
     * @param keys 键名
     */
    void setPersist(Collection<String> keys);

    /**
     * 返回缓存键名集合
     */
    Set<String> getKeySet();
}
