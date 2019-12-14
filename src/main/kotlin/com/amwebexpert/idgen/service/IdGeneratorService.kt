package com.amwebexpert.idgen.service

import com.amwebexpert.idgen.domain.Namespace
import com.amwebexpert.idgen.domain.NamespaceIdentifier
import com.amwebexpert.idgen.repository.NamespaceIdRepository
import com.amwebexpert.idgen.repository.NamespaceRepository
import org.slf4j.LoggerFactory
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import javax.transaction.Transactional


@Service
class IdGeneratorService(
        private val namespaceRepository: NamespaceRepository,
        private val namespaceIdRepository: NamespaceIdRepository
) {

    companion object {
        private val LOGGER = LoggerFactory.getLogger(IdGeneratorService::class.java)
    }

    @Transactional
    fun generateID(namespaceName: String): NamespaceIdentifier {
        LOGGER.debug("Generating id within [{}] namespace", namespaceName)

        val namespace = getOrCreate(namespaceName)
        val namespaceIdentifier = namespaceIdRepository.save(NamespaceIdentifier(null, namespace))
        namespace.identifiers.add(namespaceIdentifier)

        LOGGER.debug("Generated element with internal id: {}", namespaceIdentifier.id)
        return namespaceIdentifier
    }

    @Cacheable("namespaces")
    fun getOrCreate(name: String): Namespace {
        LOGGER.debug("Looking for [{}] namespace", name)
        val namespace = namespaceRepository.findByName(name)

        if (namespace.isPresent) {
            LOGGER.debug("Namespace [{}] already exists: no need to create.", name)
            return namespace.get()
        }

        LOGGER.debug("Generating [{}] namespace", name)
        return namespaceRepository.save(Namespace(null, name))
    }

}
