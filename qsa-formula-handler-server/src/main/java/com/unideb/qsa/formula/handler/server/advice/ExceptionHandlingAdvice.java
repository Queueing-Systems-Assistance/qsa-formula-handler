package com.unideb.qsa.formula.handler.server.advice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.unideb.qsa.formula.handler.domain.error.ErrorResponse;
import com.unideb.qsa.formula.handler.domain.exception.QSANoFormulaAvailableException;
import com.unideb.qsa.formula.handler.domain.exception.QSAServerException;

/**
 * Advice for exceptions, errors.
 */
@ControllerAdvice
public class ExceptionHandlingAdvice extends ResponseEntityExceptionHandler {

    private static final Logger LOG = LoggerFactory.getLogger(ExceptionHandlingAdvice.class);
    private static final String EXCEPTION_OCCURRED_INTERNAL = "Internal Exception occurred";
    private static final String EXCEPTION_OCCURRED_MESSAGE = "Client Exception occurred [{}]";
    private static final Void EMPTY_BODY = null;

    /**
     * Exception handler for {@link Throwable}.
     * @param exception the exception
     * @return A readable error
     */
    @ExceptionHandler({Throwable.class, QSAServerException.class})
    public ResponseEntity<Void> handleExceptions(Exception exception) {
        LOG.error(EXCEPTION_OCCURRED_INTERNAL, exception);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(EMPTY_BODY);
    }

    /**
     * Exception handler for {@link Throwable}.
     * @param exception the exception
     * @return A readable error
     */
    @ExceptionHandler(QSANoFormulaAvailableException.class)
    public ResponseEntity<ErrorResponse> handleMessageExceptions(Exception exception) {
        LOG.warn(EXCEPTION_OCCURRED_MESSAGE, exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(exception.getMessage()));
    }

    @NonNull
    @Override
    public ResponseEntity<Object> handleHttpMessageNotReadable(
            @NonNull HttpMessageNotReadableException exception,
            @NonNull HttpHeaders headers,
            @NonNull HttpStatus status,
            @NonNull WebRequest webRequest) {
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(EMPTY_BODY);
    }

    @NonNull
    @Override
    public ResponseEntity<Object> handleNoHandlerFoundException(
            @NonNull NoHandlerFoundException exception,
            @NonNull HttpHeaders headers,
            @NonNull HttpStatus status,
            @NonNull WebRequest request) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(EMPTY_BODY);
    }

}
