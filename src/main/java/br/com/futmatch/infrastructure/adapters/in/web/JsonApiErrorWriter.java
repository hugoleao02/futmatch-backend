package br.com.futmatch.infrastructure.adapters.in.web;

import br.com.futmatch.infrastructure.adapters.in.web.dto.ApiErrorResponse;
import br.com.futmatch.infrastructure.adapters.in.web.dto.ApiErrorResponseFactory;
import br.com.futmatch.infrastructure.adapters.in.web.dto.ErrorCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Component
public class JsonApiErrorWriter {

    private final ObjectMapper objectMapper;

    public JsonApiErrorWriter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public void write(HttpServletResponse response, HttpStatus status, ErrorCode code, String message)
            throws IOException {
        write(response, status, code, message, null);
    }

    public void write(HttpServletResponse response, HttpStatus status, ErrorCode code, String message,
                      Map<String, Object> details) throws IOException {
        ApiErrorResponse body = ApiErrorResponseFactory.create(code, status, message, details);
        response.setStatus(status.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        objectMapper.writeValue(response.getOutputStream(), body);
    }
}
