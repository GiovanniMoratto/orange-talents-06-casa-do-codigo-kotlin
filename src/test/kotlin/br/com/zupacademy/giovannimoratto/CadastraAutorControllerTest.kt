package br.com.zupacademy.giovannimoratto

import br.com.zupacademy.giovannimoratto.autor.AutorController
import br.com.zupacademy.giovannimoratto.autor.AutorRepository
import br.com.zupacademy.giovannimoratto.autor.AutorRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import javax.validation.ConstraintViolationException

/**
 *@Author giovanni.moratto
 */

@MicronautTest
internal class CadastraAutorControllerTest {

    @Inject
    lateinit var controller: AutorController

    @Inject
    lateinit var repository: AutorRepository

    @BeforeEach
    internal fun setUp() {
        repository.deleteAll()
    }

    @Test
    internal fun `status 400 - nome vazio`() {
        val request = AutorRequest(
            "",
            "rafael.ponte@zup.com.br",
            "Marajá dos Legados",
            "937.661.980-33",
            "60832-100",
            "1700"
        )

        val exception = assertThrows<ConstraintViolationException> {
            controller.cadastrar(request)
        }
        assertEquals("cadastrar.request.nome: não deve estar em branco", exception.message)
    }

    @Test
    internal fun `status 400 - email vazio`() {
        val request = AutorRequest(
            "Rafael Ponte",
            "",
            "Marajá dos Legados",
            "937.661.980-33",
            "60832-100",
            "1700"
        )

        val exception = assertThrows<ConstraintViolationException> {
            controller.cadastrar(request)
        }
        assertEquals("cadastrar.request.email: não deve estar em branco", exception.message)
    }

    @Test
    internal fun `status 400 - email formato invalido`() {
        val request = AutorRequest(
            "Rafael Ponte",
            "teste",
            "Marajá dos Legados",
            "937.661.980-33",
            "60832-100",
            "1700"
        )

        val exception = assertThrows<ConstraintViolationException> {
            controller.cadastrar(request)
        }
        assertEquals("cadastrar.request.email: deve ser um endereço de e-mail bem formado", exception.message)
    }

    @Test
    internal fun `status 400 - email duplicado`() {
        val request1 = AutorRequest(
            "Rafael Ponte",
            "rafael.ponte@zup.com.br",
            "Marajá dos Legados",
            "937.661.980-33",
            "60832-100",
            "1700"
        )
        controller.cadastrar(request1)

        val request2 = AutorRequest(
            "Tony Stark",
            "rafael.ponte@zup.com.br",
            "Iron Man",
            "039.674.420-63",
            "60863-550",
            "250"
        )

        val exception = assertThrows<ConstraintViolationException> {
            controller.cadastrar(request2)
        }
        assertEquals("cadastrar.request.email: Este valor já existe!", exception.message)
    }

    @Test
    internal fun `status 400 - descricao vazia`() {
        val request = AutorRequest(
            "Rafael Pont",
            "rafael.ponte@zup.com.br",
            "",
            "937.661.980-33",
            "60832-100",
            "1700"
        )

        val exception = assertThrows<ConstraintViolationException> {
            controller.cadastrar(request)
        }
        assertEquals("cadastrar.request.descricao: não deve estar em branco", exception.message)
    }

    @Test
    internal fun `status 400 - descricao maior que 400 caracteres`() {
        val request = AutorRequest(
            "Rafael Pont",
            "rafael.ponte@zup.com.br",
            "a".repeat(401),
            "937.661.980-33",
            "60832-100",
            "1700"
        )

        val exception = assertThrows<ConstraintViolationException> {
            controller.cadastrar(request)
        }
        assertEquals("cadastrar.request.descricao: tamanho deve ser entre 0 e 400", exception.message)
    }

    @Test
    internal fun `status 400 - cpf vazio`() {
        val request = AutorRequest(
            "Rafael Ponte",
            "rafael.ponte@zup.com.br",
            "Marajá dos Legados",
            "",
            "60832-100",
            "1700"
        )

        val exception = assertThrows<ConstraintViolationException> {
            controller.cadastrar(request)
        }
        assertEquals(
            "cadastrar.request.cpf: número do registro de contribuinte individual brasileiro (CPF) inválido, " +
                    "cadastrar.request.cpf: não deve estar em branco",
            exception.message
        )
    }

    @Test
    internal fun `status 400 - cpf formato invalido`() {
        val request = AutorRequest(
            "Rafael Ponte",
            "rafael.ponte@zup.com.br",
            "Marajá dos Legados",
            "teste",
            "60832-100",
            "1700"
        )

        val exception = assertThrows<ConstraintViolationException> {
            controller.cadastrar(request)
        }
        assertEquals(
            "cadastrar.request.cpf: número do registro de contribuinte individual brasileiro (CPF) inválido",
            exception.message
        )
    }

    @Test
    internal fun `status 400 - cpf duplicado`() {
        val request1 = AutorRequest(
            "Rafael Ponte",
            "rafael.ponte@zup.com.br",
            "Marajá dos Legados",
            "937.661.980-33",
            "60832-100",
            "1700"
        )
        controller.cadastrar(request1)

        val request2 = AutorRequest(
            "Tony Stark",
            "stark@emailcom",
            "Iron Man",
            "937.661.980-33",
            "60863-550",
            "250"
        )

        val exception = assertThrows<ConstraintViolationException> {
            controller.cadastrar(request2)
        }
        assertEquals("cadastrar.request.cpf: Este valor já existe!", exception.message)
    }

    @Test
    internal fun `status 400 - cep vazio`() {
        val request = AutorRequest(
            "Rafael Pont",
            "rafael.ponte@zup.com.br",
            "Marajá dos Legados",
            "937.661.980-33",
            "",
            "1700"
        )

        val exception = assertThrows<ConstraintViolationException> {
            controller.cadastrar(request)
        }
        assertEquals("cadastrar.request.cep: não deve estar em branco", exception.message)
    }

    @Test
    internal fun `status 400 - numero vazio`() {
        val request = AutorRequest(
            "Rafael Pont",
            "rafael.ponte@zup.com.br",
            "Marajá dos Legados",
            "937.661.980-33",
            "60832-100",
            ""
        )

        val exception = assertThrows<ConstraintViolationException> {
            controller.cadastrar(request)
        }
        assertEquals("cadastrar.request.numero: não deve estar em branco", exception.message)
    }

    @Test
    internal fun `status 201 - autor criado`() {
        val request = AutorRequest(
            "Rafael Ponte",
            "rafael.ponte@zup.com.br",
            "Marajá dos Legados",
            "937.661.980-33",
            "60832-100",
            "1700"
        )

        val response: HttpResponse<Any> = controller.cadastrar(request)
        assertEquals(HttpStatus.CREATED, response.status)
    }

}