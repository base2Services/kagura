package com.base2.kagura.core.authentication;

import com.base2.kagura.core.authentication.model.Group;
import com.base2.kagura.core.authentication.model.User;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

public class FileAuthentication extends AuthenticationProvider {
    private static final Logger LOG = LoggerFactory.getLogger(FileAuthentication.class);
    private String configPath;

    public FileAuthentication(String configPath) {
        this.configPath = configPath;
    }

    protected FileAuthentication() {
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
        String filename = FilenameUtils.concat(configPath, "groups.yaml");
        InputStream selectedYaml = openFile(filename);
        if (selectedYaml == null) {
            LOG.error("Can not find: {}", filename);
            return null;
        }
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        List<Group> groups = null;
        try {
            groups = mapper.readValue(selectedYaml, new TypeReference<List<Group>>() {
            });
        } catch (IOException e) {
            LOG.warn("Error parsing {}", filename);
            e.printStackTrace();
        }
        return groups;
    }

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
        String filename = FilenameUtils.concat(configPath, "users.yaml");
        if (filename == null)
        {
            final String basePath = FilenameUtils.concat(System.getProperty("user.dir"), configPath);
            if (basePath == null)
            {
                LOG.error("Can not make directory using paths {}, {}", System.getProperty("user.dir"), configPath);
                return null;
            }
            filename = FilenameUtils.concat(basePath, "users.yaml");
        }
        InputStream selectedYaml = openFile(filename);
        if (selectedYaml == null) {
            LOG.error("Can not find: {}", filename);
            return null;
        }
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        List<User> users = null;
        try {
            users = mapper.readValue(selectedYaml, new TypeReference<List<User>>() {
            });
        } catch (IOException e) {
            LOG.warn("Error parsing {}", filename);
            e.printStackTrace();
        }
        return users;
    }

    public InputStream openFile(String file)
    {
        if (!new File(file).exists()) {
            URL dir_url = FileAuthentication.class.getResource(file);
            if (dir_url != null)
            {
                try {
                    return dir_url.openStream();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                return new URL(file).openStream();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            return FileUtils.openInputStream(new File(file));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getConfigPath() {
        return configPath;
    }

    public void setConfigPath(String configPath) {
        this.configPath = configPath;
    }
}