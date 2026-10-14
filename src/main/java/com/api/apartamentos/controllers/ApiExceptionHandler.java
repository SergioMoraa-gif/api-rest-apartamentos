package com.api.apartamentos.controllers;

import java.time.LocalDateTime;
import java.util.Map;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<Map<String, Object>> validation(MethodArgumentNotValidException exception) {
        return response(HttpStatus.BAD_REQUEST, exception.getBindingResult().getFieldError() == null ? "Solicitud inválida" : exception.getBindingResult().getFieldError().getDefaultMessage());
    }
    @ExceptionHandler(IllegalArgumentException.class)
    ResponseEntity<Map<String, Object>> badRequest(IllegalArgumentException exception) { return response(HttpStatus.BAD_REQUEST, exception.getMessage()); }

    // cuando el JSON viene mal escrito o un valor no corresponde (ej. un enum que no existe)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    ResponseEntity<Map<String, Object>> jsonInvalido(HttpMessageNotReadableException exception) {
        return response(HttpStatus.BAD_REQUEST, "El cuerpo JSON no es valido");
    }

    // cuando se rompe una regla de la BD: email repetido o borrar un registro que otros usan
    @ExceptionHandler(DataIntegrityViolationException.class)
    ResponseEntity<Map<String, Object>> conflicto(DataIntegrityViolationException exception) {
        return response(HttpStatus.CONFLICT, "El registro esta duplicado o tiene datos relacionados");
    }
    private ResponseEntity<Map<String, Object>> response(HttpStatus status, String message) {
        return ResponseEntity.status(status).body(Map.of("timestamp", LocalDateTime.now(), "status", status.value(), "error", message));
    }
}
