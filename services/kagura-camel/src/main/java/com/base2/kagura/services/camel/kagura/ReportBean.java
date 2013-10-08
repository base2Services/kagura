package com.base2.kagura.services.camel.kagura;

import com.base2.kagura.core.reporting.view.report.ExportHandler;
import com.base2.kagura.core.reporting.view.report.ColumnDef;
import com.base2.kagura.core.reporting.view.report.parameterTypes.ParamConfig;
import com.base2.kagura.core.reporting.view.report.configmodel.ReportConfig;
import com.base2.kagura.core.reporting.view.report.configmodel.ReportsConfig;
import com.base2.kagura.core.reporting.view.report.connectors.ReportConnector;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.camel.Exchange;
import org.apache.camel.Header;
import org.apache.commons.beanutils.ConversionException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.base2.kagura.services.camel.model.Parameters;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author aubels
 *         Date: 28/08/13
 */
@Service
public class ReportBean {
    private static final Integer EXPORT_PAGE_LIMIT = 1000;
    private ServerBean serverBean;
    private static final Logger LOG = LoggerFactory.getLogger(ReportBean.class);

    private ReportConnector getConnector(String name)
    {
        ReportsConfig reportsConfig = getReportsConfig();
        ReportConfig reportConfig = reportsConfig.getReports().get(name);
        if (reportConfig == null)
            return null;
        return reportConfig.getReportConnector();
    }

    private ReportsConfig getReportsConfig() {
        LOG.info("Opening {}", serverBean.getConfigPath());
        try {
            return ReportsConfig.getConfig(serverBean.getConfigPath());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Map<String, Object> getReportDetails(String reportName, boolean full) {
        Map<String, Object> result = new HashMap<String, Object>();
        return getReportDetails(reportName, full, result);
    }

    private Map<String, Object> getReportDetails(String reportName, boolean full, Map<String, Object> result) {
        ReportConfig reportConfig = getReportConfig(reportName, result);
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

    private ReportConfig getReportConfig(String reportName, Map<String, Object> result) {
        ReportsConfig reportsConfig = getReportsConfig();
        ReportConfig reportConfig = reportsConfig.getReports().get(reportName);
        if (reportConfig == null)
            return null;
        if (reportsConfig.getErrors() != null)
            result.put("errors", reportsConfig.getErrors());
        return reportConfig;
    }

    public Map<String, Object> getReportsDetailed(@Header("reportId") String reportId, Exchange exchange) throws AuthenticationException {
        Map<String, Object> result = getReportDetails(reportId, true);
        return result;
    }

    public Map<String, Object> run(
            @Header("reportId") String reportId
            , @Header("page") int page
            , @Header("allpages") boolean all
            , @Header("pageLimit") Integer pageLimit
            , @Header("parameters") Parameters parameters) throws AuthenticationException {
        Map<String, Object> result = new HashMap<String, Object>();
        ReportConnector reportConnector = getConnector(reportId);
        reportConnector.setPage(page);
        List<String> errors = new ArrayList<String>();
        if (all)
            reportConnector.setPageLimit(EXPORT_PAGE_LIMIT);
        else
            if (pageLimit != null && pageLimit > 0)
                reportConnector.setPageLimit(Math.min(EXPORT_PAGE_LIMIT, pageLimit));
        insertParameters(parameters, reportConnector, errors);
        reportConnector.run();
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
            , @Header("parameters") Parameters parameters) throws AuthenticationException {
        Map<String, Object> result = new HashMap<String, Object>();
        getReportDetails(reportId, true, result);
        ReportConnector reportConnector = getConnector(reportId);
        reportConnector.setPage(page);
        List<String> errors = new ArrayList<String>();
        if (all)
            reportConnector.setPageLimit(EXPORT_PAGE_LIMIT);
        else
            if (pageLimit != null && pageLimit > 0)
                reportConnector.setPageLimit(Math.min(EXPORT_PAGE_LIMIT, pageLimit));
        insertParameters(parameters, reportConnector, errors);
        reportConnector.run();
        errors.addAll(reportConnector.getErrors());
        List<ColumnDef> columns = reportConnector.getColumns();
        List<Map<String, Object>> rows = reportConnector.getRows();
        result.put("columns",columns);
        result.put("rows",rows);
        result.put("errors", errors);
        return result;
    }

    private void insertParameters(Parameters parameters, ReportConnector reportConnector, List<String> errors) {
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

    public ByteArrayInputStream export(@Header("reportId") String reportId
            , Exchange exchange
            , @Header("page") int page
            , @Header("allpages") boolean all
            , @Header("filetype") String filetype
            , @Header("pageLimit") Integer pageLimit
            , @Header("parameters") Parameters parameters) throws AuthenticationException {
        ExportHandler exportHandler = new ExportHandler();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ReportConnector reportConnector = getConnector(reportId);
        try {

            List<String> errors = new ArrayList<String>();
            insertParameters(parameters, reportConnector, errors);
            reportConnector.setPage(page);
            if (all)
                reportConnector.setPageLimit(EXPORT_PAGE_LIMIT);
            else
                if (pageLimit != null && pageLimit > 0)
                    reportConnector.setPageLimit(Math.min(EXPORT_PAGE_LIMIT, pageLimit));
            reportConnector.run();
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
