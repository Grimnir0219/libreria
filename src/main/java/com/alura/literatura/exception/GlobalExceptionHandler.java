package com.alura.literatura.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public  Map<String, Object> manejarRuntimeException(RuntimeException ex){
        Map<String, Object> response = new HashMap<>();
        response.put("mensaje", ex.getMessage());
        response.put("estado", HttpStatus.BAD_REQUEST.value());
        response.put("error", "Solicitud incorrecta");
        return response;
    }



}
