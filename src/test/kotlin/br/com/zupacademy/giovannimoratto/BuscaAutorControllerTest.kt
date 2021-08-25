package br.com.zupacademy.giovannimoratto

import br.com.zupacademy.giovannimoratto.autor.*
import br.com.zupacademy.giovannimoratto.endereco.Endereco
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test


/**
 *@Author giovanni.moratto
 */

@MicronautTest
internal class BuscaAutorControllerTest {

    @field:Inject
    @field:Client("/")
    lateinit var client: HttpClient

    @Inject
    lateinit var controller: AutorController

    @Inject
    lateinit var repository: AutorRepository

    lateinit var request1: AutorRequest
    lateinit var request2: AutorRequest
    lateinit var autor1: AutorModel
    lateinit var autor2: AutorModel

    @BeforeEach
    internal fun setUp() {
        request1 = AutorRequest(
            "Rafael Ponte",
            "rafael.ponte@zup.com.br",
            "Marajá dos Legados",
            "937.661.980-33",
            "60832-100",
            "1700"
        )
        request2 = AutorRequest(
            "Tony Stark",
            "stark@emailcom",
            "Iron Man",
            "039.674.420-63",
            "60863-550",
            "250"
        )
        controller.cadastrar(request1)
        controller.cadastrar(request2)

        autor1 = AutorModel(
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
        autor2 = AutorModel(
            "Tony Stark",
            "stark@emailcom",
            "Iron Man",
            "039.674.420-63",
            Endereco(
                "Rua D (Cj João Paulo II)",
                "Fortaleza",
                "CE",
                "60863-550",
                "250",
                "Barroso"
            )
        )
        repository.save(autor1)
        repository.save(autor2)
    }

    @AfterEach
    internal fun tearDown() {
        repository.deleteAll()
    }

    @Test
    internal fun `status 200 - lista de autores`() {
//        val response = controller.buscar("")

        val resposta = client.toBlocking().exchange("/api/autores?email=${null}", DetalhesDoAutorResponse::class.java)


//        assertEquals(HttpStatus.OK, response.status)
//        assertNotNull(response.body())
        assertEquals(autor1.nome, resposta.body()!!.nome)
    }

    @Test
    internal fun `status 200 - lista de autores2`() {
//        val response = controller.buscar("")

//        val response = client
//            .toBlocking()
//            .exchange("/api/autores?email=${autor1.email}", AutorResponse::class.java)

        val response = client
            .toBlocking()
            .exchange("/api/autores?email=${null}", AutorResponse::class.java)

        assertEquals(HttpStatus.OK, response.status)
        assertNotNull(response.body())
    }

    @Test
    internal fun `status 200 - busca autor`() {

        val response = client
            .toBlocking()
            .exchange("/api/autores?email=${autor1.email}", AutorResponse::class.java)

        assertEquals(HttpStatus.OK, response.status)
        assertNotNull(response.body())
        assertEquals(autor1.nome,response.body()!!.nome)
        assertEquals(autor1.descricao,response.body()!!.descricao)
        assertEquals(autor1.email,response.body()!!.email)

    }
}