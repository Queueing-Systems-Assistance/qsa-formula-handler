package com.unideb.qsa.formula.handler.implementation.client;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;

import com.unideb.qsa.formula.handler.domain.calculator.OutputFeature;
import com.unideb.qsa.formula.handler.domain.calculator.OutputFeatureRequest;
import com.unideb.qsa.formula.handler.domain.exception.QSAServerException;
import com.unideb.qsa.formula.handler.implementation.assembler.HeaderAssembler;

/**
 * Client to call calculator.
 */
@Component
public class CalculatorClient {

    private static final String ERROR_RESPONSE_CALCULATOR = "Error response from calculator, status code [%s]";

    @Autowired
    private WebClient webClient;
    @Autowired
    private HeaderAssembler headerAssembler;
    @Value("${qsa.calculator.uri}")
    private String calculatorUri;

    /**
     * Get system output features by calling the calculator.
     * @param systemId          system id
     * @param featureConditions input features
     */
    public Map<String, String> getFeatureOutputs(String systemId, Map<String, String> featureConditions) {
        var currentLocale = LocaleContextHolder.getLocale();
        var requestBody = new OutputFeatureRequest();
        requestBody.setFeatureConditions(featureConditions);
        return webClient.post()
                        .uri(String.format(calculatorUri, systemId))
                        .headers(httpHeaders -> httpHeaders.addAll(headerAssembler.assemble()))
                        .bodyValue(requestBody)
                        .retrieve()
                        .onStatus(HttpStatus::isError, clientResponse -> {
                            LocaleContextHolder.setLocale(currentLocale);
                            return createError(clientResponse);
                        })
                        .bodyToMono(new ParameterizedTypeReference<List<OutputFeature>>() {})
                        .blockOptional()
                        .stream()
                        .flatMap(Collection::stream)
                        .collect(Collectors.toMap(OutputFeature::getId, outputFeature -> outputFeature.getValues().get(0)));
    }

    private Mono<Throwable> createError(ClientResponse clientResponse) {
        return Mono.error(new QSAServerException(String.format(ERROR_RESPONSE_CALCULATOR, clientResponse.statusCode())));
    }

}
