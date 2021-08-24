package br.com.lukinhasssss.dto.response

import br.com.lukinhasssss.ConsultarChaveResponse
import io.micronaut.core.annotation.Introspected
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

@Introspected
class DetalhesDaChaveResponse(response: ConsultarChaveResponse) {

    val pixId = response.pixId
    val idCliente = response.idCliente
    val tipo = response.tipoChave.name
    val chave = response.chave

    val conta = mapOf(
        Pair("instituicao", response.conta.instituicao),
        Pair("tipoConta", response.conta.tipoConta),
        Pair("agencia", response.conta.agencia),
        Pair("numero", response.conta.numero)
    )

    val criadaEm = LocalDateTime.ofEpochSecond(
        response.criadoEm.seconds,
        response.criadoEm.nanos,
        ZoneOffset.UTC
    ).format(DateTimeFormatter.ofPattern("dd/MM/yyyy-hh/ss/nn"))

}