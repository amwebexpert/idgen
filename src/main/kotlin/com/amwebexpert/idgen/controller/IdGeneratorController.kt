package com.amwebexpert.idgen.controller

import com.amwebexpert.idgen.service.IdGeneratorService
import io.swagger.annotations.Api
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/api/v1")
@Api(tags = ["idgen"])
class IdGeneratorController(private val service: IdGeneratorService) {

    companion object {
        private val LOGGER = LoggerFactory.getLogger(IdGeneratorController::class.java)
    }

    @RequestMapping(value = ["new-id/{namespaceName}"], method = [RequestMethod.GET])
    fun generate(@PathVariable namespaceName: String): ResponseEntity<String> {
        val namespace = service.generateID(namespaceName)
        val id = namespace.fullyQualifiedIdentifier()

        LOGGER.info("Returning generated id", id)
        return ResponseEntity(id, HttpStatus.OK)
    }

}
