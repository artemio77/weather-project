package com.weather.microservices.weathernotificationservice.controller

import com.weather.microservices.weathernotificationservice.client.SubscriptionClient
import com.weather.microservices.weathernotificationservice.model.Subscription
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/api/v1/subscription")
class SubscriptionController(val subscriptionClient: SubscriptionClient) {


    @PostMapping
    fun createSubscription(@RequestBody subscription: Subscription): Mono<Subscription> {
        return subscriptionClient.subscribeSync(subscription);
    }

    @GetMapping
    fun getSubscriptionInfo(@RequestBody subscription: Subscription): Mono<String> {
        return subscriptionClient.getSubscriptionDetails(subscription);

    }
}