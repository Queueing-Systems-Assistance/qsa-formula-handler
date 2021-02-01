package com.unideb.qsa.formula.handler.implementation.resolver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.unideb.qsa.formula.handler.implementation.assembler.FormulaAssembler;
import com.unideb.qsa.formula.handler.implementation.client.I18nServiceClient;

/**
 * Resolver to resolve formula steps.
 */
@Component
public class FormulaStepsResolver {

    private static final String FORMULA_STEPS_PATTERN = "feature.element.%s.calculation.steps.%s";

    @Autowired
    private I18nServiceClient i18nServiceClient;
    @Autowired
    private FormulaAssembler formulaAssembler;

    /**
     * Resolves formula steps.
     * @param systemId  system id
     * @param featureId feature id
     * @return resolved formula
     */
    public String resolve(String systemId, String featureId) {
        String resolvedFormula = i18nServiceClient.getI18nKeyValue(String.format(FORMULA_STEPS_PATTERN, featureId, systemId));
        return formulaAssembler.assemble(systemId, resolvedFormula);
    }

}
