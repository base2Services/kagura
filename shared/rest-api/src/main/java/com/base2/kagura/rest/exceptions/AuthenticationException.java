/*
   Copyright 2014 base2Services

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */
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
