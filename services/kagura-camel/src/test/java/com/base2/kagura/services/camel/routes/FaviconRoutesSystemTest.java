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
import org.apache.camel.test.spring.CamelSpringTestSupport;
import org.junit.Test;
import org.springframework.context.support.AbstractApplicationContext;

import java.io.IOException;

import static com.jayway.restassured.RestAssured.expect;
import static com.jayway.restassured.RestAssured.given;
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
        return TestUtils.buildContext();
    }
}
