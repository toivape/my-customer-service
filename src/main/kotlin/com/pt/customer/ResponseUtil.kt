package com.pt.customer

import org.springframework.context.MessageSource
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.Errors

import com.fasterxml.jackson.databind.ObjectMapper

object ResponseUtil {

    fun badRequest(errors: Errors, messageSource: MessageSource): ResponseEntity<*> {
        val errorMessages = getErrorMessages(errors, messageSource)
        val er = ErrorResponse(HttpStatus.BAD_REQUEST, "Bad request", errorMessages)
        return ResponseEntity(er, er.httpStatus)
    }

    fun badRequest(e: Exception): ResponseEntity<*> {
        val er = ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error", e.message?:"")
        return ResponseEntity(er, er.httpStatus)
    }

    fun okRequest(o: Any): ResponseEntity<*> {
        return ResponseEntity(asJsonString(o), HttpStatus.OK)
    }

    fun teapotRequest(message: String): ResponseEntity<*> {
        val er = ErrorResponse(HttpStatus.I_AM_A_TEAPOT, "I am a teapot", message)
        return ResponseEntity(er, er.httpStatus)
    }

    private fun getErrorMessages(errors: Errors, messageSource: MessageSource): MutableList<String> {
        val errorList = mutableListOf<String>()
        for (e in errors.allErrors) {
            val msg = messageSource.getMessage(e, null)
            errorList.add(msg)
        }
        return errorList
    }

    private fun asJsonString(obj: Any): String {
        val mapper = ObjectMapper()
        return mapper.writeValueAsString(obj)
    }
}
