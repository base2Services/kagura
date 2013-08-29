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
public class AuthRoutes extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        from("cxfrs:bean:rsAuthServer?bindingStyle=SimpleConsumer")
                .log("Executed ${header.operationName}")
                .doTry()
                    .recipientList(simple("direct:rs-${header.operationName}")).end()
                .doCatch(AuthException.class)
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
                .doCatch(AuthException.class)
                    .setBody(constant("Not OK"))
                .end()
                .routeId("rsTestAuthTokenRouteId");

    }
}
