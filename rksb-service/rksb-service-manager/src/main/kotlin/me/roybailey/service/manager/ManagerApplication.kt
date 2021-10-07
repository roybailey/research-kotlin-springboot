package me.roybailey.service.manager

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication


@SpringBootApplication(scanBasePackages = ["me.roybailey.service.manager", "me.roybailey.api", "me.roybailey.codegen"])
open class ManagerApplication


fun main(args: Array<String>) {
    runApplication<ManagerApplication>(*args)
}
