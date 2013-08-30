package com.base2.kagura.services.camel.routes;

import com.base2.kagura.services.camel.utils.TestUtils;
import com.jayway.restassured.response.ResponseBody;
import org.apache.camel.test.spring.CamelSpringTestSupport;
import org.junit.Test;
import org.springframework.context.support.AbstractApplicationContext;

import static com.jayway.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
/**
 * @author aubels
 *         Date: 28/08/13
 */
public class AuthRoutesSystemTest extends CamelSpringTestSupport {

    @Test
    public void tryAuthFailNopass()
    {
        expect().body(equalTo("{\"error\":\"Authentication failure\",\"token\":\"\"}"))
                .when().request().body("").post("http://localhost:8432/auth/login/username");
    }
    @Test
    public void tryAuthFailGoodPass()
    {
        expect()
                    .body("error",equalTo(""))
                    .body("issue",not(equalTo("")))
                .when().request().body("testuserpass").post("http://localhost:8432/auth/login/testuser");
    }
    @Test
    public void authenticationPersists()
    {
        ResponseBody login = given().request().body("testuserpass").post("http://localhost:8432/auth/login/testuser").body();
        String token = login.jsonPath().get("token");
        expect().body(equalTo("OK"))
                .when().get("http://localhost:8432/auth/test/{1}", token);
    }
    @Test
    public void authenticationPersistsInvalidFails()
    {
        ResponseBody login = given().request().body("testuserpass").post("http://localhost:8432/auth/login/testuser").body();
        String token = login.jsonPath().get("token");
        expect().body(equalTo("Not OK"))
                .when().get("http://localhost:8432/auth/test/{1}", token+"asdf");
    }

    @Test
    public void authenticationLogoutWorks()
    {
        ResponseBody login = given().request().body("testuserpass").post("http://localhost:8432/auth/login/testuser").body();
        String token = login.jsonPath().get("token");
        expect().body(equalTo("OK")).when().get("http://localhost:8432/auth/test/{1}", token);
        expect().body(equalTo("Done")).when().get("http://localhost:8432/auth/logout/{1}", token);
        expect().body(equalTo("Not OK")).when().get("http://localhost:8432/auth/test/{1}", token);
    }

    @Test
    public void correctReportsAreReturned()
    {
        ResponseBody login = given().request().body("testuserpass").post("http://localhost:8432/auth/login/testuser").body();
        String token = login.jsonPath().get("token");
        expect().body(equalTo("[\"fake1\"]")).when().get("http://localhost:8432/auth/reports/{1}", token);

    }

    @Test
    public void correctReportsDetailsAreReturned()
    {
        ResponseBody login = given().request().body("testuserpass").post("http://localhost:8432/auth/login/testuser").body();
        String token = login.jsonPath().get("token");

        ResponseBody details = expect().get("http://localhost:8432/auth/reportsDetails/{1}", token);
        expect()
                .body("fake1.extra.displayPriority", equalTo("1"))
                .body("fake1.extra.image", equalTo("fake.png"))
                .body("fake1.extra.reportName", equalTo("Fake sample 1"))
                .when().get("http://localhost:8432/auth/reportsDetails/{1}", token);

    }

    @Override
    protected AbstractApplicationContext createApplicationContext() {
        return TestUtils.buildContext();
    }

}
