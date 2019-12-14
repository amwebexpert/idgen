package com.amwebexpert.idgen.utils

import org.ehcache.event.CacheEvent
import org.ehcache.event.CacheEventListener
import org.slf4j.LoggerFactory

/**
 * Optional: allows to see cache in action from the server logs
 */
class CacheEventLogger : CacheEventListener<Object, Object> {

    companion object {
        private val LOGGER = LoggerFactory.getLogger(CacheEventLogger::class.java)
    }

    override fun onEvent(cacheEvent: CacheEvent<out Object, out Object>) {
        LOGGER.debug("{}, {} ==> {}", cacheEvent.key, cacheEvent.oldValue, cacheEvent.newValue)
    }
}