package br.com.zup.raphaelfeitosa.proposta.aviso;

import java.time.LocalDate;

public class SolicitaAvisoViagemServicoCartaoApi {

    private String destino;
    private String validoAte;

    public SolicitaAvisoViagemServicoCartaoApi(String destino, String validoAte) {
        this.destino = destino;
        this.validoAte = validoAte;
    }

    public String getDestino() {
        return destino;
    }

    public String getValidoAte() {
        return validoAte;
    }
}
