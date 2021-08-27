package br.com.zupacademy.giovannimoratto

import br.com.zupacademy.giovannimoratto.autor.AutorModel
import br.com.zupacademy.giovannimoratto.autor.AutorRepository
import br.com.zupacademy.giovannimoratto.autor.AutorRequest
import br.com.zupacademy.giovannimoratto.core.endereco.Endereco
import br.com.zupacademy.giovannimoratto.core.endereco.EnderecoClient
import br.com.zupacademy.giovannimoratto.core.endereco.EnderecoClientResponse
import io.micronaut.context.annotation.Parameter
import io.micronaut.http.HttpRequest.POST
import io.micronaut.http.HttpResponse.ok
import io.micronaut.http.HttpStatus.BAD_REQUEST
import io.micronaut.http.HttpStatus.CREATED
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.test.annotation.MockBean
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.NullAndEmptySource
import org.junit.jupiter.params.provider.ValueSource
import org.mockito.Mockito

/**
 *@Author giovanni.moratto
 */

@MicronautTest
class CadastraAutorControllerTest {

    @Inject
    lateinit var repository: AutorRepository

    @Inject
    @field:Client("/")
    lateinit var client: HttpClient

    @Inject
    lateinit var enderecoClient: EnderecoClient

    @BeforeEach
    internal fun setUp() {
        repository.deleteAll()

//        val autorDuplicado = AutorModel(
//            "Duplicado",
//            "duplicado@email.com",
//            "Autor duplicado",
//            "390.164.250-12",
//            Endereco(
//                "teste",
//                "teste",
//                "teste",
//                "teste",
//                "teste",
//                "teste"
//            )
//        )
//        repository.save(autorDuplicado)
    }

    @ParameterizedTest
    @NullAndEmptySource
    internal fun `deve retornar status 400 quando o nome for vazio`(nome: String) {
        // Cenário
        val email = "rafael.ponte@zup.com.br"
        val descricao = "Marajá dos Legados"
        val cpf = "937.661.980-33"
        val cep = "60832-100"
        val numero = "1700"
        val novoAutor = AutorRequest(nome, email, descricao, cpf, cep, numero)
        // Ação
        Mockito.`when`(enderecoClient.consulta(novoAutor.cep)).thenReturn(ok(clientResponse))
        val request = POST(uri, novoAutor)
        val exception = assertThrows(HttpClientResponseException::class.java) {
            client.toBlocking().exchange(request, Any::class.java)
        }
        // Validação
        assertEquals(exception.status, BAD_REQUEST)
        assertEquals(1, repository.count())
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = ["invalidEmail.com", "@invalid.com", "@.com", "@invalid", "duplicado@email.com"])
    internal fun `deve retornar status 400 quando o nome for vazio, formato incorreto ou duplicado`() {
        // Cenário
        val nome = "Rafael Ponte"
        val email = "rafael.ponte@zup.com.br"
        val descricao = "Marajá dos Legados"
        val cpf = "937.661.980-33"
        val cep = "60832-100"
        val numero = "1700"
        val novoAutor = AutorRequest(nome, email, descricao, cpf, cep, numero)
        // Ação
        Mockito.`when`(enderecoClient.consulta(novoAutor.cep)).thenReturn(ok(clientResponse))
        val request = POST(uri, novoAutor)
        val exception = assertThrows(HttpClientResponseException::class.java) {
            client.toBlocking().exchange(request, Any::class.java)
        }
        // Validação
        assertEquals(exception.status, BAD_REQUEST)
        assertTrue(repository.findAll().isEmpty())
    }

    @Test
    internal fun `status 400 - email formato invalido`() {
        // Cenário
        val novoAutor = AutorRequest(
            "Rafael Ponte",
            "teste",
            "Marajá dos Legados",
            "937.661.980-33",
            "60832-100",
            "1700"
        )
        // Ação
        Mockito.`when`(enderecoClient.consulta(novoAutor.cep)).thenReturn(ok(clientResponse))
        val request = POST(uri, novoAutor)
        val exception = assertThrows(HttpClientResponseException::class.java) {
            client.toBlocking().exchange(request, Any::class.java)
        }
        // Validação
        assertEquals(exception.status, BAD_REQUEST)
        assertTrue(repository.findAll().isEmpty())
    }

    @Test
    internal fun `status 400 - email duplicado`() {
        // Cenário
        autorDuplicado(repository)
        val novoAutor = AutorRequest(
            "Rafael Ponte",
            "duplicado@email.com",
            "Marajá dos Legados",
            "937.661.980-33",
            "60832-100",
            "1700"
        )
        // Ação
        Mockito.`when`(enderecoClient.consulta(novoAutor.cep)).thenReturn(ok(clientResponse))
        val request = POST(uri, novoAutor)
        val exception = assertThrows(HttpClientResponseException::class.java) {
            client.toBlocking().exchange(request, Any::class.java)
        }
        // Validação
        assertEquals(exception.status, BAD_REQUEST)
        assertTrue(repository.findAll().isEmpty())
    }

    @Test
    internal fun `status 400 - descricao vazia`() {
        // Cenário
        val novoAutor = AutorRequest(
            "Rafael Pont",
            "rafael.ponte@zup.com.br",
            "",
            "937.661.980-33",
            "60832-100",
            "1700"
        )
        // Ação
        Mockito.`when`(enderecoClient.consulta(novoAutor.cep)).thenReturn(ok(clientResponse))
        val request = POST(uri, novoAutor)
        val exception = assertThrows(HttpClientResponseException::class.java) {
            client.toBlocking().exchange(request, Any::class.java)
        }
        // Validação
        assertEquals(exception.status, BAD_REQUEST)
        assertTrue(repository.findAll().isEmpty())
    }

    @Test
    internal fun `status 400 - descricao maior que 400 caracteres`() {
        // Cenário
        val novoAutor = AutorRequest(
            "Rafael Pont",
            "rafael.ponte@zup.com.br",
            "a".repeat(401),
            "937.661.980-33",
            "60832-100",
            "1700"
        )
        // Ação
        Mockito.`when`(enderecoClient.consulta(novoAutor.cep)).thenReturn(ok(clientResponse))
        val request = POST(uri, novoAutor)
        val exception = assertThrows(HttpClientResponseException::class.java) {
            client.toBlocking().exchange(request, Any::class.java)
        }
        // Validação
        assertEquals(exception.status, BAD_REQUEST)
        assertTrue(repository.findAll().isEmpty())
    }

    @Test
    internal fun `status 400 - cpf vazio`() {
        // Cenário
        val novoAutor = AutorRequest(
            "Rafael Ponte",
            "rafael.ponte@zup.com.br",
            "Marajá dos Legados",
            "",
            "60832-100",
            "1700"
        )
        // Ação
        Mockito.`when`(enderecoClient.consulta(novoAutor.cep)).thenReturn(ok(clientResponse))
        val request = POST(uri, novoAutor)
        val exception = assertThrows(HttpClientResponseException::class.java) {
            client.toBlocking().exchange(request, Any::class.java)
        }
        // Validação
        assertEquals(exception.status, BAD_REQUEST)
        assertTrue(repository.findAll().isEmpty())
    }

    @Test
    internal fun `status 400 - cpf formato invalido`() {
        // Cenário
        val novoAutor = AutorRequest(
            "Rafael Ponte",
            "rafael.ponte@zup.com.br",
            "Marajá dos Legados",
            "teste",
            "60832-100",
            "1700"
        )
        // Ação
        Mockito.`when`(enderecoClient.consulta(novoAutor.cep)).thenReturn(ok(clientResponse))
        val request = POST(uri, novoAutor)
        val exception = assertThrows(HttpClientResponseException::class.java) {
            client.toBlocking().exchange(request, Any::class.java)
        }
        // Validação
        assertEquals(exception.status, BAD_REQUEST)
        assertTrue(repository.findAll().isEmpty())
    }

    @Test
    internal fun `status 400 - cpf duplicado`() {
        // Cenário
        val novoAutor = AutorRequest(
            "Rafael Ponte",
            "rafael.ponte@zup.com.br",
            "Marajá dos Legados",
            "390.164.250-12",
            "60832-100",
            "1700"
        )
        // Ação
        Mockito.`when`(enderecoClient.consulta(novoAutor.cep)).thenReturn(ok(clientResponse))
        val request = POST(uri, novoAutor)
        val exception = assertThrows(HttpClientResponseException::class.java) {
            client.toBlocking().exchange(request, Any::class.java)
        }
        // Validação
        assertEquals(exception.status, BAD_REQUEST)
        assertTrue(repository.findAll().isEmpty())
    }

    @Test
    internal fun `status 400 - cep vazio`() {
        // Cenário
        val novoAutor = AutorRequest(
            "Rafael Pont",
            "rafael.ponte@zup.com.br",
            "Marajá dos Legados",
            "937.661.980-33",
            "",
            "1700"
        )
        // Ação
        Mockito.`when`(enderecoClient.consulta(novoAutor.cep)).thenReturn(ok(clientResponse))
        val request = POST(uri, novoAutor)
        val exception = assertThrows(HttpClientResponseException::class.java) {
            client.toBlocking().exchange(request, Any::class.java)
        }
        // Validação
        assertEquals(exception.status, BAD_REQUEST)
        assertTrue(repository.findAll().isEmpty())
    }

    @Test
    internal fun `status 400 - numero vazio`() {
        // Cenário
        val novoAutor = AutorRequest(
            "Rafael Pont",
            "rafael.ponte@zup.com.br",
            "Marajá dos Legados",
            "937.661.980-33",
            "60832-100",
            ""
        )
        // Ação
        Mockito.`when`(enderecoClient.consulta(novoAutor.cep)).thenReturn(ok(clientResponse))
        val request = POST(uri, novoAutor)
        val exception = assertThrows(HttpClientResponseException::class.java) {
            client.toBlocking().exchange(request, Any::class.java)
        }
        // Validação
        assertEquals(exception.status, BAD_REQUEST)
        assertTrue(repository.findAll().isEmpty())
    }

    @Test
    internal fun `status 201 - autor criado`() {
        // Cenário
        val novoAutor = AutorRequest(
            "Rafael Ponte",
            "rafael.ponte@zup.com.br",
            "Marajá dos Legados",
            "937.661.980-33",
            "60832-100",
            "1700"
        )
        // Ação
        Mockito.`when`(enderecoClient.consulta(novoAutor.cep)).thenReturn(ok(clientResponse))
        val request = POST(uri, novoAutor)
        val response = client.toBlocking().exchange(request, Any::class.java)
        // Validação
        assertEquals(response.status, CREATED)
        assertTrue(repository.findByEmail("rafael.ponte@zup.com.br").isPresent)
    }

    @MockBean(EnderecoClient::class)
    fun enderecoMock(): EnderecoClient {
        return Mockito.mock(EnderecoClient::class.java)
    }

}

private var uri: String = "/api/cadastra-autor"
private var clientResponse = EnderecoClientResponse(
    "teste", "teste", "teste", "teste", "teste"
)
private var novoAutor = AutorRequest(
    nome = "Duplicado",
    email = "duplicado@email.com",
    descricao = "Autor duplicado",
    cpf = "390.164.250-12",
    cep = "60832-100",
    numero = "1700"
)

fun autorDuplicado(
    repository: AutorRepository
) {
    val autorDuplicado = AutorModel(
        "Duplicado",
        "duplicado@email.com",
        "Autor duplicado",
        "390.164.250-12",
        Endereco(
            "teste",
            "teste",
            "teste",
            "teste",
            "teste",
            "teste"
        )
    )
    repository.save(autorDuplicado)
}
