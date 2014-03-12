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

import com.base2.kagura.rest.exceptions.AuthenticationException;
import com.base2.kagura.rest.exceptions.AuthorizationException;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jackson.JacksonDataFormat;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.spi.DataFormat;

/**
 * @author aubels
 *         Date: 26/08/13
 */
public class ReportsRoutes extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        DataFormat jsonDataFormat = new JacksonDataFormat();
        from("cxfrs:bean:rsReportsServer?bindingStyle=SimpleConsumer")
                .log("Executing ${header.operationName}")
                .doTry()
                    .beanRef("authBean", "isLoggedIn")
                    .beanRef("authBean", "canAccessReport")
                    .recipientList(simple("direct:rs-${header.operationName}")).end()
                .doCatch(AuthenticationException.class)
                    .log("Authentication failed ${header.operationName}")
                    .beanRef("authBean", "buildAuthFail")
                    .marshal(jsonDataFormat)
                .doCatch(AuthorizationException.class)
                    .log("Report authorization failed ${header.operationName} ${header.reportId}")
                    .beanRef("reportBean", "noSuchReport")
                    .marshal(jsonDataFormat)
                .end()
                .routeId("cxfrsReportsInRouteId");

        from("direct:rs-reportDetails")
                .beanRef("reportBean", "getReportsDetailed")
                .marshal().json(JsonLibrary.Jackson)
                .routeId("rsReportDetailsRouteId");

        from("direct:rs-runReport")
                .beanRef("reportBean", "run")
                .marshal().json(JsonLibrary.Jackson)
                .routeId("rsRunReportRouteId");

        from("direct:rs-detailsAndRunReport")
                .beanRef("reportBean", "detailsAndRun")
                .marshal().json(JsonLibrary.Jackson)
                .routeId("rsDetailsAndRunReportRouteId");

        from("direct:rs-exportReport")
                .beanRef("reportBean", "export")
                .routeId("rsExportReportRouteId");
    }
}
