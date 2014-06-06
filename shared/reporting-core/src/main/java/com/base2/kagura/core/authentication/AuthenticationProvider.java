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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * @author aubels
 *         Date: 2/10/13
 */
public abstract class AuthenticationProvider {
    private static final Logger LOG = LoggerFactory.getLogger(AuthenticationProvider.class);

    public abstract List<Group> getGroups();

    public Set<String> getUserReports(String username)
    {
        Map<String, User> userMap = getStringUserMap();
        return getUserReports(username, userMap);
    }

    public Set<String> getUserReports(String username, Map<String, User> userMap) {
        User user = userMap.get(username);
        return getUserReports(user);
    }

    public Set<String> getUserReports(User user) {
        Set<String> result = new LinkedHashSet<String>();
        Map<String, Group> groupMap = getStringGroupMap();
        for (String group : user.getGroups()) {
            if (groupMap.containsKey(group)) {
                result.addAll(groupMap.get(group).getReports());
            } else
                LOG.warn("Group '{}' does not exist.", group);
        }
        return result;
    }

    public abstract List<User> getUsers();

    public Map<String, User> getStringUserMap() {
        List<User> users = getUsers();
        Map<String, User> userMap = new HashMap<String, User>();
        if (users == null) return userMap;
        for (User each : users) {
            userMap.put(each.getUsername(), each);
        }
        return userMap;
    }

    public Map<String, Group> getStringGroupMap() {
        List<Group> groups = getGroups();
        Map<String, Group> groupMap = new HashMap<String, Group>();
        if (groups == null) return groupMap;
        for (Group each : groups) {
            groupMap.put(each.getGroupname(), each);
        }
        return groupMap;
    }

    public abstract void authenticateUser(String user, String pass) throws Exception;

    public User getUser(String username) {
        Map<String, User> userMap = getStringUserMap();
        User user = userMap.get(username);
        return user;
    }
}
