package com.base2.example.war.rest;

import com.base2.kagura.core.ExportHandler;
import com.base2.kagura.core.report.configmodel.ReportConfig;
import com.base2.kagura.core.report.configmodel.parts.ColumnDef;
import com.base2.kagura.core.report.connectors.ReportConnector;
import com.base2.kagura.rest.ReportsRest;
import com.base2.kagura.rest.exceptions.AuthenticationException;
import com.base2.kagura.rest.model.Parameters;
import com.base2.kagura.rest.model.ReportDetails;
import com.base2.kagura.rest.model.ReportDetailsAndResults;

import javax.ws.rs.*;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author aubels
 *         Date: 26/08/13
 */
@Path("/report/{authToken}/{reportName}")
@RequestScoped
public class ReportsRestImpl extends ReportsRest implements Serializable {

    String reportName;

    @PathParam("reportName")
    public String getReportName() {
        return reportName;
    }

    @PathParam("reportName")
    public void setReportName(String reportName) {
        this.reportName = reportName;
    }

    @PathParam("authToken")
    String authToken;

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    @Inject
    private KaguraBean kaguraBean;

    @Override
    public ReportDetails reportDetails()
    {
        if (!kaguraBean.userHasAccess(reportName)) return kaguraBean.noSuchReport(reportName, new ReportDetails());
        return kaguraBean.getReportDetails(reportName, true, new ReportDetails());
    }

    @Override
    public ReportDetailsAndResults runReport(
            boolean allpages,
            Integer pageLimit,
            int page,
            Parameters parameters
    ) throws AuthenticationException {
        ReportDetailsAndResults result = new ReportDetailsAndResults();
        if (kaguraBean.getUser() == null) throw new AuthenticationException();
        if (!kaguraBean.userHasAccess(reportName)) return kaguraBean.noSuchReport(reportName, result);
        ReportConnector reportConnector = kaguraBean.getConnector(reportName);
        if (reportConnector == null)
        {
            result.setErrors(new ArrayList<String>() {{
                add("Report error.");
            }});
            return result;
        }
        reportConnector.setPage(page);
        List<String> errors = new ArrayList<String>();
        if (allpages)
        {
            reportConnector.setPageLimit(KaguraBean.EXPORT_PAGE_LIMIT);
            reportConnector.setPage(0);
        }
        else
            if (pageLimit != null && pageLimit > 0)
                reportConnector.setPageLimit(Math.min(KaguraBean.EXPORT_PAGE_LIMIT, pageLimit));
        kaguraBean.insertParameters(parameters, reportConnector, errors);
        reportConnector.run(kaguraBean.generateExtraRunOptions());
        errors.addAll(reportConnector.getErrors());
        List<ColumnDef> columns = reportConnector.getColumns();
        List<Map<String, Object>> rows = reportConnector.getRows();
        result.setColumns(columns);
        result.setRows(rows);
        result.setErrors(errors);
        return result;
    }

    @Override
    public ReportDetailsAndResults detailsAndRunReport(
            boolean allpages,
            Integer pageLimit,
            int page,
            Parameters parameters
    ) throws AuthenticationException {
        if (kaguraBean.getUser() == null) throw new AuthenticationException("Authentication failure");
        if (!kaguraBean.userHasAccess(reportName)) return kaguraBean.noSuchReport(reportName, new ReportDetailsAndResults());
        ReportConfig reportConfig = kaguraBean.getReportConfig(reportName);
        ReportConnector reportConnector = reportConfig.getReportConnector();
        if (reportConnector == null)
        {
            return new ReportDetailsAndResults()
            {{
                setErrors(new ArrayList<String>() {{
                    add("Report error.");
                }});
            }};
        }
        reportConnector.setPage(page);
        List<String> errors = new ArrayList<String>();
        if (allpages)
        {
            reportConnector.setPageLimit(KaguraBean.EXPORT_PAGE_LIMIT);
            reportConnector.setPage(0);
        }
        else
        if (pageLimit != null && pageLimit > 0)
            reportConnector.setPageLimit(Math.min(KaguraBean.EXPORT_PAGE_LIMIT, pageLimit));
        final Map<String, Object> extra = kaguraBean.generateExtraRunOptions();
        reportConfig.prepareParameters(extra);
        ReportDetailsAndResults result = kaguraBean.getReportDetails(reportConfig, true, new ReportDetailsAndResults());
        kaguraBean.insertParameters(parameters, reportConnector, errors);
        reportConnector.run(extra);
        errors.addAll(reportConnector.getErrors());
        List<ColumnDef> columns = reportConnector.getColumns();
        List<Map<String, Object>> rows = reportConnector.getRows();
        result.setColumns(columns);
        result.setRows(rows);
        result.setErrors(errors);
        return result;
    }

    @Override
    public InputStream exportReport(
            boolean allpages,
            String filetype,
            Integer pageLimit,
            int page,
            Parameters parameters
    ) throws AuthenticationException {
        if (kaguraBean.getUser() == null) throw new AuthenticationException("Authentication failure");
        if (!kaguraBean.userHasAccess(reportName)) return null;
        ExportHandler exportHandler = new ExportHandler();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ReportConnector reportConnector = kaguraBean.getConnector(reportName);
        try {
            List<String> errors = new ArrayList<String>();
            kaguraBean.insertParameters(parameters, reportConnector, errors);
            reportConnector.setPage(page);
            if (allpages)
            {
                reportConnector.setPageLimit(KaguraBean.EXPORT_PAGE_LIMIT);
                reportConnector.setPage(0);
            }
            else
            if (pageLimit != null && pageLimit > 0)
                reportConnector.setPageLimit(Math.min(KaguraBean.EXPORT_PAGE_LIMIT, pageLimit));
            reportConnector.run(kaguraBean.generateExtraRunOptions());
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

}
