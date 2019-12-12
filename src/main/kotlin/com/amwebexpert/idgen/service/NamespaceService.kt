package com.amwebexpert.idgen.service

import com.amwebexpert.idgen.domain.Namespace
import com.amwebexpert.idgen.repository.NamespaceRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import javax.transaction.Transactional


@Service
class NamespaceService(
        private val namespaceRepository: NamespaceRepository
) {

    companion object {
        private val LOGGER = LoggerFactory.getLogger(NamespaceService::class.java)
    }

    fun findAll() = namespaceRepository.findAll()

    @Transactional
    fun getOrCreate(name: String): Namespace {
        LOGGER.debug("Looking for [${name}] namespace")
        val namespace = namespaceRepository.findByName(name)

        if (namespace.isPresent) {
            LOGGER.debug("Namespace [${name}] already exists: no need to create.")
            return namespace.get()
        }

        LOGGER.debug("Generating [${name}] namespace")
        return namespaceRepository.save(Namespace(null, name))
    }

}
