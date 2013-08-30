package com.base2.kagura.services.camel.routes;

import com.base2.kagura.services.camel.utils.TestUtils;
import com.jayway.restassured.response.ResponseBody;
import org.apache.camel.test.spring.CamelSpringTestSupport;
import org.junit.Test;
import org.springframework.context.support.AbstractApplicationContext;

import static com.jayway.restassured.RestAssured.expect;
import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

/**
 * @author aubels
 *         Date: 28/08/13
 */
public class ReportsRoutesSystemTest extends CamelSpringTestSupport {

    @Test
    public void reportDetailsTest()
    {
        ResponseBody login = given().request().body("testuserpass").post("http://localhost:8432/auth/login/testuser").body();
        String token = login.jsonPath().get("token");
        expect()
                .body("extra.displayPriority", equalTo("1"))
                .body("extra.image", equalTo("fake.png"))
                .body("extra.reportName", equalTo("Fake sample 1"))
                .body("reportId", equalTo("fake1"))
                .when().get("http://localhost:8432/report/{1}/fake1/details", token);
    }

    @Test
    public void reportDetailsNoRightsTest()
    {
        ResponseBody login = given().request().body("testuserpass").post("http://localhost:8432/auth/login/testuser").body();
        String token = login.jsonPath().get("token");
        expect()
                .body("error", equalTo("No such report"))
                .body("reportId", equalTo("fake2"))
                .when().get("http://localhost:8432/report/{1}/fake2/details", token);
    }

    @Test
    public void reportDetailsNoReportTest()
    {
        ResponseBody login = given().request().body("testuserpass").post("http://localhost:8432/auth/login/testuser").body();
        String token = login.jsonPath().get("token");
        expect()
                .body("reportId", equalTo("fake-1"))
                .body("error", equalTo("No such report"))
                .when().get("http://localhost:8432/report/{1}/fake-1/details", token);
    }

    @Test
    public void reportRunTest()
    {
        ResponseBody login = given().request().body("testuserpass").post("http://localhost:8432/auth/login/testuser").body();
        String token = login.jsonPath().get("token");
        expect().body(equalTo("hi")).when().get("http://localhost:8432/report/{1}/fake1/run", token);
    }

    @Test
    public void reportExportTest()
    {
        ResponseBody login = given().request().body("testuserpass").post("http://localhost:8432/auth/login/testuser").body();
        String token = login.jsonPath().get("token");
        expect().body(equalTo("hi")).when().get("http://localhost:8432/report/{1}/fake1/export", token);
    }


    @Override
    protected AbstractApplicationContext createApplicationContext() {
        return TestUtils.buildContext();
    }
}
