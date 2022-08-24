package com.derevets.artem

import io.micronaut.runtime.Micronaut.*
fun main(args: Array<String>) {
	build()
	    .args(*args)
		.packages("com.derevets.artem")
		.start()
}

