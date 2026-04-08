package br.com.futmatch.infrastructure.adapters.in.web.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiErrorBody(
        String code,
        String message,
        Map<String, Object> details
) {
}
