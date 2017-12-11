package com.pt.customer

import java.util.ArrayList

import org.springframework.http.HttpStatus

class ErrorResponse {
    var httpStatus: HttpStatus? = null
    var message: String? = null
    private var errors: MutableList<String> = ArrayList()

    constructor(httpStatus: HttpStatus, message: String, errors: MutableList<String>) {
        this.httpStatus = httpStatus
        this.message = message
        this.errors = errors
    }

    constructor(httpStatus: HttpStatus, message: String, error: String) {
        this.httpStatus = httpStatus
        this.message = message
        addError(error)
    }

    fun addError(error: String) {
        errors.add(error)
    }

    fun getErrors(): List<String> {
        return errors
    }

    fun setErrors(errors: MutableList<String>) {
        this.errors = errors
    }
}
