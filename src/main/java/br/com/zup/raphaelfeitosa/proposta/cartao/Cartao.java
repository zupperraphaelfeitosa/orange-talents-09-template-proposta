package br.com.zup.raphaelfeitosa.proposta.cartao;

import br.com.zup.raphaelfeitosa.proposta.proposta.Proposta;

import javax.persistence.*;
import java.time.LocalDateTime;

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

    @OneToOne(mappedBy = "cartao")
    private Proposta proposta;

    @Deprecated
    public Cartao(){}

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
}
