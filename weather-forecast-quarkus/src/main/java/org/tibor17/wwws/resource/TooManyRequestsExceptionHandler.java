package org.tibor17.wwws.resource;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import lombok.extern.slf4j.Slf4j;

import static jakarta.ws.rs.core.Response.Status.TOO_MANY_REQUESTS;
import static jakarta.ws.rs.core.Response.status;

@Provider
@Slf4j
public class TooManyRequestsExceptionHandler implements ExceptionMapper<TooManyRequestsException> {
    @Override
    public Response toResponse(TooManyRequestsException e) {
        log.error(e.getMessage(), e);
        return status(TOO_MANY_REQUESTS)
                .entity(e.getMessage())
                .build();
    }
}
