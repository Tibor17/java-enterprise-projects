package org.tibor17.wwws.resource;

import jakarta.persistence.PersistenceException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import lombok.extern.slf4j.Slf4j;

import static jakarta.ws.rs.core.Response.Status.INTERNAL_SERVER_ERROR;
import static jakarta.ws.rs.core.Response.status;

@Provider
@Slf4j
public class PersistenceExceptionHandler implements ExceptionMapper<PersistenceException> {
    @Override
    public Response toResponse(PersistenceException e) {
        log.error(e.getMessage(), e);
        return status(INTERNAL_SERVER_ERROR)
                .entity(e.getMessage())
                .build();
    }
}
