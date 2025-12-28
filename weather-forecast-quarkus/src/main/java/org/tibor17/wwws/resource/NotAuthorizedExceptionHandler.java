package org.tibor17.wwws.resource;

import jakarta.ws.rs.NotAuthorizedException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import lombok.extern.slf4j.Slf4j;

import static jakarta.ws.rs.core.Response.Status.UNAUTHORIZED;
import static jakarta.ws.rs.core.Response.status;

@Provider
@Slf4j
public class NotAuthorizedExceptionHandler implements ExceptionMapper<NotAuthorizedException> {
    @Override
    public Response toResponse(NotAuthorizedException e) {
        log.error(e.getMessage(), e);
        return status(UNAUTHORIZED)
                .entity("You are not authorized to access Weatherbit with 'AuthKey'?\n" + e.getMessage())
                .build();
    }
}
