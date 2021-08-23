package br.com.zupacademy.giovannimoratto.core.exceptions

import io.micronaut.http.HttpStatus

/**
 *@Author giovanni.moratto
 */

class ApiErrorException(
    val status: HttpStatus,
    override val message: String
) : RuntimeException() {
}