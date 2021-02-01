package com.unideb.qsa.formula.handler.server.controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.unideb.qsa.formula.handler.implementation.facade.FormulaCalculatorFacade;

/**
 * Controller for system output features.
 */
@RestController
public class FormulaHandlerController {

    @Autowired
    private FormulaCalculatorFacade formulaCalculatorFacade;

    /**
     * Resolves system outputs based on the requested ids.
     * @return the calculated system outputs
     */
    @PostMapping("formula/default/{systemId}/{featureId}")
    public String getDefaultFormula(
            @PathVariable String systemId,
            @PathVariable String featureId) {
        return formulaCalculatorFacade.getDefaultFormula(systemId, featureId);
    }

    /**
     * Resolves streamed system outputs based on the requested ids.
     * @return the calculated stream system outputs
     */
    @PostMapping("formula/steps/{systemId}/{featureId}")
    public String getFormulaSteps(
            @PathVariable String systemId,
            @PathVariable String featureId) {
        return formulaCalculatorFacade.getFormulaSteps(systemId, featureId);
    }

    /**
     * Resolves streamed system outputs based on the requested ids.
     * @return the calculated stream system outputs
     */
    @PostMapping("formula/calculated/{systemId}/{featureId}")
    public String getCalculatedFormula(
            @PathVariable String systemId,
            @PathVariable String featureId,
            @RequestBody Map<String, String> featureConditions) {
        return formulaCalculatorFacade.getCalculatedFormula(systemId, featureId, featureConditions);
    }

}
