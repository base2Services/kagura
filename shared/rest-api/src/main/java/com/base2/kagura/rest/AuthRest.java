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

import com.base2.kagura.rest.model.AuthenticationResult;
import com.base2.kagura.rest.model.ReportDetails;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.Map;

/**
 * @author aubels
 *         Date: 26/08/13
 */
public class AuthRest {
    /**
     * Allows you to login and see reports
     *
     *
     * @param user username
     * @param password Password, plaintext for the mean time
     * @return Auth token in JSON tag "token", or a plain text error in JSON tag "error".
     */
    @Path("login/{user}")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public AuthenticationResult getAuthToken(@PathParam("user") String user, String password){return null;}

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
    public Map<String, ReportDetails> getReportsDetailed(@PathParam("authToken") String authToken){return null;}

}
