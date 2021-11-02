package br.com.zup.raphaelfeitosa.proposta.aviso;

import br.com.zup.raphaelfeitosa.proposta.cartao.Cartao;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "tb_avisos_viagens")
public class AvisoViagem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String destino;

    @Column(nullable = false)
    private LocalDate dataTermino;

    @CreationTimestamp
    private LocalDateTime instante;

    @Column(nullable = false)
    private String ipCliente;

    @Column(nullable = false)
    private String userAgent;

    @ManyToOne
    @JoinColumn(name = "id_cartao", nullable = false)
    private Cartao cartao;

    @Deprecated
    public AvisoViagem(){}

    public AvisoViagem(String destino, LocalDate dataTermino, String ipCliente, String userAgent, Cartao cartao) {
        this.destino = destino;
        this.dataTermino = dataTermino;
        this.ipCliente = ipCliente;
        this.userAgent = userAgent;
        this.cartao = cartao;
    }
}
