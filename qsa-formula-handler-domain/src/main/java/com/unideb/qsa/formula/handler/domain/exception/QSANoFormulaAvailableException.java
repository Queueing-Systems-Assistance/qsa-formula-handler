package com.unideb.qsa.formula.handler.domain.exception;

/**
 * Exception when no formula found.
 */
public class QSANoFormulaAvailableException extends QSAClientException {

    private final String message;

    public QSANoFormulaAvailableException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
