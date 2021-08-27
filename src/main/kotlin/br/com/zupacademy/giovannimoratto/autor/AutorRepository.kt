package br.com.zupacademy.giovannimoratto.autor

import io.micronaut.data.annotation.Repository
import io.micronaut.data.jpa.repository.JpaRepository
import java.util.*

@Repository
interface AutorRepository : JpaRepository<AutorModel, Long> {

    fun getByEmail(email: String): AutorModel?
    fun getById(id: Long): AutorModel?

    fun findByEmail(email: String): Optional<AutorModel>

}
