package br.com.lukinhasssss.controllers

import br.com.lukinhasssss.RemoverChaveRequest
import br.com.lukinhasssss.RemoverChaveServiceGrpc
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Delete

@Controller("/v1/clientes")
class RemoverChaveController(
    private val grpcClient: RemoverChaveServiceGrpc.RemoverChaveServiceBlockingStub
) {

    @Delete("/{idCliente}/pix/{pixId}")
    fun removerChave(idCliente: String, pixId: String): HttpResponse<Unit> {
        val request = RemoverChaveRequest.newBuilder()
            .setIdCliente(idCliente)
            .setPixId(pixId)
            .build()

        grpcClient.removerChave(request)

        return HttpResponse.ok()
    }

}