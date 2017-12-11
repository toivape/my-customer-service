package com.pt.customer

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@SpringBootApplication
@EnableJpaAuditing
class MyCustomerServiceApplication

fun main(args: Array<String>) {
    SpringApplication.run(MyCustomerServiceApplication::class.java, *args)
}
