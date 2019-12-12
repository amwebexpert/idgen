package com.amwebexpert.idgen.config

import org.h2.server.web.WebServlet
import org.springframework.stereotype.Component
import org.springframework.web.WebApplicationInitializer
import javax.servlet.ServletContext

/**
 * Optional: setup the h2-console
 */
@Component
class PersistenceH2ConsoleExposer : WebApplicationInitializer {

    override fun onStartup(servletContext: ServletContext) {
        val servlet = servletContext.addServlet("h2-console", WebServlet())

        servlet.setLoadOnStartup(2)
        servlet.setInitParameter("-webAllowOthers", "true")
        servlet.addMapping("/h2-console/*")
    }

}