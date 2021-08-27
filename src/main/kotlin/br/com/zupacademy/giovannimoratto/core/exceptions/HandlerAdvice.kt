package br.com.zupacademy.giovannimoratto.core.exceptions

import io.micronaut.http.client.exceptions.HttpClientResponseException

/**
 *@Author giovanni.moratto
 */

class HandlerAdvice(
    val status: HttpClientResponseException,
    override val message: String
) : RuntimeException() {
}