package br.com.zup.raphaelfeitosa.proposta.biometria;

import br.com.zup.raphaelfeitosa.proposta.cartao.Cartao;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tb_biometrias")
public class Biometria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String fingerPrint;

    @CreationTimestamp
    private LocalDateTime dataCadastro;

    @ManyToOne
    @JoinColumn(name = "id_cartao", nullable = false)
    private Cartao cartao;

    @Deprecated
    public Biometria() {
    }

    public Biometria(String fingerPrint, Cartao cartao) {
        this.fingerPrint = fingerPrint;
        this.cartao = cartao;
    }

    public Long getId() {
        return id;
    }
}
