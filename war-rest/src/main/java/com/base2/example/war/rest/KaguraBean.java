package com.base2.example.war.rest;

import com.base2.kagura.core.authentication.AuthenticationProvider;
import com.base2.kagura.core.authentication.FileAuthentication;
import com.base2.kagura.core.authentication.model.User;
import com.base2.kagura.core.report.configmodel.ReportConfig;
import com.base2.kagura.core.report.configmodel.ReportsConfig;
import com.base2.kagura.core.report.connectors.ReportConnector;
import com.base2.kagura.core.storage.FileReportsProvider;
import com.base2.kagura.core.storage.ReportsProvider;
import com.base2.kagura.rest.helpers.ParameterUtils;
import com.base2.kagura.rest.model.ReportDetails;
import com.base2.kagura.rest.model.ReportDetailsAndResults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.*;

/**
 * @author aubels
 *         Date: 14/11/2013
 */
@Named
@SessionScoped
public class KaguraBean implements Serializable {
    private static final Logger LOG = LoggerFactory.getLogger(KaguraBean.class);
    static final int EXPORT_PAGE_LIMIT = 10000;
    ReportsProvider<?> reportsProvider;
    AuthenticationProvider authenticationProvider;
    User user;

    {{
        ParameterUtils.SetupDateConverters();
    }}

    @PostConstruct
    void init()
    {
        final String reportLoc = "/reports";
        reportsProvider = new FileReportsProvider(reportLoc);
        authenticationProvider = new FileAuthentication(reportLoc);
    }

    public <T extends ReportDetails> T noSuchReport(String reportName, T result) {
        result.setReportId(reportName);
        final String error = "No such report";
        result.setError(error);
        return result;
    }

    public <T extends ReportDetails> T getReportDetails(String reportName, boolean full, T result) {
        Map<String, Object> errors = new HashMap<String, Object>();
        ReportConfig reportConfig = getReportConfig(reportName, errors);
        if (reportConfig != null)
        {
            getReportDetails(reportConfig, full, result);
        }
        else
        {
            result = noSuchReport(reportName, result);
        }
        return result;
    }

    public <T extends ReportDetails> T getReportDetails(ReportConfig reportConfig, boolean full, T result) {
        reportConfig.prepareParameters(generateExtraRunOptions());
        result.setReportId(reportConfig.getReportId());
        result.setExtra(reportConfig.getExtraOptions());
        if (full)
        {
            result.setParams(reportConfig.getParamConfig());
            result.setColumns(reportConfig.getColumns());
        }
        return result;
    }

    public ReportConfig getReportConfig(String reportName, Map<String, Object> result) {
        ReportsConfig reportsConfig = getReportsConfig();
        ReportConfig reportConfig = reportsConfig.getReports().get(reportName);
        if (reportConfig == null)
            return null;
        if (reportsConfig.getErrors() != null)
            result.put("errors", reportsConfig.getErrors());
        return reportConfig;
    }

    public ReportsConfig getReportsConfig() {
        final ReportsConfig reportsConfig = reportsProvider.getReportsConfig();
        if (reportsProvider.getErrors() != null)
            for (String e : reportsProvider.getErrors())
                LOG.info("Report error: {}", e);
        return reportsConfig;
    }

    public ReportConnector getConnector(String reportId) {
        final ReportConfig reportConfig = getReportConfig(reportId);
        if (reportConfig != null)
            return reportConfig.getReportConnector();
        return null;
    }

    public ReportConfig getReportConfig(String reportId) {
        return getReportsConfig().getReports().get(reportId);
    }

    public Map<String, Object> generateExtraRunOptions() {
        return new HashMap<String, Object>()
        {{

        }};
    }

    public void authenticateUser(String user, String pass) throws Exception {
        authenticationProvider.authenticateUser(user, pass);
    }

    public User getUserModel(String username) {
        return authenticationProvider.getUser(username);
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<String> getUserReports() {
        if (user == null) return new HashSet<String>();
        return authenticationProvider.getUserReports(user.getUsername());
    }


    public boolean userHasAccess(String reportId) {
        return getUserReports().contains(reportId);
    }

    public ReportDetailsAndResults notLoggedIn(ReportDetailsAndResults result) {
        return null;
    }
}
