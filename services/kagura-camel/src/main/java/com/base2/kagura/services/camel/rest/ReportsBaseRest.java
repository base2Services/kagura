package com.base2.kagura.services.camel.rest;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * @author aubels
 *         Date: 26/08/13
 */
@Path("/")
public class ReportsBaseRest {
    @Path("reports")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<String> getReports(@CookieParam("AuthToken") String authToken){return null;}

    @Path("test")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String test(){return null;}
}
