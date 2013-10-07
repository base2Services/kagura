package com.base2.kagura.core.reporting.view.authentication;

import com.base2.kagura.core.reporting.view.authentication.model.Group;
import com.base2.kagura.core.reporting.view.authentication.model.User;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

public class RestAuthentication extends AuthenticationProvider {
    private static final Logger LOG = LoggerFactory.getLogger(RestAuthentication.class);
    private String url;

    public RestAuthentication(String url) {
        this.url = url;
    }

    protected RestAuthentication() {
    }

    public InputStream openUrl(String suffix) {
        try {
            return new URL(url + "/" + suffix).openStream();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void authenticateUser(String user, String pass) throws Exception {
        Map<String, User> userMap = getStringUserMap();
        User matchUser = userMap.get(user);
        if (matchUser == null)
        {
            LOG.info("User '{}' does not exist.", user);
            throw new Exception("User was not logged in.");
        }
        if (!matchUser.getPassword().equals(pass))
        {
            LOG.info("User '{}' bad password entered.", user);
            throw new Exception("User was not logged in.");
        }
    }


    @Override
    public List<Group> getGroups() {
        String urlSuffix = "groups";
        InputStream selectedYaml = openUrl(urlSuffix);
        if (selectedYaml == null) {
            LOG.error("Can not find: {}", urlSuffix);
            return null;
        }
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        List<Group> groups = null;
        try {
            groups = mapper.readValue(selectedYaml, new TypeReference<List<Group>>() {
            });
        } catch (IOException e) {
            LOG.warn("Error parsing {}", urlSuffix);
            e.printStackTrace();
        }
        return groups;
    }

    // Inefficient - But simple.
    @Override
    public Set<String> getUserReports(String username) {
        Set<String> result = new LinkedHashSet<String>();
        Map<String, User> userMap = getStringUserMap();
        Map<String, Group> groupMap = getStringGroupMap();
        User user = userMap.get(username);
        for (String group : user.getGroups()) {
            if (groupMap.containsKey(group)) {
                result.addAll(groupMap.get(group).getReports());
            } else
                LOG.warn("Group '{}' does not exist.", group);
        }
        return result;
    }

    @Override
    public List<User> getUsers() {
        String urlSuffix = "users";
        InputStream selectedYaml = openUrl(urlSuffix);
        if (selectedYaml == null) {
            LOG.error("Can not find: {}", urlSuffix);
            return null;
        }
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        List<User> users = null;
        try {
            users = mapper.readValue(selectedYaml, new TypeReference<List<User>>() {
            });
        } catch (IOException e) {
            LOG.warn("Error parsing {}", urlSuffix);
            e.printStackTrace();
        }
        return users;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}