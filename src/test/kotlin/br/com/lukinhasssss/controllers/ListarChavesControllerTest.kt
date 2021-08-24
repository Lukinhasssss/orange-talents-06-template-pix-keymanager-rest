package br.com.lukinhasssss.controllers

import br.com.lukinhasssss.ListarChavesResponse
import br.com.lukinhasssss.ListarChavesServiceGrpc
import br.com.lukinhasssss.TipoChave
import br.com.lukinhasssss.TipoConta
import br.com.lukinhasssss.grpc.PixGrpcClientFactory
import com.google.protobuf.Timestamp
import io.micronaut.context.annotation.Factory
import io.micronaut.context.annotation.Replaces
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import jakarta.inject.Singleton
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import java.time.LocalDateTime
import java.util.*

@MicronautTest
internal class ListarChavesControllerTest {

    @field:Inject
    lateinit var grpcClient: ListarChavesServiceGrpc.ListarChavesServiceBlockingStub

    @field:Inject
    @field:Client("/")
    lateinit var httpClient: HttpClient

    private val idCliente = UUID.randomUUID().toString()

    private val pixId = UUID.randomUUID().toString()


    @Test
    fun `deve listar todas  as chaves de um cliente`(){
        val chavesResponse = ListarChavesResponse.newBuilder()
            .addAllChaves(listOf(
                ListarChavesResponse.ChavesPix.newBuilder()
                    .setPixId(pixId)
                    .setIdCliente(idCliente)
                    .setTipoChave(TipoChave.CELULAR)
                    .setValorChave("+5511987654321")
                    .setTipoConta(TipoConta.CONTA_CORRENTE)
                    .setCriadoEm(Timestamp.newBuilder()
                        .setSeconds(LocalDateTime.now().second.toLong())
                        .setNanos(LocalDateTime.now().nano)
                        .build())
                    .build(),

                ListarChavesResponse.ChavesPix.newBuilder()
                    .setPixId(pixId)
                    .setTipoChave(TipoChave.EMAIL)
                    .setValorChave("luffy@gmail.com")
                    .setCriadoEm(Timestamp.newBuilder()
                        .setSeconds(LocalDateTime.now().second.toLong())
                        .setNanos(LocalDateTime.now().nano)
                        .build())
                    .setTipoConta(TipoConta.CONTA_CORRENTE)
                    .build()))
            .build()

        `when`(grpcClient.listarChaves(any())).thenReturn(chavesResponse)

        val request = HttpRequest.GET<Any>("/v1/clientes/$idCliente/pix")
        val response = httpClient.toBlocking().exchange(request, Any::class.java)

        assertEquals(HttpStatus.OK, response.status)
        assertNotNull(response.body())
    }

    @Factory
    @Replaces(factory = PixGrpcClientFactory::class)
    internal class ListarChaveStubFactory {
        @Singleton
        fun stubMock(): ListarChavesServiceGrpc.ListarChavesServiceBlockingStub {
            return mock(ListarChavesServiceGrpc.ListarChavesServiceBlockingStub::class.java)
        }
    }

}