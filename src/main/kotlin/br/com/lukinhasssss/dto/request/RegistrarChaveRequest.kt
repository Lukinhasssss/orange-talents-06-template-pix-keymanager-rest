package br.com.lukinhasssss.dto.request

import br.com.lukinhasssss.RegistrarChaveRequest
import br.com.lukinhasssss.TipoChave
import br.com.lukinhasssss.TipoConta
import br.com.lukinhasssss.validations.ValidarChave
import io.micronaut.core.annotation.Introspected
import org.jetbrains.annotations.NotNull
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

@ValidarChave
@Introspected
data class CadastrarChaveRequest(

    @field:NotNull("Campo obrigatório!")
    val tipoChave: TipoChave,

    @field:Size(max = 77)
    val valorChave: String,

    @field:NotNull("Campo obrigatório!")
    val tipoConta: TipoConta

) {

    fun toGrpc(idCliente: String): RegistrarChaveRequest {
        return RegistrarChaveRequest.newBuilder()
            .setIdCliente(idCliente)
            .setTipoChave(tipoChave)
            .setValorChave(valorChave)
            .setTipoConta(tipoConta)
            .build()
    }

}