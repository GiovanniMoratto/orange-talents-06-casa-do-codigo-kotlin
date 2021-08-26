package br.com.zupacademy.giovannimoratto

import br.com.zupacademy.giovannimoratto.autor.*
import br.com.zupacademy.giovannimoratto.core.endereco.Endereco
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
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
class BuscaAutorControllerTest {

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
        val response = client
            .toBlocking()
            .exchange("/api/autores", Any::class.java)
//        val response = controller.buscar("")

        val testResponse = listOf(
            listOf(autor1.nome, autor1.email, autor1.descricao),
            listOf(autor2.nome, autor2.email, autor2.descricao)
        )


//        val testResponse = listOf(
//            {
//                var nome = autor1.nome
//                var email = autor1.email
//                var descricao = autor1.descricao
//            },
//            {
//                var nome = autor2.nome
//                var email = autor2.email
//                var descricao = autor2.descricao
//            }
//        )

        assertEquals(HttpStatus.OK, response.status)
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
    internal fun `status 200 - busca autor`() {
        val response = client
            .toBlocking()
            .exchange("/api/autores?email=${autor1.email}", AutorResponse::class.java)

        assertEquals(HttpStatus.OK, response.status)
        assertNotNull(response.body())
        assertEquals(autor1.nome, response.body()!!.nome)
        assertEquals(autor1.descricao, response.body()!!.descricao)
        assertEquals(autor1.email, response.body()!!.email)
    }

}