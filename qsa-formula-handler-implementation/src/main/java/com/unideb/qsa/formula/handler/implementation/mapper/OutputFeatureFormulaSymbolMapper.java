package com.unideb.qsa.formula.handler.implementation.mapper;

import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.unideb.qsa.formula.handler.implementation.client.CalculatorClient;

/**
 * Maps a formula based on the given output values which calculated by the calculator based on the input values.
 */
@Component
public class OutputFeatureFormulaSymbolMapper {

    @Autowired
    private CalculatorClient calculatorClient;
    @Autowired
    private OutputValueStringMapper outputValueStringMapper;

    /**
     * Maps the given formula QSA feature ids (eg.: \Lambda) to the calculated output values from the given input values.
     * @param formula           LaTeX formula
     * @param featureConditions input values (eg.: Lambda 3, Mu 2)
     * @return mapped formula
     */
    public String map(String systemId, String formula, Map<String, String> featureConditions) {
        Map<String, String> outputValues = calculatorClient.getFeatureOutputs(systemId, featureConditions);
        Map<String, String> formattedValues = formatValues(outputValues);
        return formattedValues.keySet().stream()
                              .map(outputFeatureId -> formula.replaceAll(outputFeatureId, formattedValues.get(outputFeatureId)))
                              .findFirst()
                              .orElse(formula);
    }

    private Map<String, String> formatValues(Map<String, String> outputValues) {
        return outputValues.keySet().stream().collect(Collectors.toMap(
                featureId -> featureId,
                featureId -> outputValueStringMapper.map(outputValues.get(featureId))));
    }
}
