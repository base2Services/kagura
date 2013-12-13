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
import java.util.List;
import java.util.Map;

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
    public List<String> getReports(String authToken)
    {
        return new ArrayList<String>()
        {{
			add("Assets");
			add("Meters");
			add("RIOAllAssetsExports");
			add("RIOAllMetersAndRecordingsExceptInitialLoad");
			add("ScheduleSummary");
        }};
    }

    @Override
    public Map<String, Object> getReportsDetailed(final String authToken)
    {
        return new HashMap<String, Object>()
        {{
            for (String reportName : getReports(authToken))
            {
                put(reportName, kaguraBean.getReportDetails(reportName, false));
            }
        }};
    }
}
