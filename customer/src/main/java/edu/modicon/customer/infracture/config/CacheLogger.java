package edu.modicon.customer.infracture.config;

import lombok.extern.slf4j.Slf4j;
import org.ehcache.event.CacheEvent;
import org.ehcache.event.CacheEventListener;

@Slf4j
public class CacheLogger implements CacheEventListener<Object, Object> {

    @Override
    public void onEvent(CacheEvent<? extends Object, ? extends Object> event) {
        log.info("Key: {} | EventType: {} | Old value: {} | New value: {}",
                event.getKey(), event.getType(), event.getOldValue(),
                event.getNewValue());
    }
}
