package br.com.zup.raphaelfeitosa.proposta.proposta;

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

    @Deprecated
    public Proposta(){}

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
}
