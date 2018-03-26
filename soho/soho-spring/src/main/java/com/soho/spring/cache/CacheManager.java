package com.soho.spring.cache;

/**
 * 缓存管理器接口
 *
 * @author shadow
 */
public interface CacheManager {

    public static final String DEFAULT_CACHE = "_default_cache_";

    public static final String SESSION_CACHE = "_session_cache_";

    public static final String SHIRO_DATA_CACHE = "_shiro_data_cache_";

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
