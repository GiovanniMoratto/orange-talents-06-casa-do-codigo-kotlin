package br.com.zupacademy.giovannimoratto.autor

class AutorResponse(
    autor: AutorModel
) {
    val nome = autor.nome
    val email = autor.email
    val descricao = autor.descricao

}
