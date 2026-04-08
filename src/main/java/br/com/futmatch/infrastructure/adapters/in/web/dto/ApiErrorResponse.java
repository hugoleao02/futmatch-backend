package br.com.futmatch.infrastructure.adapters.in.web.dto;

public record ApiErrorResponse(
        String timestamp,
        int status,
        ApiErrorBody error
) {
}
