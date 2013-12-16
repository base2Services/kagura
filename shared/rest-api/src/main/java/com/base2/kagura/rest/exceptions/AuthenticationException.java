package com.base2.kagura.rest.exceptions;

import com.base2.kagura.rest.model.ResponseBase;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

/**
 * @author aubels
 *         Date: 29/08/13
 */
public class AuthenticationException extends WebApplicationException {
    public AuthenticationException(final String s) {
        super(Response.status(Response.Status.OK).entity(new ResponseBase() {{ setError(s); }}).build());
    }

    public AuthenticationException() {
        super(Response.status(Response.Status.OK).entity(new ResponseBase() {{
            setError("Authentication failure");
        }}).build());
    }

    /**
     * Construct a new instance with a blank message and default HTTP status code of 500.
     *
     * @param cause the underlying cause of the exception.
     */
    public AuthenticationException(Throwable cause) {
        super(cause);
    }
}
