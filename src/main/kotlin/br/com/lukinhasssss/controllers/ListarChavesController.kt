package br.com.lukinhasssss.controllers

import br.com.lukinhasssss.ListarChavesRequest
import br.com.lukinhasssss.ListarChavesServiceGrpc
import br.com.lukinhasssss.dto.response.ListagemDasChavesResponse
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get

@Controller("/v1/clientes")
class ListarChavesController(
    private val grpcClient: ListarChavesServiceGrpc.ListarChavesServiceBlockingStub
) {

    @Get("/{idCliente}/pix")
    fun listarChaves(idCliente: String): HttpResponse<List<ListagemDasChavesResponse>> {
        val request = ListarChavesRequest.newBuilder().setIdCliente(idCliente).build()

        val response = grpcClient.listarChaves(request)

        response.chavesList.map {
            ListagemDasChavesResponse(it)
        }.let {
            return HttpResponse.ok(it)
        }
    }

}