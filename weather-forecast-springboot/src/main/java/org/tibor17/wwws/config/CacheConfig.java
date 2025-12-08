package org.tibor17.wwws.config;

import org.ehcache.config.CacheConfiguration;
import org.ehcache.core.config.DefaultConfiguration;
import org.ehcache.expiry.ExpiryPolicy;
import org.ehcache.impl.config.event.DefaultCacheEventListenerConfiguration;
import org.ehcache.impl.config.persistence.DefaultPersistenceConfiguration;
import org.ehcache.jsr107.EhcacheCachingProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.jcache.JCacheCacheManager;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ReflectionUtils.MethodFilter;
import org.tibor17.wwws.util.EHCacheEventListener;

import java.io.File;
import java.lang.reflect.ParameterizedType;
import java.time.Duration;
import java.util.*;

import static java.lang.reflect.Modifier.isPublic;
import static java.time.Duration.ofSeconds;
import static java.util.Collections.addAll;
import static java.util.Set.of;
import static javax.cache.Caching.getCachingProvider;
import static org.ehcache.config.builders.CacheConfigurationBuilder.newCacheConfigurationBuilder;
import static org.ehcache.config.builders.ExpiryPolicyBuilder.expiry;
import static org.ehcache.config.builders.ResourcePoolsBuilder.heap;
import static org.ehcache.config.units.MemoryUnit.valueOf;
import static org.ehcache.event.EventFiring.SYNCHRONOUS;
import static org.ehcache.event.EventOrdering.ORDERED;
import static org.ehcache.event.EventType.*;
import static org.springframework.util.ReflectionUtils.USER_DECLARED_METHODS;
import static org.springframework.util.ReflectionUtils.doWithMethods;

@Configuration
@EnableCaching
public class CacheConfig {
    private static final MethodFilter CACHEABLE_DECLARED_METHODS = m ->
            isPublic(m.getModifiers())
                    && !m.getReturnType().equals(Void.class)
                    && m.isAnnotationPresent(Cacheable.class);

    @Autowired
    private ApplicationContext ctx;

    @Value("${cache.heap.entries}")
    private int heapEntries;

    @Value("${cache.persistence.size}")
    private int persistenceSize;

    @Value("${cache.persistence.unit}")
    private String persistenceUnit;

    @Value("${cache.persistence.path}")
    private File diskStore;

    @Value("${cache.expiry.ttl.seconds}")
    private int ttlSeconds;

    @Value("${cache.expiry.idle.seconds}")
    private int idleSeconds;

    @Bean
    public CacheManager jCacheCacheManager() {
        Map<Class<?>, Collection<String>> cacheMethods = new HashMap<>();
        for (var beanName : ctx.getBeanDefinitionNames()) {
            var beanType = ctx.getType(beanName, false);
            if (!beanType.isSynthetic() && beanType.getPackageName().startsWith("org.tibor17.wwws")) {
                doWithMethods(beanType, m -> {
                    var returnType = m.getReturnType();
                    if (returnType.equals(Optional.class)) {
                        var generic = ((ParameterizedType) m.getGenericReturnType()).getActualTypeArguments();
                        returnType = generic.length == 1 && generic[0] instanceof Class<?> ? (Class<?>) generic[0] : null;
                    }

                    if (returnType != null) {
                        cacheMethods.compute(returnType, (k, v) -> {
                            if (v == null) v = new HashSet<>();
                            addAll(v, m.getAnnotation(Cacheable.class).cacheNames());
                            return v;
                        });
                    }
                }, m -> CACHEABLE_DECLARED_METHODS.and(USER_DECLARED_METHODS).matches(m));
            }
        }

        Map<String, CacheConfiguration<?, ?>> cacheMap = new HashMap<>();

        var resourcePoolsBuilder = heap(heapEntries)
                .disk(persistenceSize, valueOf(persistenceUnit), true);

        var expiryPolicy =
                createExpiryPolicy(ofSeconds(ttlSeconds), ofSeconds(idleSeconds));

        var listenerEvents = of(CREATED, EVICTED, EXPIRED, REMOVED, UPDATED);
        var listenerConfig = new DefaultCacheEventListenerConfiguration(listenerEvents, EHCacheEventListener.class);
        listenerConfig.setEventOrderingMode(ORDERED);
        listenerConfig.setEventFiringMode(SYNCHRONOUS);

        cacheMethods.forEach((returnType, cacheNames) -> {
            var cacheConfiguration =
                    newCacheConfigurationBuilder(String.class, returnType, resourcePoolsBuilder)
                            .withExpiry(expiryPolicy)
                            .withService(listenerConfig)
                            .build();

            cacheNames.forEach(cacheName -> cacheMap.put(cacheName, cacheConfiguration));
        });

        var ehcacheCachingProvider =
                (EhcacheCachingProvider) getCachingProvider(EhcacheCachingProvider.class.getName());

        var persistenceConfig = new DefaultPersistenceConfiguration(diskStore);

        var defaultConfiguration =
                new DefaultConfiguration(cacheMap, ehcacheCachingProvider.getDefaultClassLoader(), persistenceConfig);

        var cacheManager =
                ehcacheCachingProvider.getCacheManager(ehcacheCachingProvider.getDefaultURI(), defaultConfiguration);

        var ehcacheManager = new JCacheCacheManager(cacheManager);
        ehcacheManager.setTransactionAware(true);
        return ehcacheManager;
    }

    private static ExpiryPolicy<Object, Object> createExpiryPolicy(Duration timeToLive, Duration timeToIdle) {
        return expiry()
                .create(timeToLive)
                .update(timeToLive)
                .access(timeToIdle)
                .build();
    }
}