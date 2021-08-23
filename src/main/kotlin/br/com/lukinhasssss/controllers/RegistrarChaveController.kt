package br.com.lukinhasssss.controllers

import br.com.lukinhasssss.dto.request.CadastrarChaveRequest
import br.com.lukinhasssss.RegistrarChaveServiceGrpc
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.PathVariable
import io.micronaut.http.annotation.Post
import io.micronaut.validation.Validated
import javax.validation.Valid

@Validated
@Controller("/v1/clientes")
class RegistrarChaveController(
    private val grpcClient: RegistrarChaveServiceGrpc.RegistrarChaveServiceBlockingStub
) {

    @Post("/{idCliente}/pix")
    fun registrarChave(@PathVariable idCliente: String, @Valid @Body request: CadastrarChaveRequest): HttpResponse<Unit> {
        grpcClient.registrarChave(request.toGrpc(idCliente)).let {
            val uri = HttpResponse.uri("/v1/clientes/$idCliente/pix/${it.pixId}")
            return HttpResponse.created(uri)
        }
    }

}