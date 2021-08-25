package br.com.zupacademy.giovannimoratto

import br.com.zupacademy.giovannimoratto.autor.AutorModel
import br.com.zupacademy.giovannimoratto.autor.AutorRepository
import br.com.zupacademy.giovannimoratto.autor.AutorResponse
import br.com.zupacademy.giovannimoratto.endereco.Endereco
import br.com.zupacademy.giovannimoratto.endereco.EnderecoResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach

/**
 *@Author giovanni.moratto
 */

@MicronautTest
internal class ListaAutoresControllerTest {

    @field:Inject
    @field:Client("/api")
    lateinit var client: HttpClient

    @field:Inject
    lateinit var repository: AutorRepository

    lateinit var autor: AutorModel

    @BeforeEach
    internal fun setUp() {
        val enderecoResponse = EnderecoResponse(
            "69316398",
            "RR",
            "Boa Vista",
            "Senador Hélio Campos",
            "Avenida Abel Monteiro Reis"
        )

        val endereco: Endereco = enderecoResponse.toEndereco("692")

        autor = AutorModel(
            "Rafael Ponte",
            "rafael.ponte@zup.com.br",
            "Marajá dos Legados",
            "034.695.200-03",
            endereco
        )

        repository.save(autor)
    }

    @AfterEach
    internal fun tearDown() {
        repository.deleteAll()
    }


    @Test
    internal fun `deve retornar os detalhes de um autor`() {
        val response = client
            .toBlocking()
            .exchange("/autores?email=email@email.com", AutorResponse::class.java)

        assertEquals(HttpStatus.OK, response.status)
        assertNotNull(response.body())

        assertEquals(autor.nome, response.body()!!.nome)
        assertEquals(autor.email, response.body()!!.email)
        assertEquals(autor.descricao, response.body()!!.descricao)
    }
}