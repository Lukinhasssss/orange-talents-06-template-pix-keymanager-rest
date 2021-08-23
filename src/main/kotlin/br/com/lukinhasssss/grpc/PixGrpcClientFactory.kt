package br.com.lukinhasssss.grpc

import br.com.lukinhasssss.ConsultarChaveServiceGrpc
import br.com.lukinhasssss.ListarChavesServiceGrpc
import br.com.lukinhasssss.RegistrarChaveServiceGrpc
import br.com.lukinhasssss.RemoverChaveServiceGrpc
import io.grpc.ManagedChannel
import io.micronaut.context.annotation.Factory
import io.micronaut.grpc.annotation.GrpcChannel
import jakarta.inject.Singleton

@Factory
class PixGrpcClientFactory {

    @Singleton
    fun registrarChave(@GrpcChannel("pix") channel: ManagedChannel): RegistrarChaveServiceGrpc.RegistrarChaveServiceBlockingStub? {
        return RegistrarChaveServiceGrpc.newBlockingStub(channel)
    }

    @Singleton
    fun removerChave(@GrpcChannel("pix") channel: ManagedChannel): RemoverChaveServiceGrpc.RemoverChaveServiceBlockingStub? {
        return RemoverChaveServiceGrpc.newBlockingStub(channel)
    }

    @Singleton
    fun consultarChave(@GrpcChannel("pix") channel: ManagedChannel): ConsultarChaveServiceGrpc.ConsultarChaveServiceBlockingStub? {
        return ConsultarChaveServiceGrpc.newBlockingStub(channel)
    }

    @Singleton
    fun listarChave(@GrpcChannel("pix") channel: ManagedChannel): ListarChavesServiceGrpc.ListarChavesServiceBlockingStub? {
        return ListarChavesServiceGrpc.newBlockingStub(channel)
    }

}