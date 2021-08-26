package br.com.zupacademy.giovannimoratto.autor

import br.com.zupacademy.giovannimoratto.core.endereco.Endereco
import org.hibernate.annotations.CreationTimestamp
import java.time.LocalDateTime
import javax.persistence.*
import javax.persistence.GenerationType.IDENTITY

@Entity
@Table(name = "`TB_AUTORES`")
class AutorModel(
    val nome: String,
    val email: String,
    var descricao: String,
    val cpf: String,
    @Embedded
    val endereco: Endereco
) {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    val id: Long? = null

    @CreationTimestamp
    @Column(name = "DATA_CRIACAO", nullable = false)
    val criadoEm: LocalDateTime? = null
}
