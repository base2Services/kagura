package com.base2.kagura.services.camel.routes;

import com.base2.kagura.services.camel.kagura.AuthenticationException;
import com.base2.kagura.services.camel.kagura.AuthorizationException;
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

        from("direct:rs-exportReport")
                .beanRef("reportBean", "export")
                .routeId("rsExportReportRouteId");
    }
}
