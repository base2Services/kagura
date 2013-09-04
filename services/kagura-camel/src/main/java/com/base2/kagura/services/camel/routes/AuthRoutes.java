package com.base2.kagura.services.camel.routes;

import com.base2.kagura.services.camel.kagura.AuthenticationException;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;

/**
 * @author aubels
 *         Date: 26/08/13
 */
public class AuthRoutes extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        from("cxfrs:bean:rsAuthServer?bindingStyle=SimpleConsumer")
                .log("Executed ${header.operationName}")
                .doTry()
                    .recipientList(simple("direct:rs-${header.operationName}")).end()
                .doCatch(AuthenticationException.class)
                    .beanRef("authBean", "buildAuthFail")
                    .marshal().json(JsonLibrary.Jackson)
                .end()
                .routeId("cxfrsAuthInRouteId");

        from("direct:rs-getAuthToken")
                .beanRef("authBean", "authenticate")
                .marshal().json(JsonLibrary.Jackson)
                .routeId("rsGetAuthRouteId");

        from("direct:rs-testAuthToken")
                .doTry()
                    .beanRef("authBean", "isLoggedIn")
                    .setBody(constant("OK"))
                .doCatch(AuthenticationException.class)
                    .setBody(constant("Not OK"))
                .end()
                .routeId("rsTestAuthTokenRouteId");

        from("direct:rs-logout")
                .doTry()
                    .beanRef("authBean", "logout")
                    .setBody(constant("Done"))
                .doCatch(AuthenticationException.class)
                    .setBody(constant("Not done"))
                .end()
                .routeId("rsLogoutRouteId");

        from("direct:rs-getReports")
                .beanRef("authBean", "getReports")
                .marshal().json(JsonLibrary.Jackson)
                .routeId("rsGetReportsRouteId");

        from("direct:rs-getReportsDetailed")
                .beanRef("authBean", "getReportsDetailed")
                .marshal().json(JsonLibrary.Jackson)
                .routeId("rsGetReportsDetailedRouteId");

        from("timer:cleanAuths?period=600000")
                .log("cleaning Auth routes")
                .beanRef("authBean", "cleanAuthTickets")
                .routeId("cleanAuthsRouteId");
    }
}
