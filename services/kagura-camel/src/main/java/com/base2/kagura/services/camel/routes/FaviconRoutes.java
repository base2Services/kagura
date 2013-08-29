package com.base2.kagura.services.camel.routes;

import com.base2.kagura.shared.ResourcesUtils;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;

/**
 * @author aubels
 *         Date: 26/08/13
 */
public class FaviconRoutes extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        from("cxfrs:bean:rsFaviconServer?bindingStyle=SimpleConsumer")
                .log("Executed ${header.operationName}")
                .recipientList(simple("direct:rs-${header.operationName}"))
                .routeId("cxfrsFaviconInRouteId");

        from("direct:rs-favicon")
                .process(new Processor() {
                    @Override
                    public void process(Exchange exchange) throws Exception {
                        exchange.getIn().setBody(ResourcesUtils.getFaviconPng());
                    }
                })
                .routeId("rsFaviconRouteId");

    }
}
