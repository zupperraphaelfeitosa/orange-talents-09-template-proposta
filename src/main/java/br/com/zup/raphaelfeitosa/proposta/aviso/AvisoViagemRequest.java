package br.com.zup.raphaelfeitosa.proposta.aviso;

import br.com.zup.raphaelfeitosa.proposta.cartao.Cartao;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

public class AvisoViagemRequest {

    @NotBlank
    private String destino;

    @Future
    @NotNull
    private LocalDate dataTermino;

    public AvisoViagemRequest(String destino, LocalDate dataTermino) {
        this.destino = destino;
        this.dataTermino = dataTermino;
    }

    public AvisoViagem toAvisoViagem(String ipCliente, String userAgent, Cartao cartao) {
        AvisoViagem avisoViagem = new AvisoViagem(destino, dataTermino, ipCliente, userAgent, cartao);
        System.out.println(avisoViagem);
        return avisoViagem;
    }
}
