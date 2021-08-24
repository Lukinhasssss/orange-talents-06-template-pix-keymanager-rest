package br.com.lukinhasssss.controllers

import br.com.lukinhasssss.ConsultarChaveRequest
import br.com.lukinhasssss.ConsultarChaveServiceGrpc
import br.com.lukinhasssss.dto.response.BuscarChaveResponse
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get

@Controller("/v1/clientes")
class ConsultarChaveController(
    private val grpcClient: ConsultarChaveServiceGrpc.ConsultarChaveServiceBlockingStub
) {

    @Get("/{idCliente}/pix/{pixId}")
    fun consultarChave(idCliente: String, pixId: String): HttpResponse<BuscarChaveResponse> {
        val request = ConsultarChaveRequest.FiltroPorPixId.newBuilder()
            .setIdCliente(idCliente)
            .setPixId(pixId)
            .build()

        val response = grpcClient.consultarChave(ConsultarChaveRequest.newBuilder().setPixId(request).build())

        return HttpResponse.ok(BuscarChaveResponse(response))
    }

}