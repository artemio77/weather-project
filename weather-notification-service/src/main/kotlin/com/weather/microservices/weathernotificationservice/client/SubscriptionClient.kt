package com.weather.microservices.weathernotificationservice.client

import com.weather.microservices.weathernotificationservice.model.Subscription
import com.weather.microservices.weathernotificationservice.model.SubscriptionStatus
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono
import reactor.core.publisher.Mono

@Component
class SubscriptionClient {

    fun getSubscriptionDetails(subscription: Subscription): Mono<String> {
        val client: WebClient = WebClient.create("https://pubsubhubbub.appspot.com/subscription-details")
        return client.get()
            .uri {
                it
                    .queryParam("hub.callback", subscription.callbackUrl)
                    .queryParam("hub.topic", subscription.subscriptionTopicName)
                it.build();
            }
            .retrieve()
            .bodyToMono();

    }

    fun subscribeSync(subscription: Subscription): Mono<Subscription> {
        val client: WebClient = WebClient.create("https://pubsubhubbub.appspot.com/subscribe")
        return client.post().contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(
                BodyInserters.fromFormData("hub.mode", "subscribe")
                    .with("hub.topic", subscription.subscriptionTopicName)
                    .with("hub.callback", subscription.callbackUrl)
                    .with("hub.verify", "sync")
            )
            .retrieve()
            .bodyToMono<String>()
            .thenReturn(subscription)
            .map {
                it.status = SubscriptionStatus.SUBSCRIBED
                it
            }
    }
}