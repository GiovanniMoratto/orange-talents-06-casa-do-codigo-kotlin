package br.com.zupacademy.giovannimoratto.autor

import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpResponse.created
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Post
import io.micronaut.http.uri.UriBuilder
import io.micronaut.validation.Validated
import javax.transaction.Transactional
import javax.validation.Valid

/**
 *@Author giovanni.moratto
 */

@Validated
@Controller(value = "/api")
class AutorController(
    val repository: AutorRepository
) {

    /* Methods */
    // POST Request - Register an author
    @Post(value = "/cadastra-autor")
    @Transactional
    fun cadastraAutor(@Body @Valid request: AutorRequest): HttpResponse<Any> {

        val novoAutor = request.toModel()
        repository.save(novoAutor)

        val uri = UriBuilder.of("/autor/{id}").expand(mutableMapOf(Pair("id", novoAutor.id)))
        return created(uri)
    }
}