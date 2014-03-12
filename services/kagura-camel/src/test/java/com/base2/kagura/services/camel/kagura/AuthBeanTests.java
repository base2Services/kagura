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
package com.base2.kagura.services.camel.kagura;

import com.base2.kagura.core.authentication.model.Group;
import com.base2.kagura.core.authentication.model.User;
import com.base2.kagura.services.camel.authentication.FileAuthentication;
import com.base2.kagura.services.camel.utils.TestUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import static org.hamcrest.Matchers.*;

/**
 * @author aubels
 *         Date: 29/08/13
 */
public class AuthBeanTests {

    @Test
    public void getGroupsTest()
    {
        ServerBean serverBean = new ServerBean();
        serverBean.setConfigPath(TestUtils.getResourcePath("TestReports"));
        AuthBean authBean = new AuthBean();
        final FileAuthentication authenticationProvider = new FileAuthentication();
        authenticationProvider.setConfigPath(TestUtils.getResourcePath("TestReports"));
        authBean.setAuthenticationProvider(authenticationProvider);
        authBean.setServerBean(serverBean);
        List<Group> groups = authBean.getGroups();
        Assert.assertNotNull(groups);
        Assert.assertThat("Incorrect number of groups returned", groups, hasSize(2));
        Assert.assertEquals("test reports", groups.get(0).getGroupname());
        Assert.assertThat(groups.get(0).getReports(), contains("fake1"));
    }
    
    @Test
    public void getUsersTest()
    {
        ServerBean serverBean = new ServerBean();
        serverBean.setConfigPath(TestUtils.getResourcePath("TestReports"));
        final FileAuthentication authenticationProvider = new FileAuthentication();
        authenticationProvider.setConfigPath(TestUtils.getResourcePath("TestReports"));
        AuthBean authBean = new AuthBean();
        authBean.setAuthenticationProvider(authenticationProvider);
        authBean.setServerBean(serverBean);
        List<User> users = authBean.getUsers();
        Assert.assertNotNull(users);
        Assert.assertThat("Incorrect number of users returned", users, hasSize(2));
        Assert.assertThat(users, contains(
                allOf(hasProperty("username", equalTo("testuser")), hasProperty("password", equalTo("testuserpass")), hasProperty("groups", hasSize(1)), hasProperty("groups", contains("test reports"))),
                allOf(hasProperty("username", equalTo("tu2")), hasProperty("password", equalTo("tup2")), hasProperty("groups", hasSize(1)), hasProperty("groups", contains("test reports2")))
        ));
    }
}
