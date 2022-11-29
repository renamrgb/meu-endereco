package com.github.renamrgb.meuendereco.cep.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class LocalCacheService {

    private static final String VALUE_FIELD = "value";
    private static final String EXPIRE_FIELD = "expire";

    private static final Map<String, Map<String, Object>> CACHE = new HashMap<>();

    @Value("${app-config.cache.ttl}")
    private Integer ttl;

    public Mono<String> save(String key, String value) {
        try {
            if (!exists(key)) {
                Map<String, Object> data = new HashMap<>();
                data.put(VALUE_FIELD, value);
                data.put(EXPIRE_FIELD, LocalDateTime.now().plusSeconds(ttl));
                CACHE.put(key, data);
                log.info("#LocalCacheService #save Salvando cache para chave {}", key);
            }
        } catch (Exception e) {
            log.error("#LocalCacheService #save Erro ao tentar salvar o cache para chave {}", key);
        }

        return Mono.just(value);
    }

    public Mono<String> get(String key) {
        try {
            if (exists(key)) {
                log.info("#LocalCacheService #get Cahce existente para chave {}", key);
                Map<String, Object> data = CACHE.get(key);
                return Mono.just((String) data.get(VALUE_FIELD));
            }
            log.info("#LocalCacheService #get Cahce não encontrado para chave {}", key);
        } catch (Exception e) {
            log.error("#LocalCacheService #get Erro ao tentar salvar o cache para chave {}", key);
        }
        return Mono.empty();
    }

    public Mono<Boolean> existsForKey(String key) {
        return Mono.just(exists(key));
    }

    private Boolean exists(String key) {
        boolean exists = CACHE.containsKey(key);
        if (exists && isExpired((LocalDateTime) CACHE.get(key).get(EXPIRE_FIELD))) {
            remove(key);
            return false;
        }
        return exists;
    }

    private boolean isExpired(LocalDateTime expiration) {
        return LocalDateTime.now().isAfter(expiration);
    }

    private void remove(String key) {
        CACHE.remove(key);
    }

    public void removeExpiredKeys() {
        List<String> keysToRemove = CACHE
                .keySet()
                .stream()
                .filter(key -> isExpired((LocalDateTime) CACHE.get(key).get(EXPIRE_FIELD)))
                .toList();

        keysToRemove
                .forEach(CACHE::remove);

        if(!keysToRemove.isEmpty()) {
            log.info("#LocalCacheService #removeExpiredKeys {} chaves removidas", keysToRemove.size());
        }
    }
}
