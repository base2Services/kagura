package com.base2.kagura.services.camel.rest;

import javax.ws.rs.CookieParam;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * @author aubels
 *         Date: 26/08/13
 */
@Path("/")
public class ServerRest {
    /**
     * Returns a list of reports
     * @param authToken Authentication token
     * @return Returns a list of reports
     */
    @Path("reports")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<String> getReports(@CookieParam("AuthToken") String authToken){return null;}

    /**
     * Test command.
     * @return Returns "test"
     */
    @Path("test")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String test(){return null;}

    /**
     * Test command.
     * @return Returns camel headers
     */
    @Path("test2")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String test2(){return null;}

}
