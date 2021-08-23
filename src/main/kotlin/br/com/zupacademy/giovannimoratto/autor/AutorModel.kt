package br.com.zupacademy.giovannimoratto.autor

import org.hibernate.annotations.CreationTimestamp
import java.time.LocalDateTime
import javax.persistence.*
import javax.persistence.GenerationType.IDENTITY

@Entity
@Table(name = "`TB_AUTORES`")
class AutorModel(
    /* Constructor */
    @Column(nullable = false)
    val nome: String,
    @Column(nullable = false, unique = true)
    val email: String,
    @Column(nullable = false, length = 400)
    val descricao: String,
) {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    var id: Long? = null

    @CreationTimestamp
    @Column(name = "DATA_CRIACAO", nullable = false)
    var criadoEm: LocalDateTime? = null
}
