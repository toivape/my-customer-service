package com.pt.customer

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.JUnitSoftAssertions
import org.junit.After
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.junit4.SpringRunner
import java.text.SimpleDateFormat
import javax.validation.ConstraintViolationException

@RunWith(SpringRunner::class)
@SpringBootTest
@TestPropertySource(value = "classpath:application-test.properties")
class CustomerServiceTest {

	@Autowired
	private lateinit var repo: CustomerRepo
	
	@Autowired
	private lateinit var service: CustomerService

	@get:Rule
	var softly = JUnitSoftAssertions()

    @get:Rule
    var thrown = ExpectedException.none();

	@After
	fun cleanUp() {
		repo.deleteAll();
	}

	private fun asDate(dateString: String) = SimpleDateFormat("yyyy-MM-dd").parse(dateString)

	private fun saveCustomer(firstName: String, lastName: String, emailAddress: String, birthDateString: String): Long {
		val c = Customer()
		c.firstName = firstName
		c.lastName = lastName
		c.emailAddress = emailAddress
		c.birthDate = asDate(birthDateString);
		return repo.saveAndFlush(c).id ?: -1
	}

	@Test
	fun `Saved customer must be found by id`() {
		val savedId = saveCustomer("frank", "test", "frank@test.com", "1992-01-01")
		val found = service.getCustomer(savedId)
		assertThat(found.id).isEqualTo(savedId)
	}

	@Test
	fun `All fields of saved customer are set`() {
		val savedId = saveCustomer("frank", "test", "frank@test.com", "1992-01-01")
		val found = service.getCustomer(savedId)
		softly.assertThat(found.firstName).isEqualTo("frank")
		softly.assertThat(found.lastName).isEqualTo("test")
		softly.assertThat(found.emailAddress).isEqualTo("frank@test.com")
		softly.assertThat(found.birthDate).isEqualTo(asDate("1992-01-01"))
		softly.assertThat(found.id).isNotNull
		softly.assertThat(found.version).isEqualTo(0)
		softly.assertThat(found.created).isNotNull
		softly.assertThat(found.updated).isNotNull
	}

    @Test
    fun birthDateInFutureFails() {
        thrown.expect(ConstraintViolationException::class.java)
        thrown.expectMessage("must be in the past")
        saveCustomer("first", "last", "first.last@gmail.com","2020-01-23")
    }

    @Test
    fun invalidEmailFails() {
        thrown.expect(ConstraintViolationException::class.java)
        thrown.expectMessage("not a well-formed email address")
        saveCustomer("first", "last", "first.last@@gmail.com", "1998-01-23")
    }

    @Test
    fun `Deleted customer is no longer found`(){
        val custId = saveCustomer("first", "last", "first.last@gmail.com","1998-01-23")
        service.deleteCustomer(custId)
        assertThat(repo.findOne(custId)).isNull()
    }

    @Test
    fun `Try to delete already deleted customer`(){
        val custId = saveCustomer("first", "last", "first.last@gmail.com","1998-01-23")
        service.deleteCustomer(custId)
        assertThat(repo.findOne(custId)).isNull()
        service.deleteCustomer(custId)
    }

    @Test
    fun `Find by last name`(){
        saveCustomer("frank", "good", "frank.good@gmail.com","1998-01-23")
        saveCustomer("joe", "black", "joe.blak@gmail.com","1996-06-06")
        val found = service.findByName("black")
        assertThat(found[0].firstName).isEqualTo("joe")
    }

    @Test
    fun `Find by first name`(){
        saveCustomer("frank", "good", "frank.good@gmail.com","1998-01-23")
        saveCustomer("joe", "black", "joe.black@gmail.com","1996-06-06")
        val found = service.findByName("joe")
        assertThat(found[0].lastName).isEqualTo("black")
    }

    @Test
    fun `Limit string to 30 characters`(){
        val s40 = "123456789012345678901234567890"
        val s30 = service.max30chars(s40)
        assertThat(s30.length).isEqualTo(30)
    }

}