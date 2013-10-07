package com.base2.kagura.services.camel.kagura;

/**
 * @author aubels
 *         Date: 29/08/13
 */
public class AuthenticationException extends Exception {
    public AuthenticationException(String s) {
        super(s);
    }

    public AuthenticationException(Throwable cause) {
        super(cause);
    }
}
