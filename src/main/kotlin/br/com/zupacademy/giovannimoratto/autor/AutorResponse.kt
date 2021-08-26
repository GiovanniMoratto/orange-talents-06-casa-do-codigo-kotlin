package br.com.zupacademy.giovannimoratto.autor

class AutorResponse(
    val nome: String,
    val email: String,
    val descricao: String
) {
    constructor(
        autor: AutorModel
    ) : this(
        nome = autor.nome,
        email = autor.email,
        descricao = autor.descricao
    )

}
