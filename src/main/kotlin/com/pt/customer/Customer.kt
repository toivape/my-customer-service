package com.pt.customer

import java.util.Date

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EntityListeners
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table
import javax.persistence.Temporal
import javax.persistence.TemporalType
import javax.persistence.Version
import javax.validation.constraints.Past
import javax.validation.constraints.Size

import org.hibernate.validator.constraints.Email
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener

@Entity
@EntityListeners(AuditingEntityListener::class) // Requires activation: Add @EnableJpaAuditing to application.
@Table(name = "PERSON")
class Customer {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID", nullable = false, updatable = false)
	var id:Long? = null

	@Version
	@Column(name = "VERSION", nullable = false)
	var version:Int? = null

	@Column(name = "CREATED", insertable = true, nullable = false, updatable = false)
	@CreatedDate
	var created:Date? = null

	@Column(name = "UPDATED", nullable = false)
	@LastModifiedDate
	var updated:Date? = null

	@Column(name = "FIRST_NAME")
	@Size(max=40)
	var firstName:String? = null

	@Column(name = "LAST_NAME")
	@Size(max=40)
	var lastName:String? = null

	@Column(name = "EMAIL")
	@Size(max=40)
	@Email
	var emailAddress:String? = null

	@Temporal(TemporalType.DATE)
	@Column(name = "BIRTH_DATE")
	@Past
	var birthDate:Date? = null

}