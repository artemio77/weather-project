package com.weather.microservices.weathernotificationservice.model

data class Subscription(
    val id: String?,
    val countryName: String,
    val subscriptionTopicName: String,
    val callbackUrl: String,
    val verifyToken: String?,
    var status: SubscriptionStatus?
)
