package br.com.zupacademy.giovannimoratto.autor

import br.com.zupacademy.giovannimoratto.endereco.EnderecoClient
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpResponse.created
import io.micronaut.http.HttpResponse.ok
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
@Controller(value = "/api")
class AutorController(
    val repository: AutorRepository,
    val enderecoClient: EnderecoClient
) {

    @Post(value = "/autores")
    @Transactional
    fun cadastrar(@Body @Valid request: AutorRequest): HttpResponse<Any> {
        val enderecoResponse = enderecoClient.consulta(request.cep).body()
            ?: return HttpResponse.badRequest("CEP inválido")

        val autor = request.toModel(enderecoResponse)
        repository.save(autor)

        val uri = UriBuilder.of("/autor/{id}").expand(mutableMapOf(Pair("id", autor.id)))
        return created(uri)
    }

    @Get(value = "/autores")
    fun listar(): HttpResponse<List<AutorResponse>> {
        val autores = repository.findAll()
        val response = autores.map { AutorResponse(it) }

        return ok(response)
    }

    @Get(value = "/autor/{id}")
    fun buscar(@PathVariable id: Long): HttpResponse<Any> {
        val optionalAutor = repository.findById(id)
            ?: return HttpResponse.notFound("Autor não encontrado")
        
        val response = optionalAutor.map { AutorResponse(it) }

        return ok(response)
    }

    @Put(value = "/autor/{id}")
    fun atualizar(@PathVariable id: Long, @Valid @NotBlank @Size(max = 400) descricao: String): HttpResponse<Any> {
        val optionalAutor = repository.findById(id)
            ?: return HttpResponse.notFound("Autor não encontrado")

        val autor = optionalAutor.get()
        autor.descricao = descricao
        repository.update(autor)

        return ok(AutorResponse(autor))
    }

    @Delete(value = "/autor/{id}")
    fun deletar(@PathVariable id: Long): HttpResponse<Any> {
        repository.deleteById(id)

        return ok()
    }

}