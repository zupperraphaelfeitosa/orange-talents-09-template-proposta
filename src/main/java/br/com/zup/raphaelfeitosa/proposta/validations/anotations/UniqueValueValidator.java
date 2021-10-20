package br.com.zup.raphaelfeitosa.proposta.validations.anotations;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UniqueValueValidator implements ConstraintValidator<UniqueValue, Object> {

    @PersistenceContext
    EntityManager entityManager;

    private String domainclass;
    private String field;


    @Override
    public void initialize(UniqueValue constraintAnnotation) {
        domainclass = constraintAnnotation.domainClass().getSimpleName();
        field = constraintAnnotation.fieldName();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext constraintValidatorContext) {
        if (value == null) {
            return false;
        }
        Query query = entityManager.createQuery("SELECT c FROM " + domainclass + " c WHERE " + field + " = :VALUE");
        query.setParameter("VALUE", value);
        return query.getResultList().isEmpty();
    }
}
