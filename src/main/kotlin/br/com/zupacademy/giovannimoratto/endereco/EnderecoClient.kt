package br.com.zupacademy.giovannimoratto.endereco

import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Get
import io.micronaut.http.client.annotation.Client

/**
 *@Author giovanni.moratto
 */

@Client("\${url-service-cep}")
interface EnderecoClient {

    @Get("/{cep}")
    fun consulta(cep: String): HttpResponse<EnderecoResponse>?

}