package com.base2.kagura.core.reporting.view.authentication;

import com.base2.kagura.core.reporting.view.authentication.model.Group;
import com.base2.kagura.core.reporting.view.authentication.model.User;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author aubels
 *         Date: 2/10/13
 */
public interface AuthenticationProvider {
    Map<String, User> getStringUserMap();

    Map<String, Group> getStringGroupMap();

    List<Group> getGroups();

    Set<String> getUserReports(String username);

    List<User> getUsers();
}
