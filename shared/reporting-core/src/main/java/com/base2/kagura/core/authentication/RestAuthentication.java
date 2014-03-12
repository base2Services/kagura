/*
   Copyright 2014 base2Services

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */
package com.base2.kagura.core.authentication;

import com.base2.kagura.core.authentication.model.Group;
import com.base2.kagura.core.authentication.model.User;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

public class RestAuthentication extends AuthenticationProvider {
    private static final Logger LOG = LoggerFactory.getLogger(RestAuthentication.class);
    private String url;

    public RestAuthentication(String url) {
        this.url = url;
    }

    public RestAuthentication() {
    }

    public InputStream httpGet(String suffix) {
        try {
            return new URL(url + "/" + suffix).openStream();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public InputStream httpPost(String suffix, HashMap<String, String> values) {
        try {
            URL obj = new URL(url + "/" + suffix);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            con.setRequestMethod("POST");

            String data = new ObjectMapper().writeValueAsString(values);

            con.setRequestProperty("Content-Type", "application/json");
            con.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(data);
            wr.flush();
            wr.close();

            int responseCode = con.getResponseCode();
            if (responseCode != 200) throw new Exception("Got error code: " + responseCode);
            return con.getInputStream();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void authenticateUser(final String user, final String pass) throws Exception {
        Map<String, User> userMap = getStringUserMap();
        User matchUser = userMap.get(user);
        if (matchUser == null)
        {
            LOG.info("User '{}' does not exist.", user);
            throw new Exception("User was not logged in.");
        }
        String result = IOUtils.toString(httpPost("login",new HashMap<String,String>(){{put("username",user);put("password",pass);}}));
        if (!"\"ok\"".equals(result))
        {
            LOG.info("User '{}' {}.", user, result);
            throw new Exception("User " + user + " " + result);
        }
    }

    @Override
    public List<Group> getGroups() {
        String urlSuffix = "groups";
        InputStream selectedYaml = httpGet(urlSuffix);
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
        InputStream selectedYaml = httpGet(urlSuffix);
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