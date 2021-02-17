package com.unideb.qsa.formula.handler.implementation.mapper;

import java.text.DecimalFormat;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Mapper to replace a formula feature symbols with values.
 */
@Component
public class FeatureValueFormulaMapper {

    private static final String FORMULA_NAME_VALUE_MAPPING_REGEXP = "\\\\%s\\b";
    private static final String FORMULA_SIMPLE_NAME_MAPPING_REGEXP = "\\b%s\\b";

    @Autowired
    private DecimalFormat decimalFormat;

    /**
     * Maps the given formula feature symbols (eg.: \Lambda) with it's value (eg.: 4).
     * For example <code>Lambda=1 and Mu=4, then</code><br>
     * <code>
     * \frac{\left(\frac{\Lambda}{\Mu}\right)^2}{1-\frac{\Lambda}{\Mu}}</code>
     * <br>will be<br>
     * <code>\frac{\left(\frac{1}{4}\right)^2}{1-\frac{1}{4}}</code>
     * @param formula  LaTeX formula
     * @param features input or output features
     * @return mapped formula
     */
    public String map(String formula, Map<String, String> features) {
        Map<String, String> formattedFeatures = formatValues(features);
        return formattedFeatures.keySet().stream()
                                .map(featureId -> (Function<String, String>) modifiedFormula -> replaceValue(formattedFeatures, featureId, modifiedFormula))
                                .reduce(Function.identity(), Function::andThen)
                                .apply(formula);
    }

    private String replaceValue(Map<String, String> formattedValues, String featureId, String formula) {
        return formula.replaceAll(String.format(FORMULA_NAME_VALUE_MAPPING_REGEXP, featureId), formattedValues.get(featureId))
                      .replaceAll(String.format(FORMULA_SIMPLE_NAME_MAPPING_REGEXP, featureId), formattedValues.get(featureId));
    }


    private Map<String, String> formatValues(Map<String, String> features) {
        return features.keySet().stream().collect(Collectors.toMap(
                featureId -> featureId,
                featureId -> decimalFormat.format(Double.parseDouble(features.get(featureId)))));
    }
}
