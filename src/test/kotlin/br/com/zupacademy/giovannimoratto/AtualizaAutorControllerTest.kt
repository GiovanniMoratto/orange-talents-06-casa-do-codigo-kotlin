package br.com.zupacademy.giovannimoratto

import br.com.zupacademy.giovannimoratto.autor.AutorController
import br.com.zupacademy.giovannimoratto.autor.AutorModel
import br.com.zupacademy.giovannimoratto.autor.AutorRepository
import br.com.zupacademy.giovannimoratto.autor.AutorResponse
import br.com.zupacademy.giovannimoratto.core.endereco.Endereco
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

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
    internal fun `status 200 - atualiza autor`() {
        val response = client
            .toBlocking()
            .exchange("/api/autor/1", AutorResponse::class.java)
    }

}