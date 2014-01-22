package com.base2.kagura.services.camel.kagura;

import com.base2.kagura.core.ExportHandler;
import com.base2.kagura.core.report.configmodel.parts.ColumnDef;
import com.base2.kagura.core.report.parameterTypes.ParamConfig;
import com.base2.kagura.core.report.configmodel.ReportConfig;
import com.base2.kagura.core.report.configmodel.ReportsConfig;
import com.base2.kagura.core.report.connectors.ReportConnector;
import com.base2.kagura.core.storage.ReportsProvider;
import com.base2.kagura.rest.exceptions.AuthenticationException;
import com.base2.kagura.rest.helpers.ParameterUtils;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.camel.Exchange;
import org.apache.camel.Header;
import org.apache.commons.beanutils.ConversionException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.base2.kagura.rest.model.Parameters;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * @author aubels
 *         Date: 28/08/13
 */
@Service
public class ReportBean {
    private ServerBean serverBean;
    private static final Logger LOG = LoggerFactory.getLogger(ReportBean.class);
    @Autowired()
    private ReportsProvider<?> reportsProvider;


    private ReportConnector getConnector(String name)
    {
        ReportsConfig reportsConfig = getReportsConfig(Arrays.asList(name));
        ReportConfig reportConfig = reportsConfig.getReports().get(name);
        if (reportConfig == null)
            return null;
        return reportConfig.getReportConnector();
    }

    public ReportsConfig getReportsConfig(Collection<String> reports) {
        final ReportsConfig reportsConfig = reportsProvider.getReportsConfig(reports);
        if (reportsProvider.getErrors() != null)
            for (String e : reportsProvider.getErrors())
                LOG.info("Report error: {}", e);
        return reportsConfig;
    }

    public Map<String, Object> getReportDetails(String reportName, boolean full, ReportConfig reportConfig) {
        Map<String, Object> result = new HashMap<String, Object>();
        if (reportConfig != null)
        {
            result.put("reportId", reportName);
            result.put("extra", reportConfig.getExtraOptions());
            if (full)
            {
                result.put("params", reportConfig.getParamConfig());
                result.put("columns", reportConfig.getColumns());
            }
        }
        else
        {
            result = noSuchReport(reportName);
        }
        return result;
    }

    public Map<String, Object> noSuchReport(String reportName) {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("reportId", reportName);
        result.put("error", "No such report");
        return result;
    }

    public void noSuchReport(@Header("reportId") String reportName, Exchange exchange) {
        Map<String, Object> result = noSuchReport(reportName);
        exchange.getOut().setBody(result);
    }

    private ReportConfig getReportConfig(String reportName, Map<String, Object> result, Collection<String> reports) {
        ReportsConfig reportsConfig = getReportsConfig(reports);
        ReportConfig reportConfig = reportsConfig.getReport(reportName);
        if (reportConfig == null)
            return null;
        if (reportsConfig.getErrors() != null)
            result.put("errors", reportsConfig.getErrors());
        return reportConfig;
    }

    public Map<String, Object> getReportsDetailed(@Header("reportId") String reportId, Exchange exchange) throws AuthenticationException {
        Map<String, Object> result = new HashMap<String, Object>();
        final ReportConfig reportConfig = getReportConfig(reportId, result, Arrays.asList(reportId));
        return getReportDetails(reportId, true, reportConfig);
    }

    public Map<String, Object> run(
            @Header("reportId") String reportId
            , @Header("page") int page
            , @Header("allpages") boolean all
            , @Header("pageLimit") Integer pageLimit
            , @Header("parameters") Parameters parameters
            , @Header("authDetails") AuthBean.AuthDetails authDetails
            , @Header("groups") Collection<String> groups
            , @Header("userExtra") Map<String, Object> userExtra
    ) throws AuthenticationException {
        Map<String, Object> result = new HashMap<String, Object>();
        ReportConnector reportConnector = getConnector(reportId);
        reportConnector.setPage(page);
        List<String> errors = new ArrayList<String>();
        if (all)
            reportConnector.setPageLimit(serverBean.getExportLimit());
        else
            if (pageLimit != null && pageLimit > 0)
                reportConnector.setPageLimit(Math.min(serverBean.getExportLimit(), pageLimit));
        ParameterUtils.insertParameters(parameters, reportConnector, errors);
        reportConnector.run(generateExtraRunOptions(authDetails,groups, userExtra));
        errors.addAll(reportConnector.getErrors());
        List<ColumnDef> columns = reportConnector.getColumns();
        List<Map<String, Object>> rows = reportConnector.getRows();
        result.put("columns",columns);
        result.put("rows",rows);
        result.put("errors", errors);
        return result;
    }

    public Map<String, Object> detailsAndRun(
            @Header("reportId") String reportId
            , @Header("page") int page
            , @Header("allpages") boolean all
            , @Header("pageLimit") Integer pageLimit
            , @Header("parameters") Parameters parameters
            , @Header("authDetails") AuthBean.AuthDetails authDetails
            , @Header("groups") Collection<String> groups
            , @Header("userExtra") Map<String, Object> userExtra
        ) throws AuthenticationException {
        Map<String, Object> result = new HashMap<String, Object>();
        final ReportConfig reportConfig = getReportConfig(reportId, result, Arrays.asList(reportId));
        result.putAll(getReportDetails(reportId, true, reportConfig));
        ReportConnector reportConnector = getConnector(reportId);
        reportConnector.setPage(page);
        List<String> errors = new ArrayList<String>();
        if (all)
            reportConnector.setPageLimit(serverBean.getExportLimit());
        else
            if (pageLimit != null && pageLimit > 0)
                reportConnector.setPageLimit(Math.min(serverBean.getExportLimit(), pageLimit));
        ParameterUtils.insertParameters(parameters, reportConnector, errors);
        final Map<String, Object> extra = generateExtraRunOptions(authDetails, groups, userExtra);
        reportConfig.prepareParameters(extra);
        reportConnector.run(extra);
        errors.addAll(reportConnector.getErrors());
        List<ColumnDef> columns = reportConnector.getColumns();
        List<Map<String, Object>> rows = reportConnector.getRows();
        result.put("columns",columns);
        result.put("rows",rows);
        result.put("errors", errors);
        return result;
    }

    public Map<String, Object> generateExtraRunOptions(final AuthBean.AuthDetails authDetails, final Collection<String> groups, final Map<String, Object> userExtra) {
        return new HashMap<String, Object>()
        {{
             put("username", authDetails.getUsername());
             put("groups",groups);
             put("userExtra",userExtra);
        }};
    }

    public ByteArrayInputStream export(@Header("reportId") String reportId
            , Exchange exchange
            , @Header("page") int page
            , @Header("allpages") boolean all
            , @Header("filetype") String filetype
            , @Header("pageLimit") Integer pageLimit
            , @Header("parameters") Parameters parameters
            , @Header("authDetails") AuthBean.AuthDetails authDetails
            , @Header("groups") Collection<String> groups
            , @Header("userExtra") Map<String, Object> userExtra
    ) throws AuthenticationException {
        ExportHandler exportHandler = new ExportHandler();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ReportConnector reportConnector = getConnector(reportId);
        try {
            List<String> errors = new ArrayList<String>();
            ParameterUtils.insertParameters(parameters, reportConnector, errors);
            reportConnector.setPage(page);
            if (all)
                reportConnector.setPageLimit(serverBean.getExportLimit());
            else
                if (pageLimit != null && pageLimit > 0)
                    reportConnector.setPageLimit(Math.min(serverBean.getExportLimit(), pageLimit));
            reportConnector.run(generateExtraRunOptions(authDetails,groups, userExtra));
            List<ColumnDef> columns = reportConnector.getColumns();
            List<Map<String, Object>> rows = reportConnector.getRows();
            if (filetype.equalsIgnoreCase("pdf"))
            {
                exportHandler.generatePdf(out, rows, columns);
            } else if (filetype.equalsIgnoreCase("csv"))
            {
                exportHandler.generateCsv(out, rows, columns);
            } else if (filetype.equalsIgnoreCase("xls"))
            {
                exportHandler.generateXls(out, rows, columns);
            }
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException err) {
                err.printStackTrace();
            }
        }
        return new ByteArrayInputStream(out.toByteArray());
    }

    @Autowired
    public void setServerBean(ServerBean serverBean) {
        this.serverBean = serverBean;
    }

    public ServerBean getServerBean() {
        return serverBean;
    }
}
