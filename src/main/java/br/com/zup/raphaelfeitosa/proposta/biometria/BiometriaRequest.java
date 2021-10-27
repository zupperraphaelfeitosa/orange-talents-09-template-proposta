package br.com.zup.raphaelfeitosa.proposta.biometria;

import br.com.zup.raphaelfeitosa.proposta.cartao.Cartao;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotBlank;

public class BiometriaRequest {

    @NotBlank
    private String fingerPrint;

    public BiometriaRequest(@JsonProperty("fingerPrint") String fingerPrint) {
        this.fingerPrint = fingerPrint;
    }

    public Biometria toBiometria(Cartao cartao) {
        return new Biometria(fingerPrint, cartao);
    }

    public String getFingerPrint() {
        return fingerPrint;
    }
}
