package com.amwebexpert.idgen.controller

import com.amwebexpert.idgen.repository.NamespaceIdRepository
import com.amwebexpert.idgen.repository.NamespaceRepository
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

/**
 * Optional, just to test JPA repository.
 *
 * Notes:
 * - Normally findAllXXXXX methods should return paged results (@see org.springframework.data.domain.Pageable) instead of
 *   whole collections to avoid server memory issues
 */

@RestController
@RequestMapping("/api/v1")
@Api(tags = ["idgen-queries"], description = "Controller for identifiers queries")
class QueriesController(
        private val namespaceRepository: NamespaceRepository,
        private val namespaceIdRepository: NamespaceIdRepository
) {

    companion object {
        private val LOGGER = LoggerFactory.getLogger(QueriesController::class.java)
    }

    @RequestMapping(value = ["findAllByNamespaceName/{namespaceName}"], method = [RequestMethod.GET])
    @ApiOperation(value = "findAllByNamespaceName", notes = "Search all IDs of the given namespace by using the lazy collection attribute of namespace")
    fun findAllByNamespaceName(@PathVariable namespaceName: String): ResponseEntity<String> {
        val namespace = namespaceRepository.findByName(namespaceName).get()
        val values = namespace.identifiers

        LOGGER.debug("Returning values {}", values)
        return ResponseEntity(values.toString(), HttpStatus.OK)
    }

    @RequestMapping(value = ["findAllByNamespaceObject/{namespaceName}"], method = [RequestMethod.GET])
    @ApiOperation(value = "findAllByNamespaceObject", notes = "Search all IDs of the given namespace by using the Namespace object as argument of IDs repo")
    fun findAllByNamespaceObject(@PathVariable namespaceName: String): ResponseEntity<String> {
        val namespace = namespaceRepository.findByName(namespaceName).get()
        val values = namespaceIdRepository.findAllByNamespace(namespace)

        LOGGER.debug("Returning values {}", values)
        return ResponseEntity(values.toString(), HttpStatus.OK)
    }

    @RequestMapping(value = ["findNamespaceByName/{namespaceName}"], method = [RequestMethod.GET])
    @ApiOperation(value = "findNamespaceByName", notes = "Search the Namespace by name")
    fun findNamespaceByName(@PathVariable namespaceName: String): ResponseEntity<String> {
        val namespace = namespaceRepository.findByName(namespaceName).get()

        LOGGER.debug("Returning namespace {}", namespace)
        return ResponseEntity(namespace.name, HttpStatus.OK)
    }

}
