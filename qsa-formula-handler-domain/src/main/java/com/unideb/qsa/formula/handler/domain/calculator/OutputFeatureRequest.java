package com.unideb.qsa.formula.handler.domain.calculator;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Cass for system output request.
 */
public final class OutputFeatureRequest {

    private Map<String, String> featureConditions;
    private List<String> outputFeatureIds;

    public List<String> getOutputFeatureIds() {
        return Optional.ofNullable(outputFeatureIds).orElse(Collections.emptyList());
    }

    public void setOutputFeatureIds(List<String> outputFeatureIds) {
        this.outputFeatureIds = outputFeatureIds;
    }

    public Map<String, String> getFeatureConditions() {
        return featureConditions;
    }

    public void setFeatureConditions(Map<String, String> featureConditions) {
        this.featureConditions = featureConditions;
    }

}
