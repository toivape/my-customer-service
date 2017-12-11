package com.pt.customer

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CustomerRepo : JpaRepository<Customer, Long> {
	
	fun findFirst30ByLastNameStartingWithOrFirstNameStartingWithOrderByLastNameAscFirstNameAsc(lastName:String, firstName:String) : List<Customer>
	
}