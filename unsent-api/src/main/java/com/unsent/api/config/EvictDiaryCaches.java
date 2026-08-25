package com.unsent.api.config;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Clears every diary cache. Put this on any method that writes a diary entry,
 * otherwise reads keep serving the pre-write copy until the cache TTL expires.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Caching(evict = {
        @CacheEvict(value = DiaryCacheNames.ALL_ENTRIES, allEntries = true),
        @CacheEvict(value = DiaryCacheNames.ENTRY_BY_RECORD_ID, allEntries = true),
        @CacheEvict(value = DiaryCacheNames.ENTRIES_BY_USER, allEntries = true),
        @CacheEvict(value = DiaryCacheNames.COUNT_BY_USER, allEntries = true),
        @CacheEvict(value = DiaryCacheNames.SEARCH, allEntries = true)
})
public @interface EvictDiaryCaches {
}
