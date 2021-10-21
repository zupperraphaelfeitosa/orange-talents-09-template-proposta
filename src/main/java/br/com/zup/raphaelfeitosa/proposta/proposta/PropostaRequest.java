package br.com.zup.raphaelfeitosa.proposta.proposta;

import br.com.zup.raphaelfeitosa.proposta.validations.anotations.CPForCNPJ;
import br.com.zup.raphaelfeitosa.proposta.validations.anotations.ExistDocument;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

public class PropostaRequest {

    @NotBlank
    private String name;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    @CPForCNPJ
    @ExistDocument
    private String document;

    @NotNull
    @Positive
    private BigDecimal salary;

    @NotBlank
    private String address;

    public PropostaRequest(String name, String email, String document, BigDecimal salary, String address) {
        this.name = name;
        this.email = email;
        this.document = document;
        this.salary = salary;
        this.address = address;
    }

    public Proposta toProposta() {
        return new Proposta(name, email, document, salary, address);
    }
}
