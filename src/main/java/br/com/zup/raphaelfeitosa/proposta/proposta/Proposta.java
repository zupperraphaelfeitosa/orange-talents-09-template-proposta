package br.com.zup.raphaelfeitosa.proposta.proposta;

import br.com.zup.raphaelfeitosa.proposta.cartao.Cartao;
import br.com.zup.raphaelfeitosa.proposta.cartao.RetornoAnaliseCartao;
import br.com.zup.raphaelfeitosa.proposta.cartao.SolicitaAnaliseCartao;
import br.com.zup.raphaelfeitosa.proposta.cartao.StatusAnaliseCartao;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "tb_propostas")
public class Proposta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String document;

    @Column(nullable = false)
    private BigDecimal salary;

    @Column(nullable = false)
    private String address;

    @Enumerated(EnumType.STRING)
    private StatusProposta status;

    @OneToOne(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinColumn(name = "id_cartao", unique = true)
    private Cartao cartao;

    @Deprecated
    public Proposta() {
    }

    public Proposta(String name, String email, String document, BigDecimal salary, String address) {
        this.name = name;
        this.email = email;
        this.document = document;
        this.salary = salary;
        this.address = address;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getDocument() {
        return document;
    }

    public BigDecimal getSalary() {
        return salary;
    }

    public String getAddress() {
        return address;
    }

    public StatusProposta getStatus() {
        return status;
    }

    public Cartao getCartao() {
        return cartao;
    }

    public SolicitaAnaliseCartao toSolicitaAnaliseCartao() {
        return new SolicitaAnaliseCartao(document, name, id.toString());
    }

    public void adicionaRestricao(StatusProposta status) {
        this.status = status;
    }

    public void associaCartao(Cartao cartao) {
        this.cartao = cartao;
    }

    public void verificaRetornoAnalise(RetornoAnaliseCartao retornoAnaliseCartao) {
        if (retornoAnaliseCartao != null && retornoAnaliseCartao.getResultadoSolicitacao().equals(StatusAnaliseCartao.SEM_RESTRICAO)) {
            adicionaRestricao(StatusProposta.ELEGIVEL);
        }
    }
}
