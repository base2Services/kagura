package com.base2.kagura.services.camel.rest;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * @author aubels
 *         Date: 26/08/13
 */
@Path("{authToken}/{reportId}")
public class ReportsRest {
    @PathParam("reportId")
    String reportId;
    @PathParam("authToken")
    String authToken;
    /**
     *
     * @return Returns a list of reports
     */
    @Path("details")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Object reportDetails(){return null;}

    /**
     *
     * @return Returns a list of reports
     */
    @Path("run")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Object runReport(@DefaultValue("0") @QueryParam("page") String page){return null;}

    /**
     * Exports report as a file type;
     * @param filetype File type, CSV, PDF, or XLS, defaults to CSV
     * @return Returns a list of reports
     */
    @Path("export")
    @GET
    @Produces(MediaType.WILDCARD)
    public Object exportReport(@DefaultValue("CSV") @QueryParam("filetype") String filetype){return null;}

}
