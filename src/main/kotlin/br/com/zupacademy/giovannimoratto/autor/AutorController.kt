package br.com.zupacademy.giovannimoratto.autor

import br.com.zupacademy.giovannimoratto.core.endereco.EnderecoClient
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpResponse.*
import io.micronaut.http.annotation.*
import io.micronaut.http.uri.UriBuilder
import io.micronaut.validation.Validated
import javax.transaction.Transactional
import javax.validation.Valid
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

/**
 *@Author giovanni.moratto
 */

@Validated
@Controller("/api")
class AutorController(
    val repository: AutorRepository,
    val client: EnderecoClient
) {
    @Post("/cadastra-autor")
    @Transactional
    fun cadastrar(@Body @Valid request: AutorRequest): HttpResponse<Any> {

        val clientResponse = client.consulta(request.cep)?.body()
            ?: return badRequest("CEP inválido")

        val autor = request.toModel(clientResponse)
        repository.save(autor)

        val uri = UriBuilder.of("/autor/{id}").expand(mutableMapOf(Pair("id", autor.id)))
        return created(uri)
    }

    @Get("/autores")
    @Transactional
    fun buscar(@QueryValue(defaultValue = "") email: String): HttpResponse<Any> {
        if (email.isBlank()) {
            val autores = repository.findAll()
            val response = autores.map(::AutorResponse)

            return ok(response)
        }

        val autor = repository.getByEmail(email)
            ?: return notFound("Autor não encontrado")

        return ok(AutorResponse(autor))
    }

    @Put("/autor/{id}")
    @Transactional
    fun atualizar(@PathVariable id: Long, @NotBlank @Size(max = 400) descricao: String): HttpResponse<Any> {

        val autor = repository.getById(id)
            ?: return notFound("Autor não encontrado")

        autor.descricao = descricao

        return ok(AutorResponse(autor))
    }

    @Delete("/autor/{id}")
    @Transactional
    fun deletar(@PathVariable id: Long): HttpResponse<Any> {
        repository.deleteById(id)

        return ok()
    }

}