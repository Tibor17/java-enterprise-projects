package org.tibor17.wwws.resource;

import jakarta.ws.rs.ForbiddenException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import lombok.extern.slf4j.Slf4j;

import static jakarta.ws.rs.core.Response.Status.FORBIDDEN;
import static jakarta.ws.rs.core.Response.status;

@Provider
@Slf4j
public class ForbiddenExceptionHandler implements ExceptionMapper<ForbiddenException> {
    @Override
    public Response toResponse(ForbiddenException e) {
        log.error(e.getMessage(), e);
        return status(FORBIDDEN)
                .entity("Wrong authorization Weatherbit 'AuthKey'?\n" + e.getMessage())
                .build();
    }
}
