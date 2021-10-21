package br.com.zup.raphaelfeitosa.proposta.validations.anotations;

import br.com.zup.raphaelfeitosa.proposta.proposta.PropostaRepository;
import br.com.zup.raphaelfeitosa.proposta.validations.exceptions.ApiResponseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ExistsDocumentValidator implements ConstraintValidator<ExistDocument, String> {

    @Autowired
    private PropostaRepository propostaRepository;

    @Override
    public void initialize(ExistDocument constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String document, ConstraintValidatorContext context) {
        if (document != null) {
            if (!propostaRepository.findByDocument(document).isEmpty()) {
                throw new ApiResponseException("document", "Documento já vinculado em uma proposta", HttpStatus.UNPROCESSABLE_ENTITY);
            }
        }
        return true;
    }
}
