package com.soho.spring.cache;

/**
 * 缓存管理器接口
 *
 * @author shadow
 */
public interface CacheManager {

    /**
     * 获取缓存堆
     *
     * @param cacheName 缓存名称
     * @return 缓存对象
     */
    public abstract Cache getCache(String cacheName);

    /**
     * 注销管理器
     */
    public abstract void destroy();

}
