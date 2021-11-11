package br.com.zup.raphaelfeitosa.proposta.proposta;

import java.math.BigDecimal;

import static br.com.zup.raphaelfeitosa.proposta.config.util.OfuscaDadoSensivel.*;

public class AcompanhamentoPropostaResponse {

    private String nome;
    private String email;
    private String documento;
    private BigDecimal salario;
    private String endereco;
    private StatusProposta status;
    private String cartao;

    public AcompanhamentoPropostaResponse(Proposta proposta) {
        this.nome = ofuscaNome(proposta.getName());
        this.email = ofuscaEmail(proposta.getEmail());
        this.documento = ofuscaDocumento(proposta.getDocument());
        this.salario = proposta.getSalary();
        this.endereco = proposta.getAddress();
        this.status = proposta.getStatus();
        this.cartao = proposta.getCartao() != null ? ofuscaCartao(proposta.getCartao().getNumero()) : "CARTÃO NÃO ELEGIVEL";
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public String getDocumento() {
        return documento;
    }

    public BigDecimal getSalario() {
        return salario;
    }

    public String getEndereco() {
        return endereco;
    }

    public StatusProposta getStatus() {
        return status;
    }

    public String getCartao() {
        return cartao;
    }
}
