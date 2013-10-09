package com.base2.kagura.core.reporting.view.authentication.model;

import java.util.List;
import java.util.Map;

/**
 * @author aubels
 *         Date: 29/08/13
 */
public class User {
    private String username;
    private List<String> groups;
    private String password;
    private Map<String, Object> extraOptions;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<String> getGroups() {
        return groups;
    }

    public void setGroups(List<String> groups) {
        this.groups = groups;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Map<String, Object> getExtraOptions() {
        return extraOptions;
    }

    public void setExtraOptions(Map<String, Object> extraOptions) {
        this.extraOptions = extraOptions;
    }
}
