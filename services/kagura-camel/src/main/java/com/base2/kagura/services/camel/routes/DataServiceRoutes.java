package com.base2.kagura.services.camel.routes;

import org.apache.camel.builder.RouteBuilder;

/**
 * @author aubels
 *         Date: 26/08/13
 */
public class DataServiceRoutes extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        from("direct:returnData")
                .log("Executed")
                .routeId("RouteId");
    }
}
