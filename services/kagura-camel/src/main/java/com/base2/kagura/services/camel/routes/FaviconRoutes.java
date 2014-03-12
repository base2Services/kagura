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
