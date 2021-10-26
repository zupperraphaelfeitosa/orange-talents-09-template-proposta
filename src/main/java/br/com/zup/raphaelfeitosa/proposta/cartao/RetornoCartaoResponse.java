package br.com.zup.raphaelfeitosa.proposta.cartao;

import br.com.zup.raphaelfeitosa.proposta.proposta.Proposta;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public class RetornoCartaoResponse {

    @JsonProperty("id")
    private String numero;

    private LocalDateTime emitidoEm;

    private String titular;

    private Integer limite;

    public RetornoCartaoResponse(String numero, LocalDateTime emitidoEm, String titular, Integer limite) {
        this.numero = numero;
        this.emitidoEm = emitidoEm;
        this.titular = titular;
        this.limite = limite;
    }

    public Cartao toCartao(Proposta proposta) {
    return new Cartao(numero, emitidoEm, titular, limite, proposta);
    }
}
