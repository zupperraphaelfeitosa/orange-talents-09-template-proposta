package br.com.zup.raphaelfeitosa.proposta.carteira;

public class RetornoCateiraServicoCartaoApi {
    private StatusCarteira resultado;
    private String id;

    public RetornoCateiraServicoCartaoApi(StatusCarteira resultado, String id) {
        this.resultado = resultado;
        this.id = id;
    }

    public StatusCarteira getResultado() {
        return resultado;
    }

    public String getId() {
        return id;
    }
}
