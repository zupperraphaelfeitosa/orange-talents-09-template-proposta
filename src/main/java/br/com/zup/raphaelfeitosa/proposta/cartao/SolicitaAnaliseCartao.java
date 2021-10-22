package br.com.zup.raphaelfeitosa.proposta.cartao;

public class SolicitaAnaliseCartao {

    private String documento;
    private String nome;
    private String idProposta;

    public SolicitaAnaliseCartao(String documento, String nome, String idProposta) {
        this.documento = documento;
        this.nome = nome;
        this.idProposta = idProposta;
    }

    public String getDocumento() {
        return documento;
    }

    public String getNome() {
        return nome;
    }

    public String getIdProposta() {
        return idProposta;
    }
}
