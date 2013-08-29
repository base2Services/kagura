package com.base2.kagura.services.camel.routes;

import org.apache.camel.test.spring.CamelSpringTestSupport;
import org.junit.Test;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import static com.jayway.restassured.RestAssured.expect;
import static org.hamcrest.Matchers.equalTo;

/**
 * @author aubels
 *         Date: 28/08/13
 */
public class ServerRoutesSystemTest extends CamelSpringTestSupport {

    @Test
    public void test()
    {
        expect().body(equalTo("hi")).when().get("http://localhost:8432/server/test");
    }

    @Override
    protected AbstractApplicationContext createApplicationContext() {
        return new ClassPathXmlApplicationContext("/META-INF/spring/beans-test.xml");
    }
}
