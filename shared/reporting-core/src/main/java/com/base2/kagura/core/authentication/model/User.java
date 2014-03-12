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
package com.base2.kagura.core.authentication.model;

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
