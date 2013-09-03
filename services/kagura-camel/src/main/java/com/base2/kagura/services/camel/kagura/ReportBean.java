package com.base2.kagura.services.camel.kagura;

import com.base2.kagura.core.reporting.view.ReportExportBean;
import com.base2.kagura.core.reporting.view.report.ColumnDef;
import com.base2.kagura.core.reporting.view.report.configmodel.ReportConfig;
import com.base2.kagura.core.reporting.view.report.configmodel.ReportsConfig;
import com.base2.kagura.core.reporting.view.report.connectors.ReportConnector;
import org.apache.camel.Exchange;
import org.apache.camel.Header;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
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
        ReportConfig reportConfig = getReportConfig(reportName);
        Map<String, Object> result = null;
        if (reportConfig != null)
        {
            result = new HashMap<String, Object>();
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

    private ReportConfig getReportConfig(String reportName) {
        ReportsConfig reportsConfig = getReportsConfig();
        ReportConfig reportConfig = reportsConfig.getReports().get(reportName);
        if (reportConfig == null)
            return null;
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
            , @Header("pageLimit") int pageLimit) throws AuthenticationException {
        Map<String, Object> result = new HashMap<String, Object>();
        ReportConnector reportConnector = getConnector(reportId);
        reportConnector.setPage(page);
        if (all)
            reportConnector.setPageLimit(EXPORT_PAGE_LIMIT);
        else
            reportConnector.setPageLimit(Math.min(EXPORT_PAGE_LIMIT, pageLimit));
        reportConnector.run();
        List<ColumnDef> columns = reportConnector.getColumns();
        List<Map<String, Object>> rows = reportConnector.getRows();
        result.put("columns",columns);
        result.put("rows",rows);
        return result;
    }

    public ByteArrayInputStream export(@Header("reportId") String reportId
            , Exchange exchange
            , @Header("page") int page
            , @Header("allpages") boolean all
            , @Header("filetype") String filetype
            , @Header("pageLimit") int pageLimit) throws AuthenticationException {
        ReportExportBean reportExportBean = new ReportExportBean();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ReportConnector reportConnector = getConnector(reportId);
        try {
            reportConnector.setPage(page);
            if (all)
                reportConnector.setPageLimit(EXPORT_PAGE_LIMIT);
            else
                reportConnector.setPageLimit(Math.min(EXPORT_PAGE_LIMIT, pageLimit));
            reportConnector.run();
            List<ColumnDef> columns = reportConnector.getColumns();
            List<Map<String, Object>> rows = reportConnector.getRows();
            if (filetype.equalsIgnoreCase("pdf"))
            {
                reportExportBean.generatePdf(out, rows, columns);
            } else if (filetype.equalsIgnoreCase("csv"))
            {
                reportExportBean.generateCsv(out, rows, columns);
            } else if (filetype.equalsIgnoreCase("xls"))
            {
                reportExportBean.generateXls(out, rows, columns);
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
