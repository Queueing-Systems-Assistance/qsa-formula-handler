package com.unideb.qsa.formula.handler.implementation.mapper;

import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.unideb.qsa.config.resolver.resolver.ConfigResolver;
import com.unideb.qsa.domain.context.Qualifier;
import com.unideb.qsa.formula.handler.implementation.trimmer.SimpleFormulaTrimmer;

/**
 * Maps formula LaTeX feature ids to QSA feature ids.
 */
@Component
public class FormulaSymbolMapper {

    private static final String CONFIG_CALCULATION_NAME_MAPPING = "CALCULATION_NAME_MAPPING";
    private static final Qualifier QUALIFIER = new Qualifier.Builder().build();

    @Autowired
    private SimpleFormulaTrimmer simpleFormulaTrimmer;
    @Autowired
    private ConfigResolver configResolver;

    /**
     * Map the given formula LaTeX feature ids (eg.: \lambda) to the QSA corresponding ones (eg.: \Lambda).
     * @param formula LaTeX formula
     * @return mapped formula
     */
    public String map(String formula) {
        String simpleFormula = simpleFormulaTrimmer.trim(formula);
        Map<String, String> nameMappings = configResolver.resolve(CONFIG_CALCULATION_NAME_MAPPING, QUALIFIER, Map.class).orElseThrow();
        return nameMappings.keySet().stream()
                           .map(key -> (Function<String, String>) s -> s.replace(nameMappings.get(key), "\\" + key))
                           .reduce(Function.identity(), Function::andThen)
                           .apply(simpleFormula);
    }
}
