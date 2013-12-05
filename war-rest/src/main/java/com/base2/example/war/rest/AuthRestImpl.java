package com.base2.example.war.rest;

import com.base2.kagura.rest.AuthRest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author aubels
 *         Date: 26/08/13
 */
@Path("/auth")
@RequestScoped
public class AuthRestImpl extends AuthRest implements Serializable
{
    private static final Logger LOG = LoggerFactory.getLogger(AuthRestImpl.class);

    @Inject
    KaguraBean kaguraBean;

    @Override
    public String getAuthToken(String user, String password){return "N/A";}

    @Override
    public String testAuthToken(String authToken){return "Ok";}

    @Override
    public String logout(String authToken){return "N/A";}

    @Override
    public Response getReports(String authToken)
    {
        final ArrayList<String> reports = kaguraBean.getReportList();
        return KaguraBean.makeResponse(reports);
    }

    @Override
    public Response getReportsDetailed(final String authToken)
    {
        return KaguraBean.makeResponse(new HashMap<String, Object>() {{
            for (String reportName : kaguraBean.getReportList()) {
                put(reportName, kaguraBean.getReportDetails(reportName, false));
            }
        }});
    }

}
