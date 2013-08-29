package com.base2.kagura.services.camel.kagura;

import com.base2.kagura.services.camel.model.Group;
import com.base2.kagura.services.camel.model.User;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.apache.camel.Body;
import org.apache.camel.Exchange;
import org.apache.camel.Header;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author aubels
 *         Date: 29/08/13
 */
@Service
public class AuthBean {
    private static final Logger LOG = LoggerFactory.getLogger(AuthBean.class);

    private Map<String, AuthDetails> tokens = new HashMap<String, AuthDetails>();

    ServerBean serverBean;

    public void isLoggedIn(@Header("authToken") String authToken, Exchange exchange) throws AuthException {
        if (tokens.containsKey(authToken) && tokens.get(authToken).getLoggedIn()) return;
        throw new AuthException("User is not logged in.");
    }

    public void authenticate(@Header("user") String user, @Body String pass, Exchange exchange) throws AuthException {
        if (StringUtils.isBlank(user) || StringUtils.isBlank(pass))
        {
            LOG.info("User '{}' attempted to login with a blank username or password.", user);
            throw new AuthException("User was not logged in.");
        }
        List<User> users = getUsers();
        Map<String, User> userMap = new HashMap<String, User>();
        for (User each : users)
        {
            userMap.put(each.getUsername(), each);
        }
        User matchUser = userMap.get(user);
        if (matchUser == null)
        {
            LOG.info("User '{}' does not exist.", user);
            throw new AuthException("User was not logged in.");
        }
        if (!matchUser.getPassword().equals(pass))
        {
            LOG.info("User '{}' bad password entered.", user);
            throw new AuthException("User was not logged in.");
        }
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

    public void logout(@Header("authToken") String authToken, Exchange exchange) throws AuthException {
        if (tokens.containsKey(authToken) && tokens.get(authToken).getLoggedIn())
        {
            tokens.remove(authToken);
            return;
        }
        throw new AuthException("User is not logged in.");
    }

    public List<Group> getGroups()
    {
        String filename = FilenameUtils.concat(serverBean.getConfigPath(), "groups.yaml");
        File selectedYaml = null;
        try {
            selectedYaml = new File(new URI(filename));
        } catch (URISyntaxException e) {
            e.printStackTrace();
            LOG.error("Can not find: {}", filename);
            return null;
        }
        if (!selectedYaml.exists())
        {
            LOG.error("Can not find: {}", filename);
            return null;
        }
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        List<Group> groups = null;
        try {
            groups = mapper.readValue(selectedYaml, new TypeReference<List<Group>>(){});
        } catch (IOException e) {
            LOG.warn("Error parsing {}", filename);
            e.printStackTrace();
        }
        return groups;
    }

    public List<User> getUsers()
    {
        String filename = FilenameUtils.concat(serverBean.getConfigPath(), "users.yaml");
        File selectedYaml = null;
        try {
            selectedYaml = new File(new URI(filename));
        } catch (URISyntaxException e) {
            e.printStackTrace();
            LOG.error("Can not find: {}", filename);
            return null;
        }
        if (!selectedYaml.exists())
        {
            LOG.error("Can not find: {}", filename);
            return null;
        }
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        List<User> users = null;
        try {
            users = mapper.readValue(selectedYaml, new TypeReference<List<User>>(){});
        } catch (IOException e) {
            LOG.warn("Error parsing {}", filename);
            e.printStackTrace();
        }
        return users;
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

    public Map<String, AuthDetails> getTokens() {
        return tokens;
    }

    public ServerBean getServerBean() {
        return serverBean;
    }

    @Autowired
    public void setServerBean(ServerBean serverBean) {
        this.serverBean = serverBean;
    }
}
