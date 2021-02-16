package com.unideb.qsa.formula.handler.implementation.config;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Arrays;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.unideb.qsa.config.resolver.config.ConfigPackResolverConfiguration;
import com.unideb.qsa.config.resolver.resolver.ConfigResolver;

/**
 * Configuration for the implementation module.
 */
@Configuration
public class ImplementationConfig {

    private static final DecimalFormatSymbols DECIMAL_FORMAT_LOCALE_EN = DecimalFormatSymbols.getInstance(Locale.ENGLISH);

    @Value("${qsa.config.uris}")
    private String[] configPackUris;
    @Value("${qsa.config.refresh-rate-in-minutes}")
    private int refreshRate;
    @Value("${qsa.formula-handler.feature-value-format-pattern}")
    private String featureValueFormatPattern;

    @Bean
    public ConfigResolver configResolver() {
        return new ConfigPackResolverConfiguration(Arrays.asList(configPackUris), refreshRate).createConfigResolver();
    }

    @Bean
    public DecimalFormat decimalFormat() {
        return new DecimalFormat(featureValueFormatPattern, DECIMAL_FORMAT_LOCALE_EN);
    }
}
