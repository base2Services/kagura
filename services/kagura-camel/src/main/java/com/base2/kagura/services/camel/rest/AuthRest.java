package com.base2.kagura.services.camel.rest;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.Map;

/**
 * @author aubels
 *         Date: 26/08/13
 */
@Path("/")
public class AuthRest {
    /**
     * Allows you to login and see reports
     * @param user username
     * @param password Password, plaintext for the mean time
     * @return Auth token in JSON tag "token", or a plain text error in JSON tag "error".
     */
    @Path("login/{user}")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public String getAuthToken(@PathParam("user") String user, String password){return null;}

    /**
     * Tests login status of token
     * @param authToken Auth token
     * @return either OK, or Not OK
     */
    @Path("test/{authToken}")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String testAuthToken(@PathParam("authToken") String authToken){return null;}

    /**
     * Allows users to logout
     * @param authToken Auth token
     * @return either Done, or Not done
     */
    @Path("logout/{authToken}")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String logout(@PathParam("authToken") String authToken){return null;}

    /**
     * Returns a list of reports
     * @param authToken Authentication token
     * @return Returns a list of report ids
     */
    @Path("reports/{authToken}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<String> getReports(@PathParam("authToken") String authToken){return null;}

    /**
     * Returns a list of reports
     * @param authToken Authentication token
     * @return Returns a map of reports
     */
    @Path("reportsDetails/{authToken}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, Object> getReportsDetailed(@PathParam("authToken") String authToken){return null;}

}
