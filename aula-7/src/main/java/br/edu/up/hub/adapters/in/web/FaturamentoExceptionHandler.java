package br.edu.up.hub.adapters.in.web;

import br.edu.up.hub.domain.model.EmissorIndisponivelException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

/** Adaptador de entrada: traduz falhas do núcleo para respostas HTTP. */
@RestControllerAdvice
public class FaturamentoExceptionHandler {

    @ExceptionHandler({IllegalArgumentException.class, IllegalStateException.class})
    public ResponseEntity<Map<String, String>> regraDeNegocio(RuntimeException e) {
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(Map.of("erro", e.getMessage()));
    }

    @ExceptionHandler(EmissorIndisponivelException.class)
    public ResponseEntity<Map<String, String>> emissorForaDoAr(EmissorIndisponivelException e) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(Map.of("erro", e.getMessage()));
    }
}

