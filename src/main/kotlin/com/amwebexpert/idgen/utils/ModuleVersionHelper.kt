package com.amwebexpert.idgen.utils

import com.amwebexpert.idgen.domain.AboutInfo
import com.jcabi.manifests.Manifests
import org.springframework.stereotype.Component

/**
 * @see com.amwebexpert.idgen.controller.AboutController
 */
@Component
class ModuleVersionHelper {

    fun getAboutInfo(moduleName: String): AboutInfo {
        return AboutInfo(
                Manifests.read("$moduleName-revision"),
                Manifests.read("$moduleName-buildTimestamp"),
                Manifests.read("$moduleName-finalName")
        )
    }

}
