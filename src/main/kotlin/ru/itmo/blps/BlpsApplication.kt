package ru.itmo.blps

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class BlpsApplication

fun main(args: Array<String>) {
    runApplication<BlpsApplication>(*args)
}
