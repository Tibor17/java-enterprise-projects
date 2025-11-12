package org.tibor17.wwws.controller;

import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException.NotFound;
import org.springframework.web.client.HttpClientErrorException.Forbidden;
import org.springframework.web.client.HttpClientErrorException.Unauthorized;
import org.springframework.web.client.HttpClientErrorException.BadRequest;
import org.springframework.web.client.HttpClientErrorException.MethodNotAllowed;

import java.io.IOException;
import java.net.URISyntaxException;

import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
@Slf4j
public class RSExceptionHandler {

    @ExceptionHandler(UnrecognizedPropertyException.class)
    public ResponseEntity<String> handleUnrecognizedPropertyException(UnrecognizedPropertyException e) {
        return new ResponseEntity<>("Unknown property: " + e.getPropertyName(), BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public void handleConstraintViolationException(ConstraintViolationException e, HttpServletResponse response)
            throws IOException {
        log.error(e.getMessage(), e);
        response.sendError(BAD_REQUEST.value(), e.getMessage());
    }

    @ExceptionHandler(URISyntaxException.class)
    public void handleURISyntaxException(URISyntaxException e, HttpServletResponse response)
            throws IOException {
        log.error(e.getMessage(), e);
        response.sendError(INTERNAL_SERVER_ERROR.value(), e.getMessage());
    }

    @ExceptionHandler(NotFound.class)
    public void handleRestClientResourceNotFound(NotFound e, HttpServletResponse response)
            throws IOException {
        log.error(e.getMessage(), e);
        response.sendError(NOT_FOUND.value(), e.getMessage());
    }

    @ExceptionHandler(Forbidden.class)
    public void handleRestClientForbidden(Forbidden e, HttpServletResponse response)
            throws IOException {
        log.error(e.getMessage(), e);
        response.sendError(FORBIDDEN.value(), "Wrong authorization Weatherbit 'key'?\n" + e.getMessage());
    }

    @ExceptionHandler(Unauthorized.class)
    public void handleRestClientUnauthorized(Unauthorized e, HttpServletResponse response)
            throws IOException {
        log.error(e.getMessage(), e);
        response.sendError(UNAUTHORIZED.value(), "Wrong authorization Weatherbit 'key'?\n" + e.getMessage());
    }

    @ExceptionHandler(BadRequest.class)
    public void handleRestClientBadRequest(BadRequest e, HttpServletResponse response)
            throws IOException {
        log.error(e.getMessage(), e);
        response.sendError(BAD_REQUEST.value(), e.getMessage());
    }

    @ExceptionHandler(MethodNotAllowed.class)
    public void handleRestClientMethodNotAllowed(MethodNotAllowed e, HttpServletResponse response)
            throws IOException {
        log.error(e.getMessage(), e);
        response.sendError(METHOD_NOT_ALLOWED.value(), e.getMessage());
    }
}
