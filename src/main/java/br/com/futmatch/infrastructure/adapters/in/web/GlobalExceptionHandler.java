package br.com.futmatch.infrastructure.adapters.in.web;

import br.com.futmatch.domain.exception.EmailAlreadyExistsException;
import br.com.futmatch.domain.exception.InvalidCredentialsException;
import br.com.futmatch.domain.exception.InvalidEmailFormatException;
import br.com.futmatch.domain.exception.PartidaNotFoundException;
import br.com.futmatch.domain.exception.SenhaFracaException;
import br.com.futmatch.domain.exception.UsuarioNotFoundException;
import br.com.futmatch.infrastructure.adapters.in.web.dto.ApiErrorResponse;
import br.com.futmatch.infrastructure.adapters.in.web.dto.ApiErrorResponseFactory;
import br.com.futmatch.infrastructure.adapters.in.web.dto.ErrorCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    private static final String MSG_ERRO_INTERNO = "Ocorreu um erro interno. Tente novamente mais tarde.";
    private static final String MSG_VIOLACAO_INTEGRIDADE = "Não foi possível concluir a operação por conflito de dados.";

    private final Environment environment;

    public GlobalExceptionHandler(Environment environment) {
        this.environment = environment;
    }

    @ExceptionHandler(PartidaNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handlePartidaNotFound(PartidaNotFoundException ex) {
        return body(ErrorCode.PARTIDA_NOT_FOUND, HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(UsuarioNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleUsuarioNotFound(UsuarioNotFoundException ex) {
        return body(ErrorCode.USUARIO_NOT_FOUND, HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ApiErrorResponse> handleEmailAlreadyExists(EmailAlreadyExistsException ex) {
        return body(ErrorCode.EMAIL_JA_CADASTRADO, HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ApiErrorResponse> handleInvalidCredentials(InvalidCredentialsException ex) {
        return body(ErrorCode.CREDENCIAIS_INVALIDAS, HttpStatus.UNAUTHORIZED, ex.getMessage());
    }

    @ExceptionHandler(InvalidEmailFormatException.class)
    public ResponseEntity<ApiErrorResponse> handleInvalidEmailFormat(InvalidEmailFormatException ex) {
        return body(ErrorCode.EMAIL_INVALIDO, HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(SenhaFracaException.class)
    public ResponseEntity<ApiErrorResponse> handleSenhaFraca(SenhaFracaException ex) {
        return body(ErrorCode.SENHA_FRACA, HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiErrorResponse> handleIllegalArgument(IllegalArgumentException ex) {
        return body(ErrorCode.REQUISICAO_INVALIDA, HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ApiErrorResponse> handleIllegalState(IllegalStateException ex) {
        return body(ErrorCode.ESTADO_INVALIDO, HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, Object> fieldMap = new LinkedHashMap<>();
        List<String> globalMessages = new ArrayList<>();

        ex.getBindingResult().getAllErrors().forEach(error -> {
            if (error instanceof FieldError fieldError) {
                fieldMap.put(fieldError.getField(), fieldError.getDefaultMessage());
            } else {
                globalMessages.add(error.getDefaultMessage());
            }
        });

        Map<String, Object> details = new LinkedHashMap<>();
        if (!fieldMap.isEmpty()) {
            details.put("fields", fieldMap);
        }
        if (!globalMessages.isEmpty()) {
            details.put("global", globalMessages);
        }

        String message = details.isEmpty() ? "Erro de validação" : "Erro de validação nos dados enviados";
        ApiErrorResponse body = ApiErrorResponseFactory.create(
                ErrorCode.VALIDACAO_FALHOU,
                HttpStatus.BAD_REQUEST,
                message,
                details.isEmpty() ? null : details
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiErrorResponse> handleUnreadable(HttpMessageNotReadableException ex) {
        log.debug("Corpo da requisição inválido: {}", ex.getMessage());
        return body(ErrorCode.CORPO_INVALIDO, HttpStatus.BAD_REQUEST, "Corpo da requisição inválido ou JSON malformado");
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiErrorResponse> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        String msg = "Parâmetro inválido: " + ex.getName();
        Map<String, Object> details = Map.of(
                "parameter", ex.getName(),
                "value", Objects.toString(ex.getValue(), "null")
        );
        ApiErrorResponse body = ApiErrorResponseFactory.create(
                ErrorCode.PARAMETRO_INVALIDO,
                HttpStatus.BAD_REQUEST,
                msg,
                details
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ApiErrorResponse> handleMissingParam(MissingServletRequestParameterException ex) {
        String msg = "Parâmetro obrigatório ausente: " + ex.getParameterName();
        return body(ErrorCode.PARAMETRO_OBRIGATORIO, HttpStatus.BAD_REQUEST, msg,
                Map.of("parameter", ex.getParameterName(), "type", ex.getParameterType()));
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiErrorResponse> handleMethodNotSupported(HttpRequestMethodNotSupportedException ex) {
        var supportedMethods = ex.getSupportedHttpMethods();
        String supported = supportedMethods == null
                ? ""
                : String.join(", ", supportedMethods.stream().map(Object::toString).toList());
        String msg = "Método HTTP não permitido para este recurso";
        Map<String, Object> details = new LinkedHashMap<>();
        details.put("method", ex.getMethod());
        if (!supported.isEmpty()) {
            details.put("supportedMethods", supported);
        }
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(
                ApiErrorResponseFactory.create(ErrorCode.METODO_NAO_PERMITIDO, HttpStatus.METHOD_NOT_ALLOWED, msg, details)
        );
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleNoResource(NoResourceFoundException ex) {
        return body(ErrorCode.RECURSO_NAO_ENCONTRADO, HttpStatus.NOT_FOUND,
                "Recurso não encontrado: " + ex.getResourcePath());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiErrorResponse> handleDataIntegrity(DataIntegrityViolationException ex) {
        log.warn("Violação de integridade: {}", ex.getMostSpecificCause().getMessage());
        return body(ErrorCode.VIOLACAO_INTEGRIDADE, HttpStatus.CONFLICT, MSG_VIOLACAO_INTEGRIDADE);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleGeneric(Exception ex) {
        log.error("Erro não tratado", ex);
        if (isDevProfileActive()) {
            return body(ErrorCode.ERRO_INTERNO, HttpStatus.INTERNAL_SERVER_ERROR,
                    MSG_ERRO_INTERNO,
                    Map.of("detail", ex.getMessage() != null ? ex.getMessage() : ex.getClass().getSimpleName()));
        }
        return body(ErrorCode.ERRO_INTERNO, HttpStatus.INTERNAL_SERVER_ERROR, MSG_ERRO_INTERNO);
    }

    private boolean isDevProfileActive() {
        return Arrays.asList(environment.getActiveProfiles()).contains("dev");
    }

    private ResponseEntity<ApiErrorResponse> body(ErrorCode code, @NonNull HttpStatus status, String message) {
        return ResponseEntity.status(status).body(ApiErrorResponseFactory.create(code, status, message));
    }

    private ResponseEntity<ApiErrorResponse> body(ErrorCode code, @NonNull HttpStatus status, String message,
                                                  Map<String, Object> details) {
        return ResponseEntity.status(status).body(ApiErrorResponseFactory.create(code, status, message, details));
    }
}
