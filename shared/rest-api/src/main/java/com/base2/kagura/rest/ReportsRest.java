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
package com.base2.kagura.rest;

import com.base2.kagura.rest.exceptions.AuthenticationException;
import com.base2.kagura.rest.model.Parameters;
import com.base2.kagura.rest.model.ReportDetails;
import com.base2.kagura.rest.model.ReportDetailsAndResults;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.InputStream;

/**
 * @author aubels
 *         Date: 26/08/13
 */
@Path("{authToken}/{reportId}")
public class ReportsRest {
//    @PathParam("reportId")
//    private String reportId;
//    @PathParam("authToken")
//    private String authToken;
    /**
     *
     * @return returns report details
     */
    @Path("details")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public ReportDetails reportDetails(){return null;}

    /**
     *
     * @return report run output
     */
    @Path("run")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public ReportDetailsAndResults runReport(
            @DefaultValue("false") @QueryParam("allpages") boolean allpages,
            @DefaultValue("10") @QueryParam("pageLimit") Integer pageLimit,
            @DefaultValue("0") @QueryParam("page") int page,
            @DefaultValue("{}") @QueryParam("parameters") Parameters parameters
    ) throws AuthenticationException {return null;}

    /**
     *
     * @return the report details AND the results of a report run
     */
    @Path("detailsAndRun")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public ReportDetailsAndResults detailsAndRunReport(
            @DefaultValue("false") @QueryParam("allpages") boolean allpages,
            @DefaultValue("10") @QueryParam("pageLimit") Integer pageLimit,
            @DefaultValue("0") @QueryParam("page") int page,
            @DefaultValue("{}") @QueryParam("parameters") Parameters parameters
    ) throws AuthenticationException {return null;}

    /**
     * Exports report as a file type;
     *
     * @param filetype File type, CSV, PDF, or XLS, defaults to CSV
     * @return Returns a list of reports
     */
    @Path("export.{filetype}")
    @GET
    @Produces("application/octet-stream")
    public InputStream exportReport(
            @DefaultValue("false") @QueryParam("allpages") boolean allpages,
            @DefaultValue("csv") @PathParam("filetype") String filetype,
            @DefaultValue("10") @QueryParam("pageLimit") Integer pageLimit,
            @DefaultValue("0") @QueryParam("page") int page,
            @DefaultValue("{}") @QueryParam("parameters") Parameters parameters
    ) throws AuthenticationException {return null;}
}
