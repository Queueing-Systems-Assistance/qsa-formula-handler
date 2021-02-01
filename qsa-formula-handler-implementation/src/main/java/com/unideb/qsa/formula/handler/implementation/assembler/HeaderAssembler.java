package com.unideb.qsa.formula.handler.implementation.assembler;

import org.slf4j.MDC;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;

/**
 * Assembler for client request headers.
 */
@Component
public class HeaderAssembler {

    private static final String REQUEST_ID = "X-Request-Id";
    private static final String MDC_REQUEST_ID = "requestId";

    /**
     * Creates HTTP headers which includes the current requestId and Accept-Language.
     * @return assembled header
     */
    public HttpHeaders assemble() {
        var httpHeaders = new LinkedMultiValueMap<String, String>();
        httpHeaders.add(REQUEST_ID, MDC.get(MDC_REQUEST_ID));
        httpHeaders.add(HttpHeaders.ACCEPT_LANGUAGE, LocaleContextHolder.getLocale().toString());
        return new HttpHeaders(httpHeaders);
    }

}
