package com.unideb.qsa.formula.handler.domain.calculator;

import java.util.List;

/**
 * Represents a system output feature.
 */
public final class OutputFeature {

    private String id;
    private String name;
    private String description;
    private List<String> values;

    public OutputFeature() {
    }

    public String getDescription() {
        return description;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<String> getValues() {
        return values;
    }

}
