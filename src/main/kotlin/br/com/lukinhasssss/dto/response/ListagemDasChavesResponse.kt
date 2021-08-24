package br.com.lukinhasssss.dto.response

import br.com.lukinhasssss.ListarChavesResponse
import io.micronaut.core.annotation.Introspected
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

@Introspected
class ListagemDasChavesResponse(response: ListarChavesResponse.ChavesPix) {

    val pixId = response.pixId
    val idCliente = response.idCliente
    val tipo = response.tipoChave.name
    val chave = response.valorChave
    val tipoConta = response.tipoConta

    val criadaEm = LocalDateTime.ofEpochSecond(
        response.criadoEm.seconds,
        response.criadoEm.nanos,
        ZoneOffset.UTC
    ).format(DateTimeFormatter.ofPattern("dd/MM/yyyy-hh/ss/nn"))

}