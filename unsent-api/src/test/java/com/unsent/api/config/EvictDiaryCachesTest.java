package com.unsent.api.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * {@link EvictDiaryCaches} is a composed annotation, so its evictions only fire
 * if Spring resolves cache operations through meta-annotations. This pins that down.
 */
@SpringBootTest(classes = {CacheConfig.class, EvictDiaryCachesTest.Fixture.class})
class EvictDiaryCachesTest {

    @Autowired
    CacheManager cacheManager;

    @Autowired
    Fixture fixture;

    @Test
    void evictsEveryDiaryCache() {
        String[] caches = {
                DiaryCacheNames.ALL_ENTRIES,
                DiaryCacheNames.ENTRY_BY_RECORD_ID,
                DiaryCacheNames.ENTRIES_BY_USER,
                DiaryCacheNames.COUNT_BY_USER,
                DiaryCacheNames.SEARCH
        };
        for (String cache : caches) {
            cacheManager.getCache(cache).put("k", "stale");
        }

        fixture.read();
        assertNotNull(cacheManager.getCache(DiaryCacheNames.ALL_ENTRIES).get("read"));

        fixture.write();

        for (String cache : caches) {
            assertNull(cacheManager.getCache(cache).get("k"), cache + " was not evicted");
        }
        assertNull(cacheManager.getCache(DiaryCacheNames.ALL_ENTRIES).get("read"));
    }

    @Component
    static class Fixture {

        @Cacheable(value = DiaryCacheNames.ALL_ENTRIES, key = "'read'")
        String read() {
            return "v";
        }

        @EvictDiaryCaches
        void write() {
        }
    }
}
