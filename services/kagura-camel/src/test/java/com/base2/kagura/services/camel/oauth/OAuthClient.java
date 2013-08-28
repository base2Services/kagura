package com.base2.kagura.services.camel.oauth;

import org.apache.cxf.jaxrs.client.JAXRSClientFactoryBean;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.cxf.rs.security.oauth2.client.OAuthClientUtils;
import org.apache.cxf.rs.security.oauth2.common.AccessTokenGrant;
import org.apache.cxf.rs.security.oauth2.common.ClientAccessToken;
import org.apache.cxf.rs.security.oauth2.grants.clientcred.ClientCredentialsGrant;
import org.apache.cxf.rs.security.oauth2.grants.code.AuthorizationCodeGrant;
import org.apache.cxf.rs.security.oauth2.grants.code.ServerAuthorizationCodeGrant;
import org.apache.cxf.rs.security.oauth2.utils.AuthorizationUtils;
import org.apache.cxf.rs.security.oauth2.utils.OAuthUtils;
import org.junit.Ignore;
import org.junit.Test;

import javax.ws.rs.core.MediaType;


/**
 * @author aubels
 *         Date: 28/08/13
 */
public class OAuthClient {
    @Test
    @Ignore(value = "Not functioning")
    public void oauthClientTest()
    {
        String address = "http://localhost:8432/oauth/authorize";
        WebClient wc = WebClient.create(address);
        wc.type(MediaType.APPLICATION_FORM_URLENCODED).accept(MediaType.APPLICATION_JSON);

//        ServerAuthorizationCodeGrant serverAuthorizationCodeGrant = OAuthClientUtils.getAccessToken();
//        AuthorizationCodeGrant grant = new AuthorizationCodeGrant(serverAuthorizationCodeGrant.getCode());
        AuthorizationCodeGrant grant = new AuthorizationCodeGrant("");
//        AccessTokenGrant grant = OAuthClientUtils.getAccessToken(wc,serverAuthorizationCodeGrant);

        address = "http://localhost:8432/oauth/token";
        wc = WebClient.create(address);
        wc.type(MediaType.APPLICATION_FORM_URLENCODED).accept(MediaType.APPLICATION_JSON);
        ClientAccessToken at = OAuthClientUtils.getAccessToken(wc, new OAuthClientUtils.Consumer("alice", "alice"), grant, false);
    }
}
