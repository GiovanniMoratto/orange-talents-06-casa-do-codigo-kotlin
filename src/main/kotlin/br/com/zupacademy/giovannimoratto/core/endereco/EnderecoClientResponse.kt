package br.com.zupacademy.giovannimoratto.core.endereco

/**
 *@Author giovanni.moratto
 */

data class EnderecoClientResponse(
    val cep: String,
    val state: String,
    val city: String,
    val neighborhood: String,
    val street: String,
) {
    fun toEndereco(numero: String): Endereco {
        return Endereco(
            cep = cep,
            estado = state,
            cidade = city,
            bairro = neighborhood,
            rua = street,
            numero = numero
        )
    }

}