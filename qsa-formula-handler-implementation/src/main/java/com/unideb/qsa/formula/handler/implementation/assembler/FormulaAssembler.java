package com.unideb.qsa.formula.handler.implementation.assembler;

import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.unideb.qsa.formula.handler.implementation.resolver.i18n.MessageResolver;
import com.unideb.qsa.formula.handler.implementation.trimmer.FormulaTrimmer;
import com.unideb.qsa.formula.handler.implementation.trimmer.SimpleFormulaTrimmer;

/**
 * Assembles a formula.
 */
@Component
public class FormulaAssembler {

    private static final String DEFAULT_FORMULA_PATTERN = "feature.element.%s.calculation.default.%s";

    @Autowired
    private MessageResolver i18nServiceClient;
    @Autowired
    private FormulaTrimmer formulaTrimmer;
    @Autowired
    private SimpleFormulaTrimmer simpleFormulaTrimmer;

    /**
     * Assembled a formula by replacing the feature id placeholder between @ characters.
     * It's done by extracting the substring of the formula enclosed by @ characters
     * and substituting it with the corresponding formula stored in the i18n key.
     * Eg.: @Ro@ -> \\frac{\\lambda}{\\mu}.
     * @param systemId system id
     * @param formula  LaTeX formula
     * @return assembled and substituted formula
     */
    public String assemble(String systemId, String formula) {
        Pattern compile = Pattern.compile("@([\\s\\S]*?)@");
        return compile.matcher(formula).replaceAll(matchResult -> {
            String replaceableFormula = getReplaceableFormula(systemId, matchResult);
            replaceableFormula = resolvePlaceholderIfPresent(systemId, replaceableFormula);
            String simpleFormula = simpleFormulaTrimmer.trim(replaceableFormula);
            return addExtraBackslash(simpleFormula);
        });
    }

    private String addExtraBackslash(String simpleFormula) {
        return simpleFormula.replace("\\", "\\\\");
    }

    private String getReplaceableFormula(final String systemId, final java.util.regex.MatchResult matchResult) {
        String replaceableFeatureId = formulaTrimmer.trim(matchResult.group());
        return i18nServiceClient.getI18nKeyValue(String.format(DEFAULT_FORMULA_PATTERN, replaceableFeatureId, systemId));
    }

    private String resolvePlaceholderIfPresent(String systemId, String replaceableFormula) {
        String result = replaceableFormula;
        if (replaceableFormula.contains("@")) {
            result = assemble(systemId, replaceableFormula);
        }
        return result;
    }
}
