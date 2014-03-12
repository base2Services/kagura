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
package com.base2.kagura.services.camel.routes;

import com.base2.kagura.services.camel.utils.TestUtils;
import com.jayway.restassured.response.ResponseBody;
import org.apache.camel.test.spring.CamelSpringTestSupport;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.context.support.AbstractApplicationContext;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.*;

import static com.jayway.restassured.RestAssured.expect;
import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import org.apache.commons.codec.net.URLCodec;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.commons.codec.EncoderException;

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
                .body("extra.image", equalTo("fake1.png"))
                .body("extra.reportName", equalTo("Fake sample 1"))
                .body("reportId", equalTo("fake1"))
                .when().get("http://localhost:8432/report/{1}/fake1/details", token);
    }

    @Test
    public void reportDetailsAndRunTest()
    {
        ResponseBody login = given().request().body("testuserpass").post("http://localhost:8432/auth/login/testuser").body();
        String token = login.jsonPath().get("token");
        expect()
                .body("extra.displayPriority", equalTo("1"))
                .body("extra.image", equalTo("fake1.png"))
                .body("extra.reportName", equalTo("Fake sample 1"))
                .body("reportId", equalTo("fake1"))
                .body("columns",hasSize(3))
                .body("rows",hasSize(2))
                .when().get("http://localhost:8432/report/{1}/fake1/detailsAndRun", token);
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
        expect()
                .body("columns",hasSize(3))
                .body("rows",hasSize(2))
                .when().get("http://localhost:8432/report/{1}/fake1/run", token);
    }

    @Test
    public void reportExportCSVTest()
    {
        ResponseBody login = given().request().body("testuserpass").post("http://localhost:8432/auth/login/testuser").body();
        String token = login.jsonPath().get("token");
        ResponseBody responseBody =  expect().when().get("http://localhost:8432/report/{1}/fake1/export.csv", token);
        byte[] bytes = responseBody.asByteArray();
        Assert.assertThat(bytes.length, equalTo(54));
    }

    @Test
    public void reportExportPDFTest()
    {
        ResponseBody login = given().request().body("testuserpass").post("http://localhost:8432/auth/login/testuser").body();
        String token = login.jsonPath().get("token");
        ResponseBody responseBody =  expect().when().get("http://localhost:8432/report/{1}/fake1/export.pdf", token);
        byte[] bytes = responseBody.asByteArray();
        Assert.assertThat(bytes.length, equalTo(1216));
    }

    @Test
    public void reportExportXLSTest()
    {
        ResponseBody login = given().request().body("testuserpass").post("http://localhost:8432/auth/login/testuser").body();
        String token = login.jsonPath().get("token");
        ResponseBody responseBody =  expect().when().get("http://localhost:8432/report/{1}/fake1/export.xls", token);
        byte[] bytes = responseBody.asByteArray();
        Assert.assertThat(bytes.length, equalTo(4096));
    }

    @Test
    public void reportExportCSVParamsTest() throws JsonProcessingException, EncoderException
    {
        ResponseBody login = given().request().body("testuserpass").post("http://localhost:8432/auth/login/testuser").body();
        String token = login.jsonPath().get("token");
        String param = buildParameters();
        ResponseBody responseBody =  expect().when().get("http://localhost:8432/report/{1}/fake1/export.csv?parameters={2}", token, param);
        byte[] bytes = responseBody.asByteArray();
        Assert.assertThat(bytes.length, equalTo(54));
    }

    @Test
    public void reportExportPDFParamsTest() throws JsonProcessingException, EncoderException
    {
        ResponseBody login = given().request().body("testuserpass").post("http://localhost:8432/auth/login/testuser").body();
        String token = login.jsonPath().get("token");
        String param = buildParameters();
        ResponseBody responseBody =  expect().when().get("http://localhost:8432/report/{1}/fake1/export.pdf?parameters={2}", token, param);
        byte[] bytes = responseBody.asByteArray();
        Assert.assertThat(bytes.length, equalTo(1216));
    }

    @Test
    public void reportExportXLSParamsTest() throws JsonProcessingException, EncoderException
    {
        ResponseBody login = given().request().body("testuserpass").post("http://localhost:8432/auth/login/testuser").body();
        String token = login.jsonPath().get("token");
        String param = buildParameters();
        ResponseBody responseBody =  expect().when().get("http://localhost:8432/report/{1}/fake1/export.xls?parameters={2}", token, param);
        byte[] bytes = responseBody.asByteArray();
        Assert.assertThat(bytes.length, equalTo(4096));
    }

    private String buildParameters() throws JsonProcessingException, EncoderException {
//        List<Map<String, String>> values = new ArrayList<Map<String, String>>();
        Map<String, String> entry = new HashMap<String, String>();
        entry.put("An anonymous string", "asdf");
//        values.add(entry);
        ObjectMapper mapper = new ObjectMapper(); // can reuse, share globally«
        String json = mapper.writeValueAsString(entry);
//        String url = new URLCodec().encode(json); // Rest assured seems to be doing the URI encoding.
        return json;
    }

    @Override
    protected AbstractApplicationContext createApplicationContext() {
        return TestUtils.buildContext();
    }
}
