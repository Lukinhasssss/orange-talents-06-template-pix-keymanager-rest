package br.com.lukinhasssss.handler

import io.grpc.Status
import io.grpc.StatusRuntimeException
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.hateoas.JsonError
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test

@MicronautTest
internal class ExceptionHandlerTest {

    private val request = HttpRequest.GET<Any>("/")

    @Test
    internal fun `deve retornar 400 quando servidor grpc lancar INVALID_ARGUMENT`(){

        val mensagem = "Dados inválidos"
        val exception = StatusRuntimeException(Status.INVALID_ARGUMENT.withDescription(mensagem))

        val resposta = ExceptionHandler().handle(request, exception)

        assertEquals(HttpStatus.BAD_REQUEST, resposta.status)
        assertNotNull(resposta.body())
        assertEquals(mensagem, (resposta.body() as JsonError).message)

    }

    @Test
    internal fun `deve retornar 403 quando quando servidor grpc lancar PERMISSION_DENIED`(){

        val mensagem = "Chave Pix não pertence ao cliente!"
        val exception = StatusRuntimeException(Status.PERMISSION_DENIED.withDescription(mensagem))

        val resposta = ExceptionHandler().handle(request, exception)

        assertEquals(HttpStatus.FORBIDDEN, resposta.status)
        assertNotNull(resposta.body())
        assertEquals(mensagem, (resposta.body() as JsonError).message)

    }

    @Test
    internal fun `deve retornar 404 quando servidor grpc lancar NOT_FOUND`(){

        val mensagem = "Chave não encontrada"
        val exception = StatusRuntimeException(Status.NOT_FOUND.withDescription(mensagem))

        val resposta = ExceptionHandler().handle(request, exception)

        assertEquals(HttpStatus.NOT_FOUND, resposta.status)
        assertNotNull(resposta.body())
        assertEquals(mensagem, (resposta.body() as JsonError).message)

    }

    @Test
    internal fun `deve retornar 422 quando servidor grpc lancar ALREADY_EXISTS`(){

        val mensagem = "Chave já registrada!"
        val exception = StatusRuntimeException(Status.ALREADY_EXISTS.withDescription(mensagem))

        val resposta = ExceptionHandler().handle(request, exception)

        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, resposta.status)
        assertNotNull(resposta.body())
        assertEquals(mensagem, (resposta.body() as JsonError).message)

    }

    @Test
    internal fun `deve retornar 500 quando quando servidor grpc lancar alguma outra exception`(){

        val mensagem = "Ocorreu um erro ao fazer a requisição para o Banco Central!"
        val exception = StatusRuntimeException(Status.UNKNOWN.withDescription(mensagem))

        val resposta = ExceptionHandler().handle(request, exception)

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, resposta.status)
        assertNotNull(resposta.body())
        assertEquals(mensagem, (resposta.body() as JsonError).message)

    }

}