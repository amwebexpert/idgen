package com.amwebexpert.idgen.config


import com.google.common.base.Predicates
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import springfox.documentation.RequestHandler
import springfox.documentation.builders.ApiInfoBuilder
import springfox.documentation.builders.PathSelectors
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.service.ApiInfo
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger2.annotations.EnableSwagger2

/**
 * Optional: setup the Swagger-UI (would do that only on non-PROD instances)
 */
@Configuration
@EnableSwagger2
class SwaggerConfig {

    @Bean
    fun api(): Docket {
        return Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.any())
                .apis(Predicates.not<RequestHandler>(RequestHandlerSelectors.basePackage("org.springframework.boot")))
                .apis(Predicates.not<RequestHandler>(RequestHandlerSelectors.basePackage("org.springframework.cloud")))
                .apis(Predicates.not<RequestHandler>(RequestHandlerSelectors.basePackage("org.springframework.data.rest.webmvc")))
                .paths(PathSelectors.any())
                .build()
    }

    private fun apiInfo(): ApiInfo {
        return ApiInfoBuilder()
                .title("API of idgen")
                .description("Exposed API for the Namespace ID Generator application")
                .version("v1")
                .build()
    }
}