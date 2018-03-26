package com.soho.spring.cache;

import java.util.Collection;
import java.util.Set;

/**
 * 缓存接口定义
 *
 * @author shadow
 */
public interface Cache {

    /**
     * 获取缓存值
     *
     * @param key 缓存键
     * @return 缓存值
     */
    public abstract <V> V get(Object key);

    /**
     * 存放值到缓存
     *
     * @param key   缓存键
     * @param value 缓存值
     * @param exp   失效时间(单位/秒)
     * @return 缓存值/成功/失败
     * @
     */
    public abstract <V> boolean put(Object key, V value, int exp);

    /**
     * 存放值到缓存
     *
     * @param key   缓存键
     * @param value 缓存值
     * @return 缓存值/成功/失败
     * @
     */
    public abstract <V> boolean put(Object key, V value);

    /**
     * 删除缓存值
     *
     * @param key 缓存键
     * @return 缓存值/成功/失败
     * @
     */
    public abstract boolean remove(Object key);

    /**
     * 清空缓存所有值
     *
     * @
     */
    public abstract void clear();

    /**
     * 获取有多少个缓存键值
     *
     * @return 键值对数
     * @
     */
    public abstract long size();

    /**
     * 获取所有缓存键集合
     *
     * @return 缓存键集合
     * @
     */
    public abstract Set<Object> keys();

    /**
     * 获取缓存值集合
     *
     * @return 缓存值集合
     * @
     */
    public abstract <V> Collection<V> values();

    /**
     * 获取底层实例
     *
     * @return Object
     */
    public abstract Object getInstance();

    /**
     * 获取底层实例类型
     *
     * @return Class<?>
     */
    public abstract Class<?> getInstanceClassType();

}
