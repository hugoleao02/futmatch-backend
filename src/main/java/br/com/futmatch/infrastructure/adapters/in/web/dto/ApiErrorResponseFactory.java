package br.com.futmatch.infrastructure.adapters.in.web.dto;

import org.springframework.http.HttpStatus;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public final class ApiErrorResponseFactory {

    private static final DateTimeFormatter ISO = DateTimeFormatter.ISO_OFFSET_DATE_TIME;

    private ApiErrorResponseFactory() {
    }

    public static ApiErrorResponse create(ErrorCode code, HttpStatus status, String message) {
        return create(code, status, message, null);
    }

    public static ApiErrorResponse create(ErrorCode code, HttpStatus status, String message,
                                          Map<String, Object> details) {
        String codeValue = code.name();
        return new ApiErrorResponse(
                OffsetDateTime.now().format(ISO),
                status.value(),
                new ApiErrorBody(codeValue, message, details)
        );
    }
}
