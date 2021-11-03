package br.com.zup.raphaelfeitosa.proposta.cartao;

import br.com.zup.raphaelfeitosa.proposta.aviso.AvisoViagemRequest;
import br.com.zup.raphaelfeitosa.proposta.aviso.SolicitaAvisoViagemServicoCartaoApi;
import br.com.zup.raphaelfeitosa.proposta.biometria.Biometria;
import br.com.zup.raphaelfeitosa.proposta.bloqueio.Bloqueio;
import br.com.zup.raphaelfeitosa.proposta.bloqueio.SolicitaBloqueioServicoCartaoApi;
import br.com.zup.raphaelfeitosa.proposta.carteira.CarteiraRequest;
import br.com.zup.raphaelfeitosa.proposta.carteira.SolicitaInclusaoCartaieraServicoCartaoApi;
import br.com.zup.raphaelfeitosa.proposta.proposta.Proposta;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tb_cartoes")
public class Cartao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String numero;

    @Column(nullable = false)
    private LocalDateTime emitidoEm;

    @Column(nullable = false)
    private String titular;

    @Column(nullable = false)
    private Integer limite;

    @Enumerated(EnumType.STRING)
    private StatusCartao status = StatusCartao.DESBLOQUEADO;

    @OneToOne(mappedBy = "cartao")
    private Proposta proposta;

    @OneToMany(mappedBy = "cartao")
    private Set<Biometria> biometrias = new HashSet<>();

    @OneToOne(mappedBy = "cartao")
    private Bloqueio bloqueio;

    @Deprecated
    public Cartao() {
    }

    public Cartao(String numero, LocalDateTime emitidoEm, String titular, Integer limite, Proposta proposta) {
        this.numero = numero;
        this.emitidoEm = emitidoEm;
        this.titular = titular;
        this.limite = limite;
        this.proposta = proposta;
    }

    public String getNumero() {
        return numero;
    }

    public Integer getLimite() {
        return limite;
    }

    public Long getId() {
        return id;
    }

    public void desbloquear() {
        this.status = StatusCartao.DESBLOQUEADO;
    }

    public void bloquear() {
        this.status = StatusCartao.BLOQUEADO;
    }

    public SolicitaBloqueioServicoCartaoApi toNotificacaoBloqueio() {
        return new SolicitaBloqueioServicoCartaoApi("proposta-api");
    }

    public SolicitaInclusaoCartaieraServicoCartaoApi toSolicitaInclusaoCarteira(CarteiraRequest request) {
        return new SolicitaInclusaoCartaieraServicoCartaoApi(request.getEmail(), request.getCarteira().toString());
    }

    public SolicitaAvisoViagemServicoCartaoApi toSolicitaAvisoViagem(AvisoViagemRequest request) {
        return new SolicitaAvisoViagemServicoCartaoApi(request.getDestino(), request.getValidoAte().toString());
    }
}
