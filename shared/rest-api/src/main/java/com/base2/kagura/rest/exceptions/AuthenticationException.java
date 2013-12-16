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
        super(Response.status(Response.Status.OK).entity(new ResponseBase() {{ setError("Authentication failure"); }} ).build());
    }
}
