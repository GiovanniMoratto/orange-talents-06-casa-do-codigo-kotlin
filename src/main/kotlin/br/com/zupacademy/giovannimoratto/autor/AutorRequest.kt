package br.com.zupacademy.giovannimoratto.autor

import br.com.zupacademy.giovannimoratto.core.validators.Unique
import br.com.zupacademy.giovannimoratto.endereco.EnderecoResponse
import io.micronaut.core.annotation.Introspected
import org.hibernate.validator.constraints.br.CPF
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

@Introspected
data class AutorRequest(
    @field:NotBlank
    val nome: String,
    @field:NotBlank
    @field:Email
    @field:Unique(domainClass = AutorModel::class, fieldName = "email")
    val email: String,
    @field:NotBlank
    @field:Size(max = 400)
    val descricao: String,
    @field:NotBlank
    @field:CPF
    @field:Unique(domainClass = AutorModel::class, fieldName = "cpf")
    val cpf: String,
    @field:NotBlank
    val cep: String,
    @field:NotBlank
    val numero: String,
) {
    fun toModel(clientResponse: EnderecoResponse): AutorModel {
        val endereco = clientResponse.toEndereco(this.numero)

        return AutorModel(this.nome, this.email, this.descricao, this.cpf, endereco)
    }

}