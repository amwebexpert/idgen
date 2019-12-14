package com.amwebexpert.idgen.controller

import com.amwebexpert.idgen.dto.AboutInfo
import com.amwebexpert.idgen.utils.ModuleVersionHelper
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import org.slf4j.LoggerFactory
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * Optional: setup an about app info so we can ping our REST api
 */
@RestController
@RequestMapping("/api/v1")
@Api(tags = ["idgen-about"], description = "Controller for REST application version details")
class AboutController(private val moduleVersionHelper: ModuleVersionHelper) {

    companion object {
        private val LOGGER = LoggerFactory.getLogger(AboutController::class.java)
    }

    @GetMapping(value = ["/about"], produces = [MediaType.APPLICATION_JSON_VALUE])
    @ApiOperation(value = "about", notes = "Return REST application version details")
    fun about(): ResponseEntity<AboutInfo> {
        val info = moduleVersionHelper.getAboutInfo("idgen")
        LOGGER.info("ModuleVersion {}", info.revision)

        return ResponseEntity.ok(info)
    }

}
