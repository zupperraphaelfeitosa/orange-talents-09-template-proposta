package br.com.zup.raphaelfeitosa.proposta.bloqueio;

public class SolicitaBloqueioServicoCartaoApi {

    private String sistemaResponsavel;

    public SolicitaBloqueioServicoCartaoApi(String sistemaResponsavel) {
        this.sistemaResponsavel = sistemaResponsavel;
    }

    public String getSistemaResponsavel() {
        return sistemaResponsavel;
    }
}
