package org.tibor17.wwws.resource;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import lombok.extern.slf4j.Slf4j;

import java.net.URISyntaxException;

import static jakarta.ws.rs.core.Response.Status.INTERNAL_SERVER_ERROR;
import static jakarta.ws.rs.core.Response.status;

@Provider
@Slf4j
public class URISyntaxExceptionHandler implements ExceptionMapper<URISyntaxException> {
    @Override
    public Response toResponse(URISyntaxException e) {
        log.error(e.getMessage(), e);
        return status(INTERNAL_SERVER_ERROR)
                .entity(e.getMessage())
                .build();
    }
}
