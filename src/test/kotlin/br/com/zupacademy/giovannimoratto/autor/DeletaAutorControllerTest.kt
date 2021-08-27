package br.com.zupacademy.giovannimoratto.autor

import br.com.zupacademy.giovannimoratto.core.endereco.Endereco
import io.micronaut.http.HttpRequest.DELETE
import io.micronaut.http.HttpStatus.OK
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

/**
 *@Author giovanni.moratto
 */

@MicronautTest
class DeletaAutorControllerTest {

    @Inject
    @field:Client("/")
    lateinit var client: HttpClient

    @Inject
    lateinit var controller: AutorController

    @Inject
    lateinit var repository: AutorRepository

    private lateinit var autor: AutorModel
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
    @ValueSource(longs = [99L, 1000L, 777L])
    internal fun `deve retornar status 200 quando o id for desconhecido e nao apagar nenhum registro`(id: Long) {
        // Ação
        val request = DELETE<String>("/api/autor/$id")
        val response = client.toBlocking().exchange(request, Any::class.java)
        // Validação
        assertEquals(OK, response.status)
        assertEquals(1, repository.count())
        assertTrue(repository.findByEmail("rafael.ponte@zup.com.br").isPresent)
    }

    @Test
    internal fun `deve apagar o registro quando o ID for valido`() {
        // Ação
        val response = controller.deletar(id)
        // Validação
        assertEquals(OK, response.status)
        assertEquals(0, repository.count())
        assertFalse(repository.findByEmail("rafael.ponte@zup.com.br").isPresent)
    }
}