package br.com.zupacademy.giovannimoratto

import br.com.zupacademy.giovannimoratto.autor.*
import br.com.zupacademy.giovannimoratto.endereco.Endereco
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.net.URI

/**
 *@Author giovanni.moratto
 */

@MicronautTest
internal class BuscaAutoresControllerTest {

    @Inject
    lateinit var controller: AutorController

    @Inject
    lateinit var repository: AutorRepository

    lateinit var autor: AutorModel
    val uriBuscaAutor = "/api/autores?email="


    @BeforeEach
    internal fun setUp() {

        autor = AutorModel(
            "Rafael Ponte",
            "rafael.ponte@zup.com.br",
            "Marajá dos Legados",
            "937.661.980-33",
            Endereco(
                "Vila Manuel Florêncio",
                "Fortaleza",
                "CE",
                "60832-100",
                "1700",
                "Lagoa Redonda"
            )
        )
        repository.save(autor)
    }

    @AfterEach
    internal fun tearDown() {
        repository.deleteAll()
    }

    @Test
    internal fun `deve retornar um autor`() {

        val response = client.toBlocking().exchange(
            uriBuscaAutor + autor.email, AutorResponse::class.java)

        var request:AutorRequest = AutorRequest(
            "",
            "",
            "",
            "",
            "",
            ""
        )

        assertEquals(HttpStatus.OK, response.status)
        assertNotNull(response.body())
        assertEquals(autor.nome, response.body()!!.nome)
        assertEquals(autor.email, response.body()!!.email)
        assertEquals(autor.descricao, response.body()!!.descricao)
    }

    @Test
    internal fun `deve retornar 404 email não encontrado`() {

        val response: HttpResponse<Any> = client.toBlocking().exchange(
            "${uriBuscaAutor} + teste@email.com", AutorResponse::class.java)

        assertEquals(HttpStatus.NOT_FOUND, response.status)
    }
}