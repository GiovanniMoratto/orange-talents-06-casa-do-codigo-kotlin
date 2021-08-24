package br.com.zupacademy.giovannimoratto.endereco

data class EnderecoResponse(
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