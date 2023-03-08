package edu.modicon.customer.infracture.config;

import edu.modicon.customer.domain.model.Customer;
import edu.modicon.customer.infracture.config.CacheLogger;
import org.ehcache.clustered.client.config.builders.ClusteredResourcePoolBuilder;
import org.ehcache.clustered.client.config.builders.ClusteringServiceConfigurationBuilder;
import org.ehcache.clustered.client.config.builders.ServerSideConfigurationBuilder;
import org.ehcache.config.CacheConfiguration;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheEventListenerConfigurationBuilder;
import org.ehcache.config.builders.ExpiryPolicyBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.config.units.MemoryUnit;
import org.ehcache.core.config.DefaultConfiguration;
import org.ehcache.event.EventType;
import org.ehcache.jsr107.EhcacheCachingProvider;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.jcache.JCacheCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.cache.CacheManager;
import javax.cache.Caching;
import java.net.URI;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import static java.net.URI.create;

/**
 * Cache config repo: <a href="https://github.com/ehcache/ehcache3-samples/blob/master/fullstack/src/main/java/org/ehcache/sample/config/CacheConfiguration.java">...</a>
 */
@EnableCaching
@Configuration
public class CacheConfig extends CachingConfigurerSupport {

    private static final long IN_MEMORY_HEAP_ENTRIES = 10;
    private static final long IN_MEMORY_OFFHEAP_SIZE = 10;
    private static final MemoryUnit IN_MEMORY_OFFHEAP_UNIT = MemoryUnit.MB;
    private static final long IN_MEMORY_EXPIRE_SECONDS = 30;

    private static final URI CLUSTERED_SERVER_URI = create("terracotta://localhost:9410/clustered");
    private static final String CLUSTERED_RESOURCE_NAME = "default-resource";
    private static final long CLUSTERED_HEAP_SIZE = 10;
    private static final MemoryUnit CLUSTERED_HEAP_UNIT = MemoryUnit.MB;
    private static final long CLUSTERED_RESOURCE_SIZE = 512;
    private static final MemoryUnit CLUSTERED_RESOURCE_UNIT = MemoryUnit.MB;
    private static final long CLUSTERED_EXPIRE_SECONDS = 30;

    private ClassLoader getClassLoader() {
        return this.getClass().getClassLoader();
    }

    private CacheManager getCacheManager(EhcacheCachingProvider provider, DefaultConfiguration configuration) {
        return provider.getCacheManager(provider.getDefaultURI(), configuration);
    }

    @Bean
    @Override
    public org.springframework.cache.CacheManager cacheManager() {
        String profile = "dev";
        return new JCacheCacheManager(profile.equals("prod") ?
                createClusteredCacheManager() : createInMemoryCacheManager());
    }

    private CacheManager createInMemoryCacheManager() {
        org.ehcache.config.CacheConfiguration<Object, Object> cacheConfiguration = CacheConfigurationBuilder
                .newCacheConfigurationBuilder(Object.class, Object.class, ResourcePoolsBuilder
                        .heap(IN_MEMORY_HEAP_ENTRIES)
                        .offheap(IN_MEMORY_OFFHEAP_SIZE, IN_MEMORY_OFFHEAP_UNIT))
                .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(IN_MEMORY_EXPIRE_SECONDS)))
                .withService(getCacheEventListenerConfigurationBuilder())
                .build();

        Map<String, org.ehcache.config.CacheConfiguration<?, ?>> caches = createCacheConfigurations(cacheConfiguration);

        EhcacheCachingProvider provider = getCachingProvider();
        DefaultConfiguration configuration = new DefaultConfiguration(caches, getClassLoader());
        return getCacheManager(provider, configuration);
    }

    @Bean
    public CacheManager createClusteredCacheManager() {
        ClusteringServiceConfigurationBuilder clusteringServiceConfigurationBuilder = ClusteringServiceConfigurationBuilder.cluster(CLUSTERED_SERVER_URI);
        ServerSideConfigurationBuilder serverSideConfigurationBuilder = (clusteringServiceConfigurationBuilder.autoCreate())
                .defaultServerResource(CLUSTERED_RESOURCE_NAME);

        CacheConfiguration<Object, Object> cacheConfig = CacheConfigurationBuilder
                .newCacheConfigurationBuilder(
                        Object.class,
                        Object.class,
                        ResourcePoolsBuilder.newResourcePoolsBuilder()
                                .heap(CLUSTERED_HEAP_SIZE, CLUSTERED_HEAP_UNIT)
                                .with(ClusteredResourcePoolBuilder.clusteredDedicated(CLUSTERED_RESOURCE_SIZE, CLUSTERED_RESOURCE_UNIT))
                                .build())
                .withExpiry(ExpiryPolicyBuilder.timeToIdleExpiration(Duration.ofSeconds(CLUSTERED_EXPIRE_SECONDS
                )))
                .withService(getCacheEventListenerConfigurationBuilder())
                .build();

        Map<String, CacheConfiguration<?, ?>> caches = createCacheConfigurations(cacheConfig);

        EhcacheCachingProvider provider = getCachingProvider();
        DefaultConfiguration configuration = new DefaultConfiguration(caches, this.getClass().getClassLoader(), serverSideConfigurationBuilder.build());

        return provider.getCacheManager(provider.getDefaultURI(), configuration);
    }

    private Map<String, org.ehcache.config.CacheConfiguration<?, ?>> createCacheConfigurations(org.ehcache.config.CacheConfiguration<Object, Object> cacheConfiguration) {
        Map<String, org.ehcache.config.CacheConfiguration<?, ?>> caches = new HashMap<>();
        caches.put(Customer.class.getName(), cacheConfiguration);
        return caches;
    }

    private CacheEventListenerConfigurationBuilder getCacheEventListenerConfigurationBuilder() {
        return CacheEventListenerConfigurationBuilder
                .newEventListenerConfiguration(new CacheLogger(), EventType.CREATED, EventType.UPDATED)
                .unordered().asynchronous();
    }

    private EhcacheCachingProvider getCachingProvider() {
        return (EhcacheCachingProvider) Caching.getCachingProvider();
    }

}
