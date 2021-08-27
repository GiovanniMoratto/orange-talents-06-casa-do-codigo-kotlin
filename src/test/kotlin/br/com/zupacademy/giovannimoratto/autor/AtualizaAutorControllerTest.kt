package br.com.zupacademy.giovannimoratto.autor

import br.com.zupacademy.giovannimoratto.core.endereco.Endereco
import io.micronaut.http.HttpRequest.PUT
import io.micronaut.http.HttpStatus.BAD_REQUEST
import io.micronaut.http.HttpStatus.OK
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EmptySource
import org.junit.jupiter.params.provider.ValueSource

/**
 *@Author giovanni.moratto
 */

@MicronautTest
class AtualizaAutorControllerTest {

    @Inject
    @field:Client("/")
    lateinit var client: HttpClient

    @Inject
    lateinit var controller: AutorController

    @Inject
    lateinit var repository: AutorRepository

    lateinit var autor: AutorModel
    private var id: Long = 1L
    private var uri = "/api/autor/$id"

    @BeforeEach
    internal fun setUp() {
        repository.deleteAll()
        autor = AutorModel(
            "Rafael Ponte", "rafael.ponte@zup.com.br", "Marajá dos Legados", "937.661.980-33",
            Endereco("Vila Manuel Florêncio", "Fortaleza", "CE", "60832-100", "1700", "Lagoa Redonda")
        )
        repository.save(autor)
        id = repository.findByEmail("rafael.ponte@zup.com.br").get().id!!
    }

    @ParameterizedTest
    @EmptySource
    @ValueSource(
        strings = ["", "\n", "\t", "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
                "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
                "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
                "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
                "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
                "aaaaaaaaaaaaaaaaaaaaaa"]
    )
    internal fun `deve retornar status 400 quando o DESCRICAO for vazia ou maior que 400 caracteres`(descricao: String) {
        // Ação
        val request = PUT(uri, descricao)
        val exception = assertThrows(HttpClientResponseException::class.java) {
            client.toBlocking().exchange(request, Any::class.java)
        }
        // Validação
        assertEquals(exception.status, BAD_REQUEST)
        assertTrue(repository.findById(id).get().descricao == "Marajá dos Legados")
    }

    @Test
    internal fun `deve retornar status 200 e atualizar a descricao`() {
        // Cenário
        val descricao = "Principe dos Oceanos"
        // Ação
        val response = controller.atualizar(id, descricao)
        // Validação
        assertEquals(response.status, OK)
        assertFalse(repository.findById(id).get().descricao == "Marajá dos Legados")
        assertTrue(repository.findById(id).get().descricao == descricao)
    }

}