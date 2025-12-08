package org.tibor17.wwws.util;

import org.ehcache.event.CacheEvent;
import org.ehcache.event.CacheEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.tibor17.wwws.util.OptimalWeatherUtil.maskAuthKey;

public class EHCacheEventListener implements CacheEventListener<Object, Object> {
    private static final Logger LOG = LoggerFactory.getLogger(EHCacheEventListener.class);

    @Override
    public void onEvent(CacheEvent<?, ?> event) {
        LOG.info("EHCache - Event type: {} | Key: {} | Old value: {} | New value: {}",
                event.getType(),
                event.getKey(),
                "auth-key".equals(event.getKey()) ? maskAuthKey((String) event.getOldValue()) : event.getOldValue(),
                "auth-key".equals(event.getKey()) ? maskAuthKey((String) event.getNewValue()) : event.getNewValue());
    }
}
