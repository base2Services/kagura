package com.base2.kagura.core.reporting.view.authentication;

import com.base2.kagura.core.reporting.view.authentication.model.Group;
import com.base2.kagura.core.reporting.view.authentication.model.User;
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

    public abstract List<User> getUsers();

    public Map<String, User> getStringUserMap() {
        List<User> users = getUsers();
        Map<String, User> userMap = new HashMap<String, User>();
        for (User each : users) {
            userMap.put(each.getUsername(), each);
        }
        return userMap;
    }

    public Map<String, Group> getStringGroupMap() {
        List<Group> users = getGroups();
        Map<String, Group> userMap = new HashMap<String, Group>();
        for (Group each : users) {
            userMap.put(each.getGroupname(), each);
        }
        return userMap;
    }

    public abstract void authenticateUser(String user, String pass) throws Exception;
}
