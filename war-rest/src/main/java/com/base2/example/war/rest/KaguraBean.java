package com.base2.example.war.rest;

import com.base2.kagura.core.authentication.AuthenticationProvider;
import com.base2.kagura.core.authentication.FileAuthentication;
import com.base2.kagura.core.authentication.model.User;
import com.base2.kagura.core.report.configmodel.ReportConfig;
import com.base2.kagura.core.report.configmodel.ReportsConfig;
import com.base2.kagura.core.report.connectors.ReportConnector;
import com.base2.kagura.core.report.parameterTypes.ParamConfig;
import com.base2.kagura.core.storage.FileReportsProvider;
import com.base2.kagura.core.storage.ReportsProvider;
import com.base2.kagura.rest.model.Parameters;
import com.base2.kagura.rest.model.ReportDetails;
import com.base2.kagura.rest.model.ReportDetailsAndResults;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConversionException;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.DateConverter;
import org.apache.commons.beanutils.converters.DateTimeConverter;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
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
        DateTimeConverter dtConverter = new DateConverter(null);
        dtConverter.setPatterns(new String[] {"yyyy-MM-dd", "yyyy-MM-dd hh:mm:ss"});
        ConvertUtils.register(dtConverter, Date.class);
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
            result.setReportId(reportName);
            result.setExtra(reportConfig.getExtraOptions());
            if (full)
            {
                result.setParams(reportConfig.getParamConfig());
                result.setColumns(reportConfig.getColumns());
            }
        }
        else
        {
            result = noSuchReport(reportName, result);
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
        final ReportConfig reportConfig = getReportsConfig().getReports().get(reportId);
        if (reportConfig != null)
            return reportConfig.getReportConnector();
        return null;
    }

    public Map<String, Object> generateExtraRunOptions() {
        return new HashMap<String, Object>()
        {{

        }};
    }

    public void insertParameters(Parameters parameters, ReportConnector reportConnector, List<String> errors) {
        if (reportConnector.getParameterConfig() != null)
        {
            for (ParamConfig paramConfig : reportConnector.getParameterConfig())
            {
                if (parameters.getParameters().containsKey(paramConfig.getId()))
                {
                    Object o = parameters.getParameters().get(paramConfig.getId());
                    try {
                        if (o != null && StringUtils.isNotBlank(o.toString()))
                            BeanUtils.setProperty(paramConfig, "value", o);
                        else
                            BeanUtils.setProperty(paramConfig, "value", null);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    } catch (ConversionException e){
                        e.printStackTrace();
                        errors.add("Could not convert parameter: " + paramConfig.getId() + " value " + o);
                    }
                }
            }
        }
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
