package br.com.futmatch.infrastructure.adapters.in.web;

import br.com.futmatch.domain.exception.PartidaNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/probe")
public class ExceptionProbeController {

    @GetMapping("/partida-not-found")
    public void partidaNotFound() {
        throw new PartidaNotFoundException("test-id");
    }

    @PostMapping(value = "/echo", consumes = "application/json")
    public void echo(@RequestBody Map<String, Object> body) {
        // apenas para forçar parse JSON em testes
    }
}
