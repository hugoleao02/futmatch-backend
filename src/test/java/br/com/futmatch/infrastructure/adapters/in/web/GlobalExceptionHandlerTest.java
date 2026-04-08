package br.com.futmatch.infrastructure.adapters.in.web;

import br.com.futmatch.domain.exception.PartidaNotFoundException;
import br.com.futmatch.infrastructure.adapters.in.web.dto.ApiErrorResponse;
import br.com.futmatch.infrastructure.adapters.in.web.dto.ErrorCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class GlobalExceptionHandlerTest {

    @Mock
    private org.springframework.core.env.Environment environment;

    private GlobalExceptionHandler handler;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        when(environment.getActiveProfiles()).thenReturn(new String[0]);
        handler = new GlobalExceptionHandler(environment);
        mockMvc = MockMvcBuilders.standaloneSetup(new ExceptionProbeController())
                .setControllerAdvice(handler)
                .build();
    }

    @Test
    void partidaNotFound_returns404AndErrorCode() throws Exception {
        mockMvc.perform(get("/probe/partida-not-found"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error.code").value(ErrorCode.PARTIDA_NOT_FOUND.name()))
                .andExpect(jsonPath("$.error.message").value("test-id"))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void malformedJson_returns400() throws Exception {
        mockMvc.perform(post("/probe/echo")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ invalid json"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error.code").value(ErrorCode.CORPO_INVALIDO.name()));
    }

    @Test
    void methodArgumentNotValid_globalObjectError_doesNotThrow() throws Exception {
        MethodParameter parameter = methodParameterForBodyParam();
        BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(new Object(), "target");
        bindingResult.reject("objectError", "erro global sem field");

        MethodArgumentNotValidException ex = new MethodArgumentNotValidException(parameter, bindingResult);
        ResponseEntity<ApiErrorResponse> response = handler.handleValidation(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().error().code()).isEqualTo(ErrorCode.VALIDACAO_FALHOU.name());
        assertThat(response.getBody().error().details()).containsKey("global");
        @SuppressWarnings("unchecked")
        List<String> global = (List<String>) response.getBody().error().details().get("global");
        assertThat(global).contains("erro global sem field");
    }

    @Test
    void genericException_devProfile_includesDetail() {
        when(environment.getActiveProfiles()).thenReturn(new String[]{"dev"});
        handler = new GlobalExceptionHandler(environment);

        ResponseEntity<ApiErrorResponse> response =
                handler.handleGeneric(new RuntimeException("detalhe-servidor"));

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody().error().code()).isEqualTo(ErrorCode.ERRO_INTERNO.name());
        assertThat(response.getBody().error().details()).containsEntry("detail", "detalhe-servidor");
    }

    @Test
    void partidaNotFound_directHandler() {
        ResponseEntity<ApiErrorResponse> response =
                handler.handlePartidaNotFound(new PartidaNotFoundException("99"));

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody().error().code()).isEqualTo("PARTIDA_NOT_FOUND");
        assertThat(response.getBody().status()).isEqualTo(404);
    }

    private static MethodParameter methodParameterForBodyParam() throws Exception {
        Method method = MethodParamHolder.class.getDeclaredMethod("post", String.class);
        return new MethodParameter(method, 0);
    }

    @RestController
    @RequestMapping("/x")
    static class MethodParamHolder {
        @SuppressWarnings("unused")
        void post(@RequestBody String body) {
        }
    }
}
