package br.com.lukinhasssss.controllers

import br.com.lukinhasssss.ConsultarChaveResponse
import br.com.lukinhasssss.ConsultarChaveServiceGrpc
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
internal class ConsultarChaveControllerTest {

    @field:Inject
    lateinit var grpcClient: ConsultarChaveServiceGrpc.ConsultarChaveServiceBlockingStub

    @field:Inject
    @field:Client("/")
    lateinit var httpClient: HttpClient

    private val idCliente = UUID.randomUUID().toString()

    private val pixId = UUID.randomUUID().toString()


    @Test
    fun `Deve consultar os detalhes de uma chave`(){
        val buscarChaveResponse = ConsultarChaveResponse.newBuilder()
            .setIdCliente(idCliente)
            .setPixId(pixId)
            .setNome("Monkey D. Luffy")
            .setCpf("95375185246")
            .setTipoChave(ConsultarChaveResponse.TipoChave.CELULAR)
            .setChave("+5511987654321")
            .setConta(ConsultarChaveResponse.Conta.newBuilder()
                .setInstituicao("Lorem Ipsum")
                .setAgencia("0001")
                .setNumero("94568741")
                .setTipoConta(ConsultarChaveResponse.Conta.TipoConta.CONTA_CORRENTE)
                .build()
            )
            .setCriadoEm(Timestamp.newBuilder()
                .setSeconds(LocalDateTime.now().second.toLong())
                .setNanos(LocalDateTime.now().nano)
                .build())
            .build()

        `when`(grpcClient.consultarChave(any()))
            .thenReturn(buscarChaveResponse)

        val request = HttpRequest.GET<Any>("/v1/clientes/$idCliente/pix/$pixId")
        val response = httpClient.toBlocking().exchange(request, Any::class.java)

        assertEquals(HttpStatus.OK, response.status)
        assertNotNull(response.body())
    }


    @Factory
    @Replaces(factory = PixGrpcClientFactory::class)
    internal class ConsultarChaveStubFactory {
        @Singleton
        fun stubMock(): ConsultarChaveServiceGrpc.ConsultarChaveServiceBlockingStub {
            return mock(ConsultarChaveServiceGrpc.ConsultarChaveServiceBlockingStub::class.java)
        }
    }
}