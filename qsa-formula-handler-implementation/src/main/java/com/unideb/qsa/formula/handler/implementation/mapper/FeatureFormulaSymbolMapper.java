package com.unideb.qsa.formula.handler.implementation.mapper;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.unideb.qsa.formula.handler.implementation.client.CalculatorClient;

/**
 * Maps a formula based on the given input values.
 */
@Component
public class FeatureFormulaSymbolMapper {

    @Autowired
    private CalculatorClient calculatorClient;
    @Autowired
    private FeatureValueFormulaMapper featureValueFormulaMapper;

    /**
     * Map the given formula feature ids with the given values.
     * for example: given formula \right{\Lambda/\Mu} and values: Lambda: 2, Mu 3.
     * Maps the formula to \right{2/3}.
     * @param systemId    system id
     * @param formula     LaTeX formula
     * @param inputValues feature values
     * @return mapped formula
     */
    public String map(String systemId, String formula, Map<String, String> inputValues) {
        Map<String, String> outputValues = calculatorClient.getFeatureOutputs(systemId, inputValues);
        Map<String, String> values = combineFeatures(inputValues, outputValues);
        return featureValueFormulaMapper.map(formula, values);
    }

    private Map<String, String> combineFeatures(Map<String, String> inputValues, Map<String, String> outputValues) {
        return Stream.concat(inputValues.entrySet().stream(), outputValues.entrySet().stream())
                     .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}
