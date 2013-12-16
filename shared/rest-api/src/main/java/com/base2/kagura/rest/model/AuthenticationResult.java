package com.base2.kagura.rest.model;

import java.util.List;

/**
 * @author aubels
 *         Date: 13/12/2013
 */
public class AuthenticationResult extends ResponseBase {
    String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
