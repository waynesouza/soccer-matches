package com.waynesouza.soccer.config.interceptor;

import com.waynesouza.soccer.config.exception.ParametrizedMessageException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Locale;

@RestControllerAdvice
@RequiredArgsConstructor
public class ResponseExceptionHandler extends ResponseEntityExceptionHandler {

    private final MessageSource messageSource;

    @ExceptionHandler({ParametrizedMessageException.class})
    protected ResponseEntity<Object> handleBadRequestException(ParametrizedMessageException exception) {
        String messageName = exception.getMessageName();
        String message = messageSource.getMessage(messageName, null, "Código de erro não encontrado", Locale.getDefault());
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("x-soccerapi-error", message);
        return new ResponseEntity<>(httpHeaders, HttpStatus.BAD_REQUEST);
    }
}
