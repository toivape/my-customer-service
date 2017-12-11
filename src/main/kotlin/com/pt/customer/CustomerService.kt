package com.pt.customer

import javax.transaction.Transactional

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class CustomerService{
	
	@Autowired
	private lateinit var customerRepo:CustomerRepo
	
	fun  saveCustomer(cust:Customer) = customerRepo.saveAndFlush(cust)

	fun getCustomer(id:Long) = customerRepo.findOne(id)
	
	fun findAll() = customerRepo.findAll()

	@Transactional
	fun deleteCustomer(id:Long){
		val c = customerRepo.findOne(id)
		if (c!=null) customerRepo.delete(c)
	}

	fun findByName(firstOrLastName : String) : List<Customer> {
		val name = max30chars(firstOrLastName)
		return customerRepo.findFirst30ByLastNameStartingWithOrFirstNameStartingWithOrderByLastNameAscFirstNameAsc(name, name);
	}
	
	fun max30chars(s:String) = if (s.length>30) s.substring(0, 30) else s
}