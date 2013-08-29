package com.base2.kagura.services.camel.routes;

import com.base2.kagura.services.camel.utils.TestUtils;
import com.jayway.restassured.response.ResponseBody;
import junit.framework.Assert;
import org.apache.camel.test.spring.CamelSpringTestSupport;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.springframework.context.support.AbstractApplicationContext;

import java.io.IOException;

import static com.jayway.restassured.RestAssured.expect;
import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;

/**
 * @author aubels
 *         Date: 28/08/13
 */
public class FaviconRoutesSystemTest extends CamelSpringTestSupport {

    @Test
    public void faviconRewrite() throws IOException {
        expect().contentType("image/x-icon").when().get("http://localhost:8432/favicon.ico");
    }

    @Override
    protected AbstractApplicationContext createApplicationContext() {
        return TestUtils.testUtils();
    }
}
