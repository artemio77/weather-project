package com.weather.microservices.weathernotificationservice

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.server.reactive.ServerHttpResponse
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono

@SpringBootApplication
class WeatherNotificationApplication

fun main(args: Array<String>) {
    runApplication<WeatherNotificationApplication>(*args)
}

@Component
class LoggingFilter(val requestLogger: RequestLogger) : WebFilter {
    private val log: Logger = LoggerFactory.getLogger(this::class.java)

    override fun filter(exchange: ServerWebExchange, chain: WebFilterChain): Mono<Void> {
        log.info(requestLogger.getRequestMessage(exchange))
        val filter = chain.filter(exchange)
        exchange.response.beforeCommit {
            log.info(requestLogger.getResponseMessage(exchange))
            Mono.empty()
        }
        return filter
    }
}

@Component
class RequestLogger {

    fun getRequestMessage(exchange: ServerWebExchange): String {
        val request = exchange.request
        val method = request.method
        val path = request.uri.path
        val acceptableMediaTypes = request.headers.accept
        val contentType = request.headers.contentType
        return ">>> $method $path ${HttpHeaders.ACCEPT}: $acceptableMediaTypes ${HttpHeaders.CONTENT_TYPE}: $contentType"
    }

    fun getResponseMessage(exchange: ServerWebExchange): String {
        val request = exchange.request
        val response = exchange.response
        val method = request.method
        val path = request.uri.path
        val statusCode = getStatus(response)
        val contentType = response.headers.contentType
        return "<<< $method $path HTTP${statusCode?.value()} ${statusCode?.reasonPhrase} ${HttpHeaders.CONTENT_TYPE}: $contentType"
    }

    private fun getStatus(response: ServerHttpResponse): HttpStatus? =
        try {
            response.statusCode
        } catch (ex: Exception) {
            HttpStatus.CONTINUE
        }
}
