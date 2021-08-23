package br.com.zupacademy.giovannimoratto

import io.micronaut.runtime.Micronaut.*
fun main(args: Array<String>) {
	build()
	    .args(*args)
		.packages("br.com.zupacademy.giovannimoratto")
		.start()
}

