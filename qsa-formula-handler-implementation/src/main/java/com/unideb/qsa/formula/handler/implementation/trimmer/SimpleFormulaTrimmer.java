package com.unideb.qsa.formula.handler.implementation.trimmer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Trims a formula to its simplest form.
 */
@Component
public class SimpleFormulaTrimmer {

    @Autowired
    private FormulaTrimmer formulaTrimmer;

    /**
     * Trims a given formula to its simplest form.
     * Eg.: {@code $\\bar{Q} = \\sum_{n=1}^{\\infty}(n-1)P_n = \\frac{\\left(@Ro@\\right)^2}{1-@Ro@}$} into
     * {@code \\frac{\\left(@Ro@\\right)^2}{1-@Ro@}$}
     * @param formula LaTeX formula
     * @return trimmed formula
     */
    public String trim(String formula) {
        String simpleFormula = formula.replaceAll(".*=", "");
        return formulaTrimmer.trim(simpleFormula);
    }
}
