package com.amwebexpert.idgen.utils

import org.slf4j.LoggerFactory
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler


@ControllerAdvice
class RestResponseEntityExceptionHandler : ResponseEntityExceptionHandler() {

    companion object {
        private val LOGGER = LoggerFactory.getLogger(RestResponseEntityExceptionHandler::class.java)
        private const val NAMESPACE_ALREADY_EXISTS = "Namespace already exists"
        private const val SERVER_ERROR = "Server error"
    }

    @ExceptionHandler(value = [DataIntegrityViolationException::class])
    protected fun handleDataIntegrityViolation(ex: RuntimeException, request: WebRequest): ResponseEntity<Any> {
        return handleExceptionInternal(ex, NAMESPACE_ALREADY_EXISTS, HttpHeaders(), HttpStatus.CONFLICT, request)
    }

    @ExceptionHandler(value = [Exception::class])
    protected fun handleServerError(ex: RuntimeException, request: WebRequest): ResponseEntity<Any> {
        LOGGER.error(SERVER_ERROR, ex)
        return handleExceptionInternal(ex, SERVER_ERROR, HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request)
    }

}