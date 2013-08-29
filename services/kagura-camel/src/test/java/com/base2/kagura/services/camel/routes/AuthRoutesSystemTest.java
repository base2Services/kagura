package com.base2.kagura.services.camel.routes;

import org.apache.camel.test.spring.CamelSpringTestSupport;
import org.junit.Test;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import static com.jayway.restassured.RestAssured.*;
import static com.jayway.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.Matchers.*;
/**
 * @author aubels
 *         Date: 28/08/13
 */
public class AuthRoutesSystemTest extends CamelSpringTestSupport {

    @Test
    public void test()
    {
        expect().body(equalTo("hi")).when().get("http://localhost:8432/auth/test");
    }

    @Override
    protected AbstractApplicationContext createApplicationContext() {
        return new ClassPathXmlApplicationContext("/META-INF/spring/beans-test.xml");
//        return new ClassPathXmlApplicationContext("classpath:/META-INF/spring/rsserver.xml");
    }
}
