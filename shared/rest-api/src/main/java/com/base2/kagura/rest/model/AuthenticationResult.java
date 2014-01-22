package com.base2.kagura.rest.model;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author aubels
 *         Date: 13/12/2013
 */
@XmlRootElement
public class AuthenticationResult extends ResponseBase {
    String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
