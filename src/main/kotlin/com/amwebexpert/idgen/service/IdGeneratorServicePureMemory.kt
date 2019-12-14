package com.amwebexpert.idgen.service

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.util.concurrent.ConcurrentHashMap

/**
 * Optional, but this pure in-memory volatile implementation allows to compare performances statistics with the H2 approach.
 */
@Service
class IdGeneratorServicePureMemory() {
    companion object {
        private val LOGGER = LoggerFactory.getLogger(IdGeneratorServicePureMemory::class.java)
    }

    val identifiers = ConcurrentHashMap<String, Long>()

    fun generateID(namespaceName: String): String {
        LOGGER.debug("Generating id within [{}] namespace", namespaceName)

        val id = identifiers.getOrPut(namespaceName, { 0L }).inc()
        identifiers[namespaceName] = id

        return "$namespaceName-$id"
    }

}
