package br.com.lukinhasssss.controllers

import br.com.lukinhasssss.RemoverChaveResponse
import br.com.lukinhasssss.RemoverChaveServiceGrpc
import br.com.lukinhasssss.grpc.PixGrpcClientFactory
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
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import java.util.*

@MicronautTest
internal class RemoverChaveControllerTest {

    @field:Inject
    lateinit var grpcClient: RemoverChaveServiceGrpc.RemoverChaveServiceBlockingStub

    @field:Inject
    @field:Client("/")
    lateinit var httpClient: HttpClient

    private val idCliente = UUID.randomUUID()

    private val pixId = UUID.randomUUID()

    @Test
    fun `deve remover uma chave pix`() {
        val respostaGrpc = RemoverChaveResponse.newBuilder().build()

        `when`(grpcClient.removerChave(any())).thenReturn(respostaGrpc)

        val request = HttpRequest.DELETE<Any>("/v1/clientes/$idCliente/pix/$pixId")

        val response = httpClient.toBlocking().exchange(request, Any::class.java)

        assertEquals(HttpStatus.OK, response.status)
    }


    @Factory
    @Replaces(factory = PixGrpcClientFactory::class)
    internal class RemoverChaveStubFactory {
        @Singleton
        internal fun removeStubMock(): RemoverChaveServiceGrpc.RemoverChaveServiceBlockingStub {
            return mock(RemoverChaveServiceGrpc.RemoverChaveServiceBlockingStub::class.java)
        }
    }

}