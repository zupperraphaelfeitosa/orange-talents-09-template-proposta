package br.com.zup.raphaelfeitosa.proposta.bloqueio;

public class SolicitaBloqueio {

    private String sistemaResponsavel;

    public SolicitaBloqueio(String sistemaResponsavel) {
        this.sistemaResponsavel = sistemaResponsavel;
    }

    public String getSistemaResponsavel() {
        return sistemaResponsavel;
    }
}
