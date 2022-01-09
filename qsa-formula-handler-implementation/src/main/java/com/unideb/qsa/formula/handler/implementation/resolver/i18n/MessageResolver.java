package com.unideb.qsa.formula.handler.implementation.resolver.i18n;

import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import com.unideb.qsa.formula.handler.domain.exception.QSANoFormulaAvailableException;
import com.unideb.qsa.formula.handler.domain.localisation.I18nElement;

/**
 * I18n message resolver.
 */
@Component
public class MessageResolver {

    private static final Logger LOG = LoggerFactory.getLogger(MessageResolver.class);
    private static final String INITIALIZING_ERROR_MESSAGE = "Initializing error message [{}]";
    private static final String DEFAULT_EN_US_LOCALE = "en_US";
    private static final String NO_FORMULA_AVAILABLE_I18N_KEY = "error.bad.request.no.formula.found";

    private I18nElement errorMessageI18nElement;

    @Autowired
    private I18nResolver i18nCacheResolver;

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
        return i18nCacheResolver.resolve(List.of(key)).stream().findFirst().orElseThrow(() -> {
            String resolvedLocale = getCurrentLocale(this.errorMessageI18nElement.getValue());
            throw new QSANoFormulaAvailableException(this.errorMessageI18nElement.getValue().get(resolvedLocale));
        });
    }

    private String getCurrentLocale(Map<String, String> values) {
        return values.keySet().stream()
                     .filter(LocaleContextHolder.getLocale().toString()::equalsIgnoreCase)
                     .findFirst()
                     .orElse(DEFAULT_EN_US_LOCALE);
    }
}
