package br.com.lukinhasssss.validations

import br.com.lukinhasssss.dto.request.CadastrarChaveRequest
import io.micronaut.core.annotation.AnnotationValue
import io.micronaut.validation.validator.constraints.ConstraintValidator
import io.micronaut.validation.validator.constraints.ConstraintValidatorContext
import jakarta.inject.Singleton
import javax.validation.Constraint
import javax.validation.Payload
import kotlin.annotation.AnnotationTarget.*
import kotlin.reflect.KClass

@MustBeDocumented
@Target(CLASS, TYPE)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [ValidarChaveValidator::class])
annotation class ValidarChave(
    val message: String = "Chave Pix com valor inválido!",
    val groups: Array<KClass<Any>> = [],
    val payload: Array<KClass<Payload>> = []
)

@Singleton
class ValidarChaveValidator : ConstraintValidator<ValidarChave, CadastrarChaveRequest> {
    override fun isValid(
        value: CadastrarChaveRequest?,
        annotationMetadata: AnnotationValue<ValidarChave>,
        context: ConstraintValidatorContext
    ): Boolean {

        value ?: return false

        var isValid = false

        when (value.tipoChave.number) {
            1 -> {
                value.valorChave.matches("^\\+[1-9][0-9]\\d{1,14}\$".toRegex())
                context.messageTemplate("Celular com formato inválido!")
                isValid = true
            }

            2 -> {
                value.valorChave.matches("^[0-9]{11}\$".toRegex())
                context.messageTemplate("Cpf com formato inválido!")
                isValid = true
            }

            3 -> {
                value.valorChave.matches("[a-zA-Z0-9+._%-+]{1,256}@[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}(\\.[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25})+".toRegex())
                context.messageTemplate("Email com formato inválido!")
                isValid = true
            }

            4 -> {
                value.valorChave.isBlank()
                context.messageTemplate("Quando a chave for aleatória o valor não deve ser preenchido!")
                isValid = true
            }

            else -> isValid = false
        }

        return isValid

    }

}