package com.unideb.qsa.formula.handler.implementation.mapper;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Maps the given formula QSA feature ids to the input values.
 */
@Component
public class InputFeatureFormulaSymbolMapper {

    private static final String FORMULA_NAME_VALUE_MAPPING_REGEXP = "\\\\%s\\b";

    @Autowired
    private OutputValueStringMapper outputValueStringMapper;

    /**
     * Maps the given formula QSA feature ids (eg.: \Lambda) to the given input values (eg.: 3).
     * @param formula     LaTeX formula
     * @param inputValues feature input values (eg.: Lambda 3, Mu 2)
     * @return mapped formula
     */
    public String map(String formula, Map<String, String> inputValues) {
        Map<String, String> formattedValues = formatValues(inputValues);
        return formattedValues.keySet().stream()
                              .map(featureId -> (Function<String, String>) s -> replaceValue(formattedValues, featureId, s))
                              .reduce(Function.identity(), Function::andThen)
                              .apply(formula);
    }

    private String replaceValue(Map<String, String> formattedValues, String featureId, String formula) {
        return formula.replaceAll(String.format(FORMULA_NAME_VALUE_MAPPING_REGEXP, featureId), formattedValues.get(featureId));
    }

    private Map<String, String> formatValues(Map<String, String> outputValues) {
        return outputValues.keySet().stream().collect(Collectors.toMap(
                featureId -> featureId,
                featureId -> outputValueStringMapper.map(outputValues.get(featureId))));
    }
}
