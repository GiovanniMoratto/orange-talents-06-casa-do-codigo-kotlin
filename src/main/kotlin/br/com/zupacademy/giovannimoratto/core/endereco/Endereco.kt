package br.com.zupacademy.giovannimoratto.core.endereco

import javax.persistence.Embeddable

/**
 *@Author giovanni.moratto
 */

@Embeddable
class Endereco(
    val rua: String,
    val cidade: String,
    val estado: String,
    val cep: String,
    val numero: String,
    val bairro: String
) {
    constructor(
        endereco: Endereco
    ) : this(
        rua = endereco.rua,
        cidade = endereco.cidade,
        estado = endereco.estado,
        cep = endereco.cep,
        numero = endereco.numero,
        bairro = endereco.bairro,
    )
}