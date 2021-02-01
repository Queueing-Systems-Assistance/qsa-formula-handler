package com.unideb.qsa.formula.handler.implementation.mapper;

import org.springframework.stereotype.Component;

/**
 * Maps a formula value to be readable.
 */
@Component
public class OutputValueStringMapper {

    /**
     * Maps a given formula value to be readable by removing the trailing zeros.
     * Eg.: given a formula value 12.42000, then the mapping will remove the trailing zeros: 12.42
     * @param value formula value
     * @return mapped formula value
     */
    public String map(String value) {
        String result = value;
        if (value.contains(".")) {
            result = value.replaceAll("0*$", "").replaceAll("\\.$", "");
        }
        return result;
    }
}
