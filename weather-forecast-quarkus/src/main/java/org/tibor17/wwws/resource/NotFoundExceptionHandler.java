package org.tibor17.wwws.resource;

import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import lombok.extern.slf4j.Slf4j;

import static jakarta.ws.rs.core.Response.Status.NOT_FOUND;
import static jakarta.ws.rs.core.Response.status;

@Provider
@Slf4j
public class NotFoundExceptionHandler implements ExceptionMapper<NotFoundException> {
    @Override
    public Response toResponse(NotFoundException e) {
        log.error(e.getMessage(), e);
        return status(NOT_FOUND)
                .entity(e.getMessage())
                .build();
    }
}
