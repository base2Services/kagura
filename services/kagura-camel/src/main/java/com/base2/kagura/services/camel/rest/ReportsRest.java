package com.base2.kagura.services.camel.rest;

import com.base2.kagura.services.camel.kagura.AuthenticationException;
import org.apache.camel.Header;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.Map;
import java.util.List;
import com.base2.kagura.services.camel.model.Parameters;

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
    public Object runReport(
            @DefaultValue("false") @QueryParam("allpages") boolean allpages,
            @DefaultValue("10") @QueryParam("pageLimit") int pageLimit,
            @DefaultValue("0") @QueryParam("page") int page,
            @DefaultValue("{}") @QueryParam("parameters") Parameters parameters
    ){return null;}

    /**
     * Exports report as a file type;
     * @param filetype File type, CSV, PDF, or XLS, defaults to CSV
     * @return Returns a list of reports
     */
    @Path("export.{filetype}")
    @GET
    @Produces("application/octet-stream")
    public Object exportReport(
            @DefaultValue("false") @QueryParam("allpages") boolean allpages,
            @DefaultValue("csv") @PathParam("filetype") String filetype,
            @DefaultValue("10") @QueryParam("pageLimit") int pageLimit,
            @DefaultValue("0") @QueryParam("page") int page,
            @DefaultValue("{}") @QueryParam("parameters") Parameters parameters
    ){return null;}
}
