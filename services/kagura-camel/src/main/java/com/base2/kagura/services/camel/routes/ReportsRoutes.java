package com.base2.kagura.services.camel.routes;

import com.base2.kagura.services.camel.kagura.AuthException;
import com.base2.kagura.shared.ResourcesUtils;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;

/**
 * @author aubels
 *         Date: 26/08/13
 */
public class ReportsRoutes extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        from("cxfrs:bean:rsReportsServer?bindingStyle=SimpleConsumer")
                .log("Executing ${header.operationName}")
                .doTry()
                    .beanRef("authBean", "isLoggedIn")
                    .recipientList(simple("direct:rs-${header.operationName}")).end()
                .doCatch(AuthException.class)
                    .log("Authentication failed ${header.operationName}")
                    .beanRef("authBean", "buildAuthFail")
                    .marshal().json(JsonLibrary.Jackson)
                .end()

                .recipientList(simple("direct:rs-${header.operationName}"))
                .routeId("cxfrsReportsInRouteId");

        from("direct:rs-reportDetails")
                .setBody(simple("hi"))
                .routeId("rsReportDetailsRouteId");

        from("direct:rs-runReport")
                .setBody(simple("hi"))
                .routeId("rsRunReportRouteId");

        from("direct:rs-exportReport")
                .setBody(simple("hi"))
                .routeId("rsExportReportRouteId");
    }
}
