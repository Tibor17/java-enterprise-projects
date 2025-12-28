package org.tibor17.wwws.resource;

import jakarta.ws.rs.ClientErrorException;

import static jakarta.ws.rs.core.Response.Status.TOO_MANY_REQUESTS;

public class TooManyRequestsException extends ClientErrorException {
    public TooManyRequestsException(String message) {
        super(message, TOO_MANY_REQUESTS);
    }
}
