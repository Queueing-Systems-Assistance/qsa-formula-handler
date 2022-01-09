package com.unideb.qsa.formula.handler.implementation.config;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Arrays;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.LoadingCache;

import com.unideb.qsa.config.resolver.config.ConfigPackResolverConfiguration;
import com.unideb.qsa.config.resolver.resolver.ConfigResolver;
import com.unideb.qsa.formula.handler.domain.localisation.I18nElement;
import com.unideb.qsa.formula.handler.implementation.resolver.i18n.I18nCacheLoader;

/**
 * Configuration for the implementation module.
 */
@Configuration
public class ImplementationConfig {

    private static final DecimalFormatSymbols DECIMAL_FORMAT_LOCALE_EN = DecimalFormatSymbols.getInstance(Locale.ENGLISH);
    private static final int EXPIRE_DURATION_TWO_DAYS_IN_HOURS = 48;

    @Value("${qsa.config.uris}")
    private String[] configPackUris;
    @Value("${qsa.config.refresh-rate-in-minutes}")
    private int refreshRate;
    @Value("${qsa.formula-handler.feature-value-format-pattern}")
    private String featureValueFormatPattern;

    @Autowired
    private I18nCacheLoader i18nCacheLoader;

    @Bean
    public ConfigResolver configResolver() {
        return new ConfigPackResolverConfiguration(Arrays.asList(configPackUris), refreshRate).createConfigResolver();
    }

    @Bean
    public DecimalFormat decimalFormat() {
        return new DecimalFormat(featureValueFormatPattern, DECIMAL_FORMAT_LOCALE_EN);
    }

    @Bean
    public LoadingCache<String, I18nElement> i18nCache() {
        return CacheBuilder.newBuilder()
                           .expireAfterWrite(EXPIRE_DURATION_TWO_DAYS_IN_HOURS, TimeUnit.HOURS)
                           .build(i18nCacheLoader);
    }
}
