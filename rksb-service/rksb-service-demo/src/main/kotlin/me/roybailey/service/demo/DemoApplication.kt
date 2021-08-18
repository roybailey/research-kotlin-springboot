package me.roybailey.service.demo

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan

@SpringBootApplication
@ComponentScan(basePackages = ["me.roybailey.codegen.api"])
open class DemoApplication

fun main(args: Array<String>) {
	runApplication<DemoApplication>(*args)
}