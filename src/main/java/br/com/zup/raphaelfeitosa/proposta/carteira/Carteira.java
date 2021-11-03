package br.com.zup.raphaelfeitosa.proposta.carteira;

import br.com.zup.raphaelfeitosa.proposta.cartao.Cartao;

import javax.persistence.*;

@Entity
@Table(name = "tb_carteiras")
public class Carteira {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TipoCarteira carteira;

    @ManyToOne
    @JoinColumn(name = "id_cartao", nullable = false)
    private Cartao cartao;

    @Deprecated
    public Carteira(){}

    public Carteira(String email, TipoCarteira carteira, Cartao cartao) {
        this.email = email;
        this.carteira = carteira;
        this.cartao = cartao;
    }

    public Long getId() {
        return id;
    }
}
