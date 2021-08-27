package br.com.zupacademy.giovannimoratto.autor

import br.com.zupacademy.giovannimoratto.core.endereco.Endereco
import br.com.zupacademy.giovannimoratto.core.endereco.EnderecoClient
import br.com.zupacademy.giovannimoratto.core.endereco.EnderecoClientResponse
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
import org.junit.jupiter.params.provider.EmptySource
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

    private val uri: String = "/api/cadastra-autor"
    private val clientResponse = EnderecoClientResponse("teste", "teste", "teste", "teste", "teste")

    @BeforeEach
    internal fun setUp() {
        repository.deleteAll()
        val autorDuplicado = AutorModel(
            "Duplicado", "duplicado@email.com", "Autor duplicado", "390.164.250-12",
            Endereco("teste", "teste", "teste", "teste", "teste", "teste")
        )
        repository.save(autorDuplicado)
    }

    @ParameterizedTest
    @EmptySource
    @ValueSource(strings = ["", "\n", "\t"])
    internal fun `deve retornar status 400 quando o NOME for vazio`(nome: String) {
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
    @EmptySource
    @ValueSource(strings = ["", "\n", "\t", "invalidEmail.com", "@invalid.com", "@.com", "@invalid", "duplicado@email.com"])
    internal fun `deve retornar status 400 quando o EMAIL for vazio, formato incorreto ou duplicado`(email: String) {
        // Cenário
        val nome = "Rafael Ponte"
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
        // Cenário
        val nome = "Rafael Ponte"
        val email = "rafael.ponte@zup.com.br"
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
    @EmptySource
    @ValueSource(
        strings = ["", "\n", "\t", "42.699.531/0001-64", "SFIOWEVSAEGVE", "00000000000", "XXXXXXXXXXX",
            "16265695436", "390.164.250-12"]
    )
    internal fun `deve retornar status 400 quando o CPF for vazio, formato incorreto ou duplicado`(cpf: String) {
        // Cenário
        val nome = "Rafael Ponte"
        val email = "rafael.ponte@zup.com.br"
        val descricao = "Marajá dos Legados"
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
    @EmptySource
    @ValueSource(strings = ["", "\n", "\t"])
    internal fun `deve retornar status 400 quando o CEP for vazio`(cep: String) {
        // Cenário
        val nome = "Rafael Ponte"
        val email = "rafael.ponte@zup.com.br"
        val descricao = "Marajá dos Legados"
        val cpf = "937.661.980-33"
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
    @EmptySource
    @ValueSource(strings = ["", "\n", "\t"])
    internal fun `deve retornar status 400 quando o NUMERO for vazio`(numero: String) {
        // Cenário
        val nome = "Rafael Ponte"
        val email = "rafael.ponte@zup.com.br"
        val descricao = "Marajá dos Legados"
        val cpf = "937.661.980-33"
        val cep = "60832-100"
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

    @Test
    internal fun `status 201 - autor criado`() {
        // Cenário
        val novoAutor = AutorRequest(
            "Rafael Ponte", "rafael.ponte@zup.com.br",
            "Marajá dos Legados", "937.661.980-33", "60832-100", "1700"
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