package com.base2.kagura.services.camel.model;

import java.util.List;

/**
 * @author aubels
 *         Date: 29/08/13
 */
public class User {
    private String username;
    private List<String> groups;
    private String password;

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
}
