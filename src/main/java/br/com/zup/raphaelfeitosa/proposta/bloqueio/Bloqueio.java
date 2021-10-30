package br.com.zup.raphaelfeitosa.proposta.bloqueio;

import br.com.zup.raphaelfeitosa.proposta.cartao.Cartao;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tb_bloqueios")
public class Bloqueio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreationTimestamp
    private LocalDateTime instante;

    @Column(nullable = false)
    private String ipCLiente;

    @Column(nullable = false)
    private String userAgent;

    @Column(nullable = false)
    private String numero;

    @OneToOne(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinColumn(name = "id_cartao", nullable = false)
    private Cartao cartao;

    @Deprecated
    public Bloqueio() {
    }

    public Bloqueio(String ipCLiente, String userAgent, String numero, Cartao cartao) {
        this.ipCLiente = ipCLiente;
        this.userAgent = userAgent;
        this.numero = numero;
        this.cartao = cartao;
    }
}
