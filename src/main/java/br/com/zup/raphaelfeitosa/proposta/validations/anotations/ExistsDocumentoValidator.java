package br.com.zup.raphaelfeitosa.proposta.validations.anotations;

import br.com.zup.raphaelfeitosa.proposta.proposta.PropostaRepository;
import br.com.zup.raphaelfeitosa.proposta.validations.exceptions.ApiResponseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ExistsDocumentoValidator implements ConstraintValidator<ExistDocumento, String> {

    @Autowired
    private PropostaRepository propostaRepository;

    @Override
    public void initialize(ExistDocumento constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String documento, ConstraintValidatorContext context) {
        if (documento != null) {
            if (propostaRepository.findByDocumento(documento).isPresent())
                throw new ApiResponseException("documento", "Documento já vinculado em uma proposta", HttpStatus.UNPROCESSABLE_ENTITY);
        }
        return true;
    }
}
