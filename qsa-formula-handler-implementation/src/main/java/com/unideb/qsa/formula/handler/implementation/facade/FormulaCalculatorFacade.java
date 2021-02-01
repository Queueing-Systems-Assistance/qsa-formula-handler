package com.unideb.qsa.formula.handler.implementation.facade;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.unideb.qsa.formula.handler.implementation.resolver.CalculatedFormulaResolver;
import com.unideb.qsa.formula.handler.implementation.resolver.DefaultFormulaResolver;
import com.unideb.qsa.formula.handler.implementation.resolver.FormulaStepsResolver;

/**
 * Facade which handles formula calls.
 */
@Component
public class FormulaCalculatorFacade {

    @Autowired
    private DefaultFormulaResolver defaultFormulaResolver;
    @Autowired
    private FormulaStepsResolver formulaStepsResolver;
    @Autowired
    private CalculatedFormulaResolver calculatedFormulaResolver;

    /**
     * Get default formula of a system feature.
     * @param systemId  system id
     * @param featureId feature id
     * @return LaTeX formula string containing default formula corresponding to the parameters
     */
    public String getDefaultFormula(String systemId, String featureId) {
        return defaultFormulaResolver.resolve(systemId, featureId);
    }

    /**
     * Get formula steps of a system feature.
     * @param systemId  system id
     * @param featureId feature id
     * @return LaTeX formula string containing formula steps corresponding to the parameters
     */
    public String getFormulaSteps(String systemId, String featureId) {
        return formulaStepsResolver.resolve(systemId, featureId);
    }

    /**
     * Get calculated formula of a system feature.
     * @param systemId  system id
     * @param featureId feature id
     * @return LaTeX formula string containing calculated formula corresponding to the parameters
     */
    public String getCalculatedFormula(String systemId, String featureId, Map<String, String> featureConditions) {
        return calculatedFormulaResolver.resolve(systemId, featureId, featureConditions);
    }

}
