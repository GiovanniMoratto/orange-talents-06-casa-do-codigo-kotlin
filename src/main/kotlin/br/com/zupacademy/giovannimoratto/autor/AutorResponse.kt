package br.com.zupacademy.giovannimoratto.autor

data class AutorResponse(
    val autor: AutorModel
) {
    val nome = autor.nome
    val email = autor.email
    val descricao = autor.descricao
}
