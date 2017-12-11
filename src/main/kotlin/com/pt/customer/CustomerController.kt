package com.pt.customer

import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.http.ResponseEntity
import org.springframework.http.HttpStatus
import org.springframework.validation.Errors
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import javax.validation.Valid
import org.springframework.context.MessageSource

@RestController
class CustomerController(val customerService:CustomerService, val messageSource:MessageSource){

    var log:Logger = LoggerFactory.getLogger(CustomerController::class.java)

    @GetMapping("/v1/customer/findByName/{name}")
    fun findByName(@PathVariable name:String) = customerService.findByName(name)
	
	@GetMapping("/v1/customer/findAll")
    fun findAll() = customerService.findAll()

    @GetMapping("/v1/customer/read/{id}")
    fun getCustomer(@PathVariable id:Long) = customerService.getCustomer(id)

    @DeleteMapping("/v1/customer/delete/{id}")
    fun deleteCustomer(@PathVariable id:Long){
        log.info("Delete customer $id")
        customerService.deleteCustomer(id);
    }

    @PostMapping("/v1/customer/create")
    fun create(@Valid @RequestBody customer:Customer, errors: Errors ) : ResponseEntity<*> {
        if (customer.id!=null) return ResponseUtil.teapotRequest("Will not create entity with id field set. Use update method to update an existing entity.")

        if (errors.hasErrors()){
            log.info("Validation failed for create: " + errors.allErrors)
            return ResponseUtil.badRequest(errors, messageSource);
        }

        return saveCustomer(customer)
    }

    @PostMapping("/v1/customer/update")
    fun update(@Valid @RequestBody customer:Customer, errors: Errors) : ResponseEntity<*>{
        if (customer.id==null) return ResponseUtil.teapotRequest("Will not update entity without id field set. Use create method to create a new entity.")

        if (errors.hasErrors()){
            log.info("Validation failed for update: " + errors.allErrors)
            return ResponseUtil.badRequest(errors, messageSource);
        }

        return saveCustomer(customer)
    }

    fun saveCustomer(customer:Customer):ResponseEntity<*>{
        val saved = customerService.saveCustomer(customer)
        return ResponseUtil.okRequest(saved)
    }
}