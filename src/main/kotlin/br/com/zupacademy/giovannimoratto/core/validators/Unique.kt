package br.com.zupacademy.giovannimoratto.core.validators

import jakarta.inject.Singleton
import javax.persistence.EntityManager
import javax.transaction.Transactional
import javax.validation.Constraint
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext
import javax.validation.Payload
import kotlin.annotation.AnnotationTarget.CONSTRUCTOR
import kotlin.annotation.AnnotationTarget.FIELD
import kotlin.reflect.KClass

@MustBeDocumented
@Target(FIELD, CONSTRUCTOR)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [UniqueValidator::class])
annotation class Unique(
    val domainClass: KClass<*>,
    val fieldName: String,

    val message: String = "Este valor já existe!",
    val groups: Array<KClass<Any>> = [],
    val payload: Array<KClass<Payload>> = []
)

@Singleton
open class UniqueValidator(
    private val manager: EntityManager
) : ConstraintValidator<Unique, String> {

    private lateinit var domainClass: KClass<*>
    private lateinit var fieldName: String

    override fun initialize(annotation: Unique?) {
        this.domainClass = annotation!!.domainClass
        this.fieldName = annotation.fieldName
    }

    @Transactional
    override fun isValid(value: String?, context: ConstraintValidatorContext): Boolean {
        if (value.isNullOrBlank()) return true
        return query(value).resultList.isEmpty()
    }

    private fun query(value: String?) =
        manager.createQuery("SELECT 1 FROM ${domainClass.qualifiedName} e WHERE e.${fieldName} = '${value}'")
}