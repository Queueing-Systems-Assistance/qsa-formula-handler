package com.unideb.qsa.formula.handler.implementation.resolver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.unideb.qsa.formula.handler.implementation.assembler.FormulaAssembler;
import com.unideb.qsa.formula.handler.implementation.resolver.i18n.MessageResolver;

/**
 * Resolver to resolve default formulas.
 */
@Component
public class DefaultFormulaResolver {

    private static final String DEFAULT_FORMULA_PATTERN = "feature.element.%s.calculation.default.%s";

    @Autowired
    private MessageResolver messageResolver;
    @Autowired
    private FormulaAssembler formulaAssembler;

    /**
     * Resolves a default formula.
     * @param systemId  system id
     * @param featureId feature id
     * @return resolved formula
     */
    public String resolve(String systemId, String featureId) {
        String resolvedFormula = messageResolver.getI18nKeyValue(String.format(DEFAULT_FORMULA_PATTERN, featureId, systemId));
        return formulaAssembler.assemble(systemId, resolvedFormula);
    }

}
