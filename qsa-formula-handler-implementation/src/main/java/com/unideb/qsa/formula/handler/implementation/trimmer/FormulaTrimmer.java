package com.unideb.qsa.formula.handler.implementation.trimmer;

import org.springframework.stereotype.Component;

/**
 * Trims a given formula.
 */
@Component
public class FormulaTrimmer {

    /**
     * Removes the starting and ending $ signs from a formula.
     * @param formula LaTeX formula
     * @return trimmed formula
     */
    public String trim(String formula) {
        return formula.replaceAll("^.|.$", "");
    }
}
