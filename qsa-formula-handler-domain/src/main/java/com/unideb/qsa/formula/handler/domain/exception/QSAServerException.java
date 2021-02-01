package com.unideb.qsa.formula.handler.domain.exception;

/**
 * Exception for internal errors (5xx).
 */
public class QSAServerException extends RuntimeException {

    public QSAServerException(String message) {
        super(message);
    }
}
