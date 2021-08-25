package br.com.zupacademy.giovannimoratto.autor

import br.com.zupacademy.giovannimoratto.endereco.EnderecoClient
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
@Controller("/api/autores")
class AutorController(
    val repository: AutorRepository,
    val enderecoClient: EnderecoClient
) {

    @Post
    @Transactional
    fun cadastrar(@Body @Valid request: AutorRequest): HttpResponse<Any> {

        val clientResponse = enderecoClient.consulta(request.cep)?.body()
            ?: return badRequest("CEP inválido")

        val autor = request.toModel(clientResponse)
        repository.save(autor)

        val uri = UriBuilder.of("/autor/{id}").expand(mutableMapOf(Pair("id", autor.id)))
        return created(uri)
    }

    @Get
    @Transactional
    fun buscar(@QueryValue(defaultValue = "") email: String): HttpResponse<Any> {

        if (email.isBlank()) {
            val autores = repository.findAll()
            val response = autores.map { AutorResponse(it) }

            return ok(response)
        }

        val autor = repository.getByEmail(email)
            ?: return notFound("Autor não encontrado")

        return ok(AutorResponse(autor))
    }

    @Put("/{id}")
    @Transactional
    fun atualizar(@PathVariable id: Long, @Valid @NotBlank @Size(max = 400) descricao: String): HttpResponse<Any> {

        val autor = repository.getById(id)
            ?: return notFound("Autor não encontrado")

        autor.descricao = descricao

        return ok(AutorResponse(autor))
    }

    @Delete("/{id}")
    @Transactional
    fun deletar(@PathVariable id: Long): HttpResponse<Any> {
        repository.deleteById(id)

        return ok()
    }

}
