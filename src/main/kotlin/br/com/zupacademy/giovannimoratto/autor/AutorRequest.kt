package br.com.zupacademy.giovannimoratto.autor

import br.com.zupacademy.giovannimoratto.core.validators.Unique
import io.micronaut.core.annotation.Introspected
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

@Introspected
data class AutorRequest(
    /* Constructor */
    @field:NotBlank
    val name: String,

    @field:NotBlank
    @field:Email
    @field:Unique(domainClass = AutorModel::class, fieldName = "email")
    val email: String,

    @field:NotBlank
    @field:Size(max = 400)
    val description: String
) {
    /* Methods */
    // Convert AuthorRequest.class in AuthorModel.class
    fun toModel(): AutorModel {
        return AutorModel(this.name, this.email, this.description)
    }
}
