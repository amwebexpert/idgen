package com.amwebexpert.idgen.service

import com.amwebexpert.idgen.domain.Namespace
import com.amwebexpert.idgen.domain.NamespaceIdentifier
import com.amwebexpert.idgen.repository.NamespaceIdRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import javax.transaction.Transactional


@Service
class IdGeneratorService(
        private val namespaceService: NamespaceService,
        private val namespaceIdRepository: NamespaceIdRepository
) {

    companion object {
        private val LOGGER = LoggerFactory.getLogger(IdGeneratorService::class.java)
    }

    fun findAllNamespaces(): List<Namespace> =
            namespaceService.findAll()

    fun findAllIdentifiers(): List<NamespaceIdentifier> =
            namespaceIdRepository.findAll()

    @Transactional
    fun generateID(namespaceName: String): NamespaceIdentifier {
        LOGGER.debug("Generating id within [${namespaceName}] namespace")

        val namespace = namespaceService.getOrCreate(namespaceName)
        val namespaceIdentifier = namespaceIdRepository.save(NamespaceIdentifier(null, namespace))
        namespace.identifiers.add(namespaceIdentifier)

        LOGGER.debug("Generated element with internal id: {}", namespaceIdentifier.id)
        return namespaceIdentifier;
    }

}
