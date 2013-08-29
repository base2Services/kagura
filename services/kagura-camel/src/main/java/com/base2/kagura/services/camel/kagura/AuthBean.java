package com.base2.kagura.services.camel.kagura;

import org.apache.camel.Body;
import org.apache.camel.Exchange;
import org.apache.camel.Header;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author aubels
 *         Date: 29/08/13
 */
public class AuthBean {

    private Map<String, AuthDetails> tokens = new HashMap<String, AuthDetails>();

    public void isLoggedIn(@Header("authToken") String authToken, Exchange exchange) throws AuthException {
        if (tokens.containsKey(authToken) && tokens.get(authToken).getLoggedIn()) return;
        throw new AuthException("User is not logged in.");
    }

    public void authenticate(@Header("user") String user, @Body String pass, Exchange exchange) throws AuthException {
        if (StringUtils.isBlank(user) || StringUtils.isBlank(pass))
            throw new AuthException("User was not logged in.");
        AuthDetails authDetails = new AuthDetails(user);
        tokens.put(authDetails.getToken().toString(), authDetails);
        Map<String, String> response = new HashMap<String, String>();
        response.put("error","");
        response.put("token",authDetails.getToken().toString());
        exchange.getOut().setBody(response);
    }

    public void buildAuthFail(Exchange exchange)
    {
        Map<String, String> response = new HashMap<String, String>();
        response.put("error","Authentication failure");
        response.put("token","");
        exchange.getOut().setBody(response);
    }

    public Map<String, AuthDetails> getTokens() {
        return tokens;
    }

    public static class AuthDetails {
        private Boolean loggedIn;
        private String username;
        private UUID token;

        private AuthDetails(String username) {
            this.username = username;
            this.loggedIn = true;
            this.token = UUID.randomUUID();
        }

        private Boolean getLoggedIn() {
            return loggedIn;
        }

        private void setLoggedIn(Boolean loggedIn) {
            this.loggedIn = loggedIn;
        }

        private String getUsername() {
            return username;
        }

        private void setUsername(String username) {
            this.username = username;
        }

        public UUID getToken() {
            return token;
        }
    }
}
