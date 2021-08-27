package br.com.zupacademy.giovannimoratto.autor

import br.com.zupacademy.giovannimoratto.core.endereco.Endereco
import io.micronaut.http.HttpRequest.GET
import io.micronaut.http.HttpStatus.NOT_FOUND
import io.micronaut.http.HttpStatus.OK
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

/**
 *@Author giovanni.moratto
 */

@MicronautTest
class BuscaAutorControllerTest {

    @Inject
    @field:Client("/")
    lateinit var client: HttpClient

    @Inject
    lateinit var repository: AutorRepository

    lateinit var autor1: AutorModel
    lateinit var autor2: AutorModel
    private val uri: String = "/api/autores"

    @BeforeEach
    internal fun setUp() {
        autor1 = AutorModel(
            "Rafael Ponte", "rafael.ponte@zup.com.br", "Marajá dos Legados", "937.661.980-33",
            Endereco("Vila Manuel Florêncio", "Fortaleza", "CE", "60832-100", "1700", "Lagoa Redonda")
        )
        autor2 = AutorModel(
            "Tony Stark", "stark@emailcom", "Iron Man", "039.674.420-63",
            Endereco("Rua D (Cj João Paulo II)", "Fortaleza", "CE", "60863-550", "250", "Barroso")
        )
        repository.save(autor1)
        repository.save(autor2)
    }

    @AfterEach
    internal fun tearDown() {
        repository.deleteAll()
    }

    @ParameterizedTest
    @ValueSource(strings = ["teste", "email@email.com"])
    internal fun `deve retornar status 404 quando o email for desconhecido`(email: String) {
        // Ação
        val request = GET<String>("$uri?email=${email}")
        val exception = assertThrows(HttpClientResponseException::class.java) {
            client.toBlocking().exchange(request, Any::class.java)
        }
        // Validação
        assertEquals(NOT_FOUND, exception.status)
        assertEquals("Not Found", exception.message)
    }

    @Test
    internal fun `deve retornar status e uma lista de autores`() {
        // Ação
        val request = GET<String>(uri)
        val response = client.toBlocking().exchange(request, Any::class.java)
        // Validação
        assertEquals(OK, response.status)
        assertNotNull(response.body())
        assertTrue(response.body()!!.toString().contains(autor1.nome))
        assertTrue(response.body()!!.toString().contains(autor1.email))
        assertTrue(response.body()!!.toString().contains(autor1.descricao))
        assertTrue(response.body()!!.toString().contains(autor2.nome))
        assertTrue(response.body()!!.toString().contains(autor2.email))
        assertTrue(response.body()!!.toString().contains(autor2.descricao))
        assertFalse(response.body()!!.toString().contains(autor1.cpf))
        assertFalse(response.body()!!.toString().contains("endereco"))
        assertFalse(response.body()!!.toString().contains("criadoEm"))
        assertFalse(response.body()!!.toString().contains("id"))
    }

    @Test
    internal fun `deve retornar status 200 e buscar um autor no banco`() {
        // Ação
        val request = GET<String>("$uri?email=${autor1.email}")
        val response = client.toBlocking().exchange(request, AutorResponse::class.java)
        // Validação
        assertEquals(OK, response.status)
        assertNotNull(response.body())
        assertEquals(autor1.nome, response.body()!!.nome)
        assertEquals(autor1.descricao, response.body()!!.descricao)
        assertEquals(autor1.email, response.body()!!.email)
        assertFalse(response.body()!!.toString().contains(autor1.cpf))
        assertFalse(response.body()!!.toString().contains("endereco"))
        assertFalse(response.body()!!.toString().contains("criadoEm"))
        assertFalse(response.body()!!.toString().contains("id"))
    }

}