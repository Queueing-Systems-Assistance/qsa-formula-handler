package com.unideb.qsa.formula.handler.implementation.mapper;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Maps a formula based on the given input values.
 */
@Component
public class FeatureFormulaSymbolMapper {

    @Autowired
    private OutputFeatureFormulaSymbolMapper outputFeatureFormulaSymbolMapper;
    @Autowired
    private InputFeatureFormulaSymbolMapper inputFeatureFormulaSymbolMapper;

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
        String result = inputFeatureFormulaSymbolMapper.map(formula, inputValues);
        return outputFeatureFormulaSymbolMapper.map(systemId, result, inputValues);
    }
}
