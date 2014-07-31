package com.cisco.config;

import net.sf.ehcache.config.CacheConfiguration;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * User: Rost
 * Date: 31.07.2014
 * Time: 0:30
 */
@Configuration
@EnableCaching
public class CachingConfig implements CachingConfigurer {
    @Bean(destroyMethod = "shutdown")
    public net.sf.ehcache.CacheManager ehCacheManager() {
        CacheConfiguration ciscoCacheConfiguration = new CacheConfiguration();
        ciscoCacheConfiguration.setName("ciscoCache");
        ciscoCacheConfiguration.setMemoryStoreEvictionPolicy("LRU");
        ciscoCacheConfiguration.setMaxEntriesLocalHeap(100);

        CacheConfiguration salesCacheConfiguration = new CacheConfiguration();
        salesCacheConfiguration.setName("salesCache");
        salesCacheConfiguration.setMemoryStoreEvictionPolicy("LRU");
        salesCacheConfiguration.setMaxEntriesLocalHeap(10);


        net.sf.ehcache.config.Configuration config = new net.sf.ehcache.config.Configuration();
        config.addCache(ciscoCacheConfiguration);
        config.addCache(salesCacheConfiguration);

        return net.sf.ehcache.CacheManager.newInstance(config);
    }

    @Bean
    @Override
    public CacheManager cacheManager() {
        return new EhCacheCacheManager(ehCacheManager());
    }

    @Bean
    @Override
    public KeyGenerator keyGenerator() {
        return null;
    }
}
