package org.tibor17.wwws.resource;

import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import lombok.extern.slf4j.Slf4j;

import static jakarta.ws.rs.core.Response.Status.BAD_REQUEST;
import static jakarta.ws.rs.core.Response.status;

@Provider
@Slf4j
public class UnrecognizedPropertyExceptionHandler implements ExceptionMapper<UnrecognizedPropertyException> {
    @Override
    public Response toResponse(UnrecognizedPropertyException e) {
        log.error(e.getMessage(), e);
        return status(BAD_REQUEST)
                .entity("Unknown property: " + e.getPropertyName())
                .build();
    }
}
