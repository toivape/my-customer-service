package com.pt.customer

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.web.ErrorAttributes
import org.springframework.boot.autoconfigure.web.ErrorController
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.context.request.ServletRequestAttributes

data class ErrorData(val status: Int, val error: String, val message: String, val timestamp: String, val path: String)

@RestController
class JsonErrorController : ErrorController {

    companion object {
        const val ERROR_PATH = "/error"
    }

    @Autowired
    lateinit private var errorAttributes: ErrorAttributes

    override fun getErrorPath(): String? {
        return ERROR_PATH
    }

    @RequestMapping(value = ERROR_PATH)
    fun showError(request: HttpServletRequest, response: HttpServletResponse): ErrorData {
        val requestAttributes = ServletRequestAttributes(request);
        val errorMap = errorAttributes.getErrorAttributes(requestAttributes, false);
        return ErrorData(response.status, errorMap["error"].toString(), errorMap["message"].toString(), errorMap["timestamp"].toString(), errorMap["path"].toString());
    }

}