package com.weather.microservices.weathernotificationservice.controller

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono


@RestController
@RequestMapping("/api/v1/notification")
class NotificationController {

    private val log: Logger = LoggerFactory.getLogger(this::class.java)

    @GetMapping
    fun verifyNotification(
        @RequestParam("hub.topic") topic: String,
        @RequestParam("hub.challenge") challenge: String
    ): Mono<String> {
        return Mono.just(challenge)
    }

    @PostMapping
    fun acceptNotification(
        @RequestBody body: String
    ): Mono<String> {
        return Mono.just(body)
            .doOnNext { log.info(body) }
    }
}