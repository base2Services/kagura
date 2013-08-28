package com.base2.kagura.services.camel.oauth;

import org.apache.cxf.rs.security.oauth2.common.*;
import org.apache.cxf.rs.security.oauth2.grants.code.AuthorizationCodeDataProvider;
import org.apache.cxf.rs.security.oauth2.grants.code.AuthorizationCodeRegistration;
import org.apache.cxf.rs.security.oauth2.grants.code.ServerAuthorizationCodeGrant;
import org.apache.cxf.rs.security.oauth2.provider.OAuthDataProvider;
import org.apache.cxf.rs.security.oauth2.provider.OAuthServiceException;
import org.apache.cxf.rs.security.oauth2.tokens.mac.MacAccessToken;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/** Just a simple OAuth provider to get us started. -- Statically defined. Do not use.
 * @author aubels
 *         Date: 28/08/13
 */
public class StaticOAuthProvider implements AuthorizationCodeDataProvider {
    public static final String CALLBACK = "http://www.example.com/callback";
    public static final String APPLICATION_NAME = "Test Oauth 1.0 application";
    public static final String CLIENT_ID = "12345678";
    public static final String CLIENT_SECRET = "secret";

    protected ConcurrentHashMap<String, Client> clientAuthInfo = new ConcurrentHashMap<String, Client>();
    protected ConcurrentHashMap<String, ServerAccessToken> oauthTokens = new ConcurrentHashMap<String, ServerAccessToken>();
    protected HashMap<Client, List<OAuthPermission>> permissions = new HashMap<Client, List<OAuthPermission>>();

    /**
     * Returns the previously registered third-party {@link org.apache.cxf.rs.security.oauth2.common.Client}
     *
     * @param clientId the client id
     * @return Client
     * @throws org.apache.cxf.rs.security.oauth2.provider.OAuthServiceException
     *
     */
    @Override
    public Client getClient(String clientId) throws OAuthServiceException {
        if (!clientAuthInfo.containsKey(clientId))
        {
            Client client = new Client(clientId, clientId, true);
            clientAuthInfo.put(clientId, client);
        }
        return clientAuthInfo.get(clientId);
    }

    /**
     * Create access token
     *
     * @param accessToken the token registration info
     * @return AccessToken
     * @throws org.apache.cxf.rs.security.oauth2.provider.OAuthServiceException
     *
     */
    @Override
    public ServerAccessToken createAccessToken(AccessTokenRegistration accessToken) throws OAuthServiceException {
        MacAccessToken reqToken = new MacAccessToken(accessToken.getClient(), 10 * 1000 * 1000);
        oauthTokens.put(reqToken.getTokenKey(),reqToken);
        return reqToken;
    }

    /**
     * Get access token
     *
     * @param accessToken the token key
     * @return AccessToken
     * @throws org.apache.cxf.rs.security.oauth2.provider.OAuthServiceException
     *
     */
    @Override
    public ServerAccessToken getAccessToken(String accessToken) throws OAuthServiceException {
        return oauthTokens.get(accessToken);
    }

    /**
     * Get preauthorized access token
     *
     * @param client          Client
     * @param requestedScopes the scopes requested by the client
     * @param subject         End User subject
     * @return AccessToken access token
     * @throws org.apache.cxf.rs.security.oauth2.provider.OAuthServiceException
     *
     */
    @Override
    public ServerAccessToken getPreauthorizedToken(Client client, List<String> requestedScopes, UserSubject subject, String grantType) throws OAuthServiceException {
        return null;
    }

    /**
     * Refresh access token
     *
     * @param client          the client
     * @param refreshToken    refresh token key
     * @param requestedScopes the scopes requested by the client
     * @return AccessToken
     * @throws org.apache.cxf.rs.security.oauth2.provider.OAuthServiceException
     */
    @Override
    public ServerAccessToken refreshAccessToken(Client client, String refreshToken, List<String> requestedScopes) throws OAuthServiceException {
        ServerAccessToken serverAccessToken = oauthTokens.get(refreshToken);
        return serverAccessToken;
    }

    /**
     * Removes the token
     *
     * @param accessToken the token
     * @throws org.apache.cxf.rs.security.oauth2.provider.OAuthServiceException
     *
     */
    @Override
    public void removeAccessToken(ServerAccessToken accessToken) throws OAuthServiceException {
        oauthTokens.remove(accessToken.getTokenKey());
    }

    /**
     * Converts the requested scope to the list of permissions
     *
     * @param requestedScope
     * @return list of permissions
     */
    @Override
    public List<OAuthPermission> convertScopeToPermissions(Client client, List<String> requestedScope) {
        return permissions.get(client);
    }

    /**
     * Creates a temporarily code grant which will capture the
     * information about the {@link org.apache.cxf.rs.security.oauth2.common.Client} requesting the access to
     * the resource owner's resources
     *
     * @param reg information about the client code grant request
     * @return new code grant
     * @throws org.apache.cxf.rs.security.oauth2.provider.OAuthServiceException
     *
     * @see org.apache.cxf.rs.security.oauth2.grants.code.AuthorizationCodeRegistration
     * @see org.apache.cxf.rs.security.oauth2.grants.code.ServerAuthorizationCodeGrant
     */
    @Override
    public ServerAuthorizationCodeGrant createCodeGrant(AuthorizationCodeRegistration reg) throws OAuthServiceException {
        ServerAuthorizationCodeGrant serverAuthorizationCodeGrant = new ServerAuthorizationCodeGrant(reg.getClient(), 10*1000*1000);
        UserSubject sef = new UserSubject();
        sef.setLogin("baasdf");
        sef.setRoles(Arrays.asList("role1"));
        serverAuthorizationCodeGrant.setSubject(sef);
        return serverAuthorizationCodeGrant;
    }

    /**
     * Returns the previously registered {@link org.apache.cxf.rs.security.oauth2.grants.code.ServerAuthorizationCodeGrant}
     *
     * @param code the code grant
     * @return the grant
     * @throws org.apache.cxf.rs.security.oauth2.provider.OAuthServiceException
     *          if no grant with this code is available
     * @see org.apache.cxf.rs.security.oauth2.grants.code.ServerAuthorizationCodeGrant
     */
    @Override
    public ServerAuthorizationCodeGrant removeCodeGrant(String code) throws OAuthServiceException {
        return null;
    }
}
