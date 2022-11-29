package com.github.renamrgb.meuendereco.cep.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class CacheScheduler {

    private final LocalCacheService localCacheService;

    public CacheScheduler(LocalCacheService localCacheService) {
        this.localCacheService = localCacheService;
    }

    @Scheduled(cron = "${app-config.cache.scheduler}")
    public void removeExpiredKeys() {
        localCacheService.removeExpiredKeys();
    }
}
