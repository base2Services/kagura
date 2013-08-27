package com.base2.kagura.services.camel.routes;

import com.base2.kagura.shared.ResourcesUtils;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;

import java.io.File;
import java.util.HashMap;

/**
 * @author aubels
 *         Date: 26/08/13
 */
public class DataServiceRoutes extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        from("cxfrs:bean:rsServer?bindingStyle=SimpleConsumer")
                .log("Executed ${header.operationName}")
                .recipientList(simple("direct:rs-${header.operationName}"))
                .routeId("cxfrsInRouteId");

        from("direct:rs-test")
                .setBody(simple("hi"))
                .routeId("rsTestRouteId")
        ;

        from("direct:rs-favicon")
                .process(new Processor() {
                    @Override
                    public void process(Exchange exchange) throws Exception {
                        exchange.getIn().setBody(ResourcesUtils.getFaviconPng());
                    }
                })
                .routeId("rsFaviconRouteId")
        ;

        from("direct:rs-test2")
                .process(new Processor() {
                    @Override
                    public void process(Exchange exchange) throws Exception {
                        exchange.getIn().setBody(exchange.getIn().getHeaders().keySet());
                    }
                })
                .marshal().json(JsonLibrary.Jackson)
                .routeId("rsTest2RouteId")
        ;

    }
}
