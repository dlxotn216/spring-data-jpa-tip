package io.taesu.demo.jparepositories

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import java.util.*
import javax.annotation.PostConstruct

@SpringBootApplication
class JparepositoriesApplication {
    @PostConstruct
    fun onConstruct() {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"))
    }
}

fun main(args: Array<String>) {
    runApplication<JparepositoriesApplication>(*args)
}
