package br.com.zup.raphaelfeitosa.proposta.proposta;

import br.com.zup.raphaelfeitosa.proposta.validations.anotations.CPForCNPJ;
import br.com.zup.raphaelfeitosa.proposta.validations.anotations.ExistDocumento;

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
    @ExistDocumento
    private String documento;

    @NotNull
    @Positive
    private BigDecimal salario;

    @NotBlank
    private String endereco;

    public PropostaRequest(String name, String email, String documento, BigDecimal salario, String endereco) {
        this.name = name;
        this.email = email;
        this.documento = documento;
        this.salario = salario;
        this.endereco = endereco;
    }

    public Proposta toProposta() {
        return new Proposta(name, email, documento, salario, endereco);
    }
}
