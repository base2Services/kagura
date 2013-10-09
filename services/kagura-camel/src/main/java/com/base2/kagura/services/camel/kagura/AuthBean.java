package com.base2.kagura.services.camel.kagura;

import com.base2.kagura.core.reporting.view.authentication.AuthenticationProvider;
import com.base2.kagura.core.reporting.view.authentication.model.Group;
import com.base2.kagura.core.reporting.view.authentication.model.User;
import org.apache.camel.Body;
import org.apache.camel.Exchange;
import org.apache.camel.Header;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author aubels
 *         Date: 29/08/13
 */
@Service
public class AuthBean {
    private static final Logger LOG = LoggerFactory.getLogger(AuthBean.class);
    private Map<String, AuthDetails> tokens = new HashMap<String, AuthDetails>();
    @Autowired()
    private AuthenticationProvider authenticationProvider;
    @Autowired
    private ServerBean serverBean;
    @Autowired
    private ReportBean reportsBean;

    public void isLoggedIn(@Header("authToken") String authToken, Exchange exchange) throws AuthenticationException {
        if (tokens.containsKey(authToken))
        {
            AuthDetails authDetails = tokens.get(authToken);
            if( authDetails.getLoggedIn() && !authDetails.expired())
            {
                authDetails.updateLastAccessed();
                exchange.getIn().setHeader("authDetails",authDetails);
                return;
            }
            LOG.info("Ticket was expired or logged out.");
        }
        throw new AuthenticationException("User is not logged in.");
    }

    public void authenticate(@Header("user") String user, @Body String pass, Exchange exchange) throws AuthenticationException {
        if (StringUtils.isBlank(user) || StringUtils.isBlank(pass))
        {
            LOG.info("User '{}' attempted to login with a blank username or password.", user);
            throw new AuthenticationException("User was not logged in.");
        }
        try {
            authenticationProvider.authenticateUser(user, pass);
        } catch (Exception e) {
            throw new AuthenticationException(e);
        }
        AuthDetails authDetails = new AuthDetails(user);
        tokens.put(authDetails.getToken().toString(), authDetails);
        Map<String, String> response = new HashMap<String, String>();
        response.put("error","");
        response.put("token",authDetails.getToken().toString());
        exchange.getOut().setHeader("authDetails",authDetails);
        exchange.getOut().setBody(response);
    }

    public void buildAuthFail(Exchange exchange)
    {
        Map<String, String> response = new HashMap<String, String>();
        response.put("error","Authentication failure");
        response.put("token","");
        exchange.getOut().setBody(response);
    }

    public void cleanAuthTickets(Exchange exchange)
    {
        List<String> removeThese = new ArrayList<String>();
        for (AuthDetails authDetails : tokens.values())
        {
            if (authDetails.expired())
                removeThese.add(authDetails.getToken().toString());
        }
        for (String each: removeThese)
        {
            LOG.info("Removing token: " + each);
            tokens.remove(each);
        }
    }

    public void logout(@Header("authToken") String authToken, Exchange exchange) throws AuthenticationException {
        if (tokens.containsKey(authToken) && tokens.get(authToken).getLoggedIn())
        {
            tokens.remove(authToken);
            exchange.getIn().removeHeader("authDetails");
            return;
        }
        throw new AuthenticationException("User is not logged in.");
    }

    public void canAccessReport(
            @Header("authToken") String authToken
            , @Header("reportId") String reportId
            , Exchange exchange) throws AuthorizationException, AuthenticationException {
        if (tokens.containsKey(authToken) && tokens.get(authToken).getLoggedIn())
        {
            User user = authenticationProvider.getUser(tokens.get(authToken).getUsername());
            Collection<String> reports = authenticationProvider.getUserReports(user);
            exchange.getIn().setHeader("groups", user.getGroups());
            exchange.getIn().setHeader("userExtra", user.getExtraOptions());
            if (reports.contains(reportId))
                return;
            throw new AuthorizationException("User is not logged in.");
        }
        throw new AuthenticationException("User is not logged in.");
    }

    public Collection<String> getReports(@Header("authToken") String authToken, Exchange exchange) throws AuthenticationException {
        if (!tokens.containsKey(authToken) || !tokens.get(authToken).getLoggedIn())
            throw new AuthenticationException("User is not logged in.");
        AuthDetails authDetails = tokens.get(authToken);
        String username = authDetails.getUsername();
        Set<String> result = authenticationProvider.getUserReports(username);
        return result;
    }

    public Map<String, Object> getReportsDetailed(@Header("authToken") String authToken, Exchange exchange) throws AuthenticationException {
        if (!tokens.containsKey(authToken) || !tokens.get(authToken).getLoggedIn())
            throw new AuthenticationException("User is not logged in.");
        AuthDetails authDetails = tokens.get(authToken);
        String username = authDetails.getUsername();
        Set<String> reports = authenticationProvider.getUserReports(username);
        Map<String, Object> result = new LinkedHashMap<String, Object>();
        for (String reportName : reports)
        {
            result.put(reportName, reportsBean.getReportDetails(reportName, false));
        }
        return result;
    }

    // Used in tests
    public List<Group> getGroups()
    {
        return authenticationProvider.getGroups();
    }

    public List<User> getUsers()
    {
        return authenticationProvider.getUsers();
    }

    public AuthenticationProvider getAuthenticationProvider() {
        return authenticationProvider;
    }

    public void setAuthenticationProvider(AuthenticationProvider authenticationProvider) {
        this.authenticationProvider = authenticationProvider;
    }


    // End used in tests

    public static class AuthDetails {
        private Boolean loggedIn;
        private String username;
        private UUID token;
        private Date lastAccessed;

        private AuthDetails(String username) {
            this.username = username;
            this.loggedIn = true;
            this.token = UUID.randomUUID();
            this.lastAccessed = Calendar.getInstance().getTime();
        }

        public Boolean expired()
        {
            Date curDate = Calendar.getInstance().getTime();
            return curDate.getTime() - this.lastAccessed.getTime() > 2 * 24 * 60 * 60 * 1000;
        }

        public void updateLastAccessed()
        {
            this.lastAccessed = Calendar.getInstance().getTime();
        }

        public Boolean getLoggedIn() {
            return loggedIn;
        }

        private void setLoggedIn(Boolean loggedIn) {
            this.loggedIn = loggedIn;
        }

        public String getUsername() {
            return username;
        }

        private void setUsername(String username) {
            this.username = username;
        }

        public UUID getToken() {
            return token;
        }

        public Date getLastAccessed() {
            return lastAccessed;
        }

        public void setLastAccessed(Date lastAccessed) {
            this.lastAccessed = lastAccessed;
        }
    }

    public Map<String, AuthDetails> getTokens() {
        return tokens;
    }

    public ServerBean getServerBean() {
        return serverBean;
    }

    public void setServerBean(ServerBean serverBean) {
        this.serverBean = serverBean;
    }

    public void setReportsBean(ReportBean reportsBean) {
        this.reportsBean = reportsBean;
    }

}
