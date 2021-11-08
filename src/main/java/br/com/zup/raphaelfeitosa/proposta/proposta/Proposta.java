package br.com.zup.raphaelfeitosa.proposta.proposta;

import br.com.zup.raphaelfeitosa.proposta.cartao.Cartao;
import br.com.zup.raphaelfeitosa.proposta.cartao.RetornoAnaliseCartaoServicoAnaliseApi;
import br.com.zup.raphaelfeitosa.proposta.cartao.SolicitaAnaliseCartaoServicoAnaliseApi;
import br.com.zup.raphaelfeitosa.proposta.cartao.StatusAnaliseCartao;
import br.com.zup.raphaelfeitosa.proposta.config.security.Encryptor;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "tb_propostas")
public class Proposta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private String email;

    @Convert(converter = Encryptor.class)
    @Column(nullable = false)
    private String documento;

    @Column(nullable = false)
    private BigDecimal salario;

    @Column(nullable = false)
    private String endereco;

    @Enumerated(EnumType.STRING)
    private StatusProposta status;

    @OneToOne(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinColumn(name = "id_cartao", unique = true)
    private Cartao cartao;

    @Deprecated
    public Proposta() {
    }

    public Proposta(String nome, String email, String documento, BigDecimal salario, String endereco) {
        this.nome = nome;
        this.email = email;
        this.documento = documento;
        this.salario = salario;
        this.endereco = endereco;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public String getDocument() {
        return documento;
    }

    public BigDecimal getSalary() {
        return salario;
    }

    public String getAddress() {
        return endereco;
    }

    public StatusProposta getStatus() {
        return status;
    }

    public Cartao getCartao() {
        return cartao;
    }

    public SolicitaAnaliseCartaoServicoAnaliseApi toSolicitaAnaliseCartao() {
        return new SolicitaAnaliseCartaoServicoAnaliseApi(documento, nome, id.toString());
    }

    public void adicionaRestricao(StatusProposta status) {
        this.status = status;
    }

    public void associaCartao(Cartao cartao) {
        this.cartao = cartao;
    }

    public void verificaRetornoAnalise(RetornoAnaliseCartaoServicoAnaliseApi retornoAnaliseCartaoServicoAnaliseApi) {
        if (retornoAnaliseCartaoServicoAnaliseApi != null && retornoAnaliseCartaoServicoAnaliseApi.getResultadoSolicitacao().equals(StatusAnaliseCartao.SEM_RESTRICAO)) {
            adicionaRestricao(StatusProposta.ELEGIVEL);
        }
    }
}
