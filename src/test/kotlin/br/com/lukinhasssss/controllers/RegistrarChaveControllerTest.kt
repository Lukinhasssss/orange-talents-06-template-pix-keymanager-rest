package br.com.lukinhasssss.controllers

import br.com.lukinhasssss.*
import br.com.lukinhasssss.grpc.PixGrpcClientFactory
import io.micronaut.context.annotation.Factory
import io.micronaut.context.annotation.Replaces
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import jakarta.inject.Singleton
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.*
import java.util.*

@MicronautTest
internal class RegistrarChaveControllerTest {

    @field:Inject
    lateinit var grpcClient: RegistrarChaveServiceGrpc.RegistrarChaveServiceBlockingStub

    @field:Inject
    @field:Client("/")
    lateinit var httpClient: HttpClient

    private val idCliente = UUID.randomUUID().toString()

    private val pixId = UUID.randomUUID().toString()

    private val respostaGrpc = RegistrarChaveResponse.newBuilder()
        .setPixId(pixId)
        .build()

    @Test
    fun `Deve registrar uma nova chave pix`(){
        val cadastrarChaveRequest = object {
            val tipoChave = TipoChave.CELULAR
            val valorChave = "+5511987654321"
            val tipoConta = TipoConta.CONTA_CORRENTE
        }

        `when`(grpcClient.registrarChave(any())).thenReturn(respostaGrpc)

        val request = HttpRequest.POST("/v1/clientes/$idCliente/pix", cadastrarChaveRequest)

        val response = httpClient.toBlocking().exchange(request, RegistrarChaveRequest::class.java)

        with(response) {
            assertEquals(HttpStatus.CREATED, status)
            assertTrue(headers.contains("Location"))
            assertTrue(header("Location")!!.contains(pixId))
        }
    }

    @Test
    internal fun `nao deve registrar uma nova chave pix quando o celular for invalido`() {
        val cadastrarChaveRequest = object {
            val tipoChave = TipoChave.CELULAR
            val valorChave = "segbsggnsrh"
            val tipoConta = TipoConta.CONTA_CORRENTE
        }

        `when`(grpcClient.registrarChave(any())).thenReturn(respostaGrpc)

        val request = HttpRequest.POST("/v1/clientes/$idCliente/pix", cadastrarChaveRequest)

        val exception = assertThrows<HttpClientResponseException> {
            httpClient.toBlocking().exchange(request, RegistrarChaveRequest::class.java)
        }

        with(exception) {
            assertEquals(HttpStatus.BAD_REQUEST.code, status.code)
        }
    }

    @Test
    internal fun `nao deve registrar uma nova chave pix quando o cpf for invalido`() {
        val cadastrarChaveRequest = object {
            val tipoChave = TipoChave.CPF
            val valorChave = "931351"
            val tipoConta = TipoConta.CONTA_CORRENTE
        }

        val request = HttpRequest.POST("/v1/clientes/$idCliente/pix", cadastrarChaveRequest)

        `when`(grpcClient.registrarChave(any())).thenReturn(respostaGrpc)

        val exception = assertThrows<HttpClientResponseException> {
            httpClient.toBlocking().exchange(request, RegistrarChaveRequest::class.java)
        }

        with(exception) {
            assertEquals(HttpStatus.BAD_REQUEST.code, status.code)
        }
    }

    @Test
    internal fun `nao deve registrar uma nova chave pix quando o email for invalido`() {
        val cadastrarChaveRequest = object {
            val tipoChave = TipoChave.EMAIL
            val valorChave = "luffy@luffy"
            val tipoConta = TipoConta.CONTA_CORRENTE
        }

        val request = HttpRequest.POST("/v1/clientes/$idCliente/pix", cadastrarChaveRequest)

        `when`(grpcClient.registrarChave(any())).thenReturn(respostaGrpc)

        val exception = assertThrows<HttpClientResponseException> {
            httpClient.toBlocking().exchange(request, RegistrarChaveRequest::class.java)
        }

        with(exception) {
            assertEquals(HttpStatus.BAD_REQUEST.code, status.code)
        }
    }

    @Test
    internal fun `nao deve registrar uma nova chave pix quando a chave for aleatoria e o valor estiver preenchido`() {
        val cadastrarChaveRequest = object {
            val tipoChave = TipoChave.ALEATORIA
            val valorChave = "987654321"
            val tipoConta = TipoConta.CONTA_CORRENTE
        }

        val request = HttpRequest.POST("/v1/clientes/$idCliente/pix", cadastrarChaveRequest)

        `when`(grpcClient.registrarChave(any())).thenReturn(respostaGrpc)

        val exception = assertThrows<HttpClientResponseException> {
            httpClient.toBlocking().exchange(request, RegistrarChaveRequest::class.java)
        }

        with(exception) {
            assertEquals(HttpStatus.BAD_REQUEST.code, status.code)
        }
    }


    @Factory
    @Replaces(factory = PixGrpcClientFactory::class)
    internal class RegistrarChaveStubFactory {
        @Singleton
        fun stubMock(): RegistrarChaveServiceGrpc.RegistrarChaveServiceBlockingStub {
            return mock(RegistrarChaveServiceGrpc.RegistrarChaveServiceBlockingStub::class.java)
        }
    }

}