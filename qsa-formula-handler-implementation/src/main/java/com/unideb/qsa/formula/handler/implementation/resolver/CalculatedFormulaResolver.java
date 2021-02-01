package com.unideb.qsa.formula.handler.implementation.resolver;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.unideb.qsa.formula.handler.implementation.mapper.FeatureFormulaSymbolMapper;
import com.unideb.qsa.formula.handler.implementation.mapper.FormulaSymbolMapper;

/**
 * Resolver to resolve calculated formulas.
 */
@Component
public class CalculatedFormulaResolver {

    @Autowired
    private FormulaSymbolMapper formulaSymbolMapper;
    @Autowired
    private DefaultFormulaResolver defaultFormulaResolver;
    @Autowired
    private FeatureFormulaSymbolMapper featureFormulaSymbolMapper;

    /**
     * Resolves a calculated formula.
     * @param systemId          system id
     * @param featureId         feature id
     * @param featureConditions input feature values
     * @return resolved formula
     */
    public String resolve(String systemId, String featureId, Map<String, String> featureConditions) {
        String defaultFormula = defaultFormulaResolver.resolve(systemId, featureId);
        String featureIdMappedFormula = formulaSymbolMapper.map(defaultFormula);
        return featureFormulaSymbolMapper.map(systemId, featureIdMappedFormula, featureConditions);
    }
}
