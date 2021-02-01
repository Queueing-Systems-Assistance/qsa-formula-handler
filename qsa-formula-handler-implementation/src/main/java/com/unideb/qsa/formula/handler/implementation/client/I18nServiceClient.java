package com.unideb.qsa.formula.handler.implementation.client;

import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;

import com.unideb.qsa.formula.handler.domain.exception.QSANoFormulaAvailableException;
import com.unideb.qsa.formula.handler.domain.exception.QSAServerException;
import com.unideb.qsa.formula.handler.domain.localisation.I18nElement;
import com.unideb.qsa.formula.handler.implementation.assembler.HeaderAssembler;

/**
 * Client to call i18n-service.
 */
@Component
public class I18nServiceClient {

    private static final Logger LOG = LoggerFactory.getLogger(I18nServiceClient.class);
    private static final String INITIALIZING_ERROR_MESSAGE = "Initializing error message [{}]";
    private static final String EMPTY_I18N_KEY = "Resolved i18n key [%s] is not present!";
    private static final String DEFAULT_EN_US_LOCALE = "en_US";
    private static final String ERROR_RESPONSE_I18N_SERVICE = "Error response from i18n-service, status code [%s]";
    private static final String NO_FORMULA_AVAILABLE_I18N_KEY = "error.bad.request.no.formula.found";
    private static final int FIRST_ELEMENT = 0;

    @Autowired
    private WebClient webClient;
    @Autowired
    private HeaderAssembler headerAssembler;
    @Value("${qsa.i18n-service.uri}")
    private String i18nServiceUri;
    private I18nElement errorMessageI18nElement;

    /**
     * Resolve an i18n key value based on the current locale (eg. en_US).
     * @param key i18n key
     * @return localised message
     */
    public String getI18nKeyValue(String key) {
        I18nElement i18nElement = resolveI18nKeys(key);
        String currentLocale = getCurrentLocale(i18nElement.getValue());
        return i18nElement.getValue().get(currentLocale);
    }

    @PostConstruct
    private void init() {
        LOG.info(INITIALIZING_ERROR_MESSAGE, NO_FORMULA_AVAILABLE_I18N_KEY);
        this.errorMessageI18nElement = resolveI18nKeys(NO_FORMULA_AVAILABLE_I18N_KEY);
    }

    private I18nElement resolveI18nKeys(String key) {
        var currentLocale = LocaleContextHolder.getLocale();
        return webClient.post()
                        .uri(i18nServiceUri)
                        .headers(httpHeaders -> httpHeaders.addAll(headerAssembler.assemble()))
                        .bodyValue(List.of(key))
                        .retrieve()
                        .onStatus(HttpStatus::isError, clientResponse -> {
                            LocaleContextHolder.setLocale(currentLocale);
                            return createError(clientResponse);
                        })
                        .bodyToMono(new ParameterizedTypeReference<List<I18nElement>>() {})
                        .blockOptional()
                        .orElseThrow(() -> new QSAServerException(String.format(EMPTY_I18N_KEY, key)))
                        .get(FIRST_ELEMENT);
    }

    private String getCurrentLocale(Map<String, String> values) {
        return values.keySet().stream()
                     .filter(LocaleContextHolder.getLocale().toString()::equalsIgnoreCase)
                     .findFirst()
                     .orElse(DEFAULT_EN_US_LOCALE);
    }

    private Mono<Throwable> createError(ClientResponse clientResponse) {
        RuntimeException exception;
        if (clientResponse.statusCode() == HttpStatus.UNPROCESSABLE_ENTITY) {
            String currentLocale = getCurrentLocale(this.errorMessageI18nElement.getValue());
            exception = new QSANoFormulaAvailableException(this.errorMessageI18nElement.getValue().get(currentLocale));
        } else {
            exception = new QSAServerException(String.format(ERROR_RESPONSE_I18N_SERVICE, clientResponse.statusCode()));
        }
        return Mono.error(exception);
    }

}
