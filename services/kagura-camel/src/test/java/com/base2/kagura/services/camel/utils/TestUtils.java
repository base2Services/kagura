package com.base2.kagura.services.camel.utils;

import com.base2.kagura.services.camel.kagura.ServerBean;
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
        try {
            return dir_url.toURI().toString();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static AbstractApplicationContext buildContext() {
        ClassPathXmlApplicationContext classPathXmlApplicationContext = new ClassPathXmlApplicationContext("/META-INF/spring/test.xml");
        ServerBean serverBean = (ServerBean)classPathXmlApplicationContext.getBean("serverBean");
        serverBean.setConfigPath(getResourcePath("TestReports"));
        return classPathXmlApplicationContext;
    }
}
