package br.com.zupacademy.giovannimoratto

import br.com.zupacademy.giovannimoratto.autor.AutorRequest
import br.com.zupacademy.giovannimoratto.endereco.EnderecoClient
import br.com.zupacademy.giovannimoratto.endereco.EnderecoResponse
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.annotation.MockBean
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.mockito.Mockito

/**
 *@Author giovanni.moratto
 */

@MicronautTest
class CadastraAutorControllerTest {

    @Inject
    @field:Client("/")
    lateinit var client: HttpClient

    @Inject
    lateinit var enderecoClient: EnderecoClient

    @Test
    internal fun `deve cadastrar um novo autor`() {
        val cep = "11347-685"

        val novoAutor = AutorRequest(
            "nome",
            "test@email.com",
            "descricao",
            "434.602.440-89",
            cep,
            "1500"
        )

        Mockito.`when`(enderecoClient.consulta(novoAutor.cep))
            .thenReturn(
                HttpResponse.ok(
                    EnderecoResponse(
                        cep,
                        "SP",
                        "São Vicente",
                        "Jardim Irmã Dolores",
                        "Rua Paraná"
                    )
                )
            )

        val request = HttpRequest.POST("/autores", novoAutor)
        val response = client.toBlocking().exchange(request, Any::class.java)

        assertEquals(HttpStatus.CREATED, response.status)
        assertTrue(response.headers.contains("Location"))
        assertTrue(response.header("Location")!!.matches("/autores/\\d".toRegex()))

    }

    @MockBean(EnderecoClient::class)
    fun enderecoMock(): EnderecoClient {
        return Mockito.mock(EnderecoClient::class.java)
    }

}