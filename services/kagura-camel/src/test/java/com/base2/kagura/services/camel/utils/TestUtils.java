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
package com.base2.kagura.services.camel.utils;

import com.base2.kagura.services.camel.authentication.FileAuthentication;
import com.base2.kagura.services.camel.kagura.ServerBean;
import com.base2.kagura.services.camel.storage.FileReportsProvider;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.net.URISyntaxException;
import java.net.URL;

/**
 * @author aubels
 *         Date: 29/08/13
 */
public class TestUtils {
    public static String getResourcePath(String path)
    {
        URL dir_url = ClassLoader.getSystemResource(path);
        return dir_url.getFile();
    }

    public static AbstractApplicationContext buildContext() {
        ClassPathXmlApplicationContext classPathXmlApplicationContext = new ClassPathXmlApplicationContext("/META-INF/spring/test.xml");
        ServerBean serverBean = (ServerBean)classPathXmlApplicationContext.getBean("serverBean");
        serverBean.setConfigPath(getResourcePath("TestReports"));
        FileAuthentication fileAuthentication = (FileAuthentication)classPathXmlApplicationContext.getBean("fileAuthentication");
        fileAuthentication.setConfigPath(getResourcePath("TestReports"));
        FileReportsProvider fileReportsProvider = (FileReportsProvider)classPathXmlApplicationContext.getBean("fileReportsProvider");
        fileReportsProvider.setReportDirectory(getResourcePath("TestReports"));
        return classPathXmlApplicationContext;
    }
}
