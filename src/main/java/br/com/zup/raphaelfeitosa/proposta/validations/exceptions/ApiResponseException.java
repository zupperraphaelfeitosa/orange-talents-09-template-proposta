package br.com.zup.raphaelfeitosa.proposta.validations.exceptions;

import org.springframework.http.HttpStatus;

public class ApiResponseException extends RuntimeException {

    private final String fieldName;
    private final String message;
    private final HttpStatus httpStatus;

    public ApiResponseException(final String fieldName, final String message, final HttpStatus httpStatus) {
        this.message = message;
        this.fieldName = fieldName;
        this.httpStatus = httpStatus;
    }

    public String getFieldName() {
        return fieldName;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
