package br.com.zup.raphaelfeitosa.proposta.cartao;

public class RetornoAnaliseCartao {

    private String documento;
    private String nome;
    private StatusAnaliseCartao resultadoSolicitacao;
    private String idProposta;


    public RetornoAnaliseCartao(String documento, String nome, StatusAnaliseCartao resultadoSolicitacao, String idProposta) {
        this.documento = documento;
        this.nome = nome;
        this.resultadoSolicitacao = resultadoSolicitacao;
        this.idProposta = idProposta;
    }

    public String getDocumento() {
        return documento;
    }

    public String getNome() {
        return nome;
    }

    public StatusAnaliseCartao getResultadoSolicitacao() {
        return resultadoSolicitacao;
    }

    public String getIdProposta() {
        return idProposta;
    }

}

