package com.base2.example.war.rest;

import com.base2.kagura.core.ExportHandler;
import com.base2.kagura.core.report.configmodel.parts.ColumnDef;
import com.base2.kagura.core.report.connectors.ReportConnector;
import com.base2.kagura.rest.ReportsRest;
import com.base2.kagura.rest.model.Parameters;

import javax.ws.rs.*;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.security.Identity;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author aubels
 *         Date: 26/08/13
 */
@Path("/report/{authToken}/{reportId}")
@RequestScoped
//@LoggedIn // Best to handle this in code.
public class ReportsRestImpl extends ReportsRest {

//    @Inject
//    Identity identity;

//    @PathParam("reportId")
//    String reportId;
//    @PathParam("authToken")
//    String authToken;

    @Inject
    private KaguraBean kaguraBean;

    @Override
    public Response reportDetails()
    {
        return KaguraBean.makeResponse("Test");
    }

    @Override
    public Response runReport(
            boolean allpages,
            Integer pageLimit,
            int page,
            Parameters parameters
    )
    {
        Map<String, Object> result = new HashMap<String, Object>();
//        if (restrictions.isReportAccess(identity))
        {
            ReportConnector reportConnector = kaguraBean.getConnector(reportId);
            if (reportConnector == null)
            {
                result.put("errors", new ArrayList<String>() {{ add("Report error."); }});
                return KaguraBean.makeResponse(result);
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
            result.put("columns",columns);
            result.put("rows",rows);
            result.put("errors", errors);
//        } else {
//            result.put("errors", new ArrayList<String>() {{ add("Not logged in."); }});
        }
        return KaguraBean.makeResponse(result);
    }

    @Override
    public Response detailsAndRunReport(
            boolean allpages,
            Integer pageLimit,
            int page,
            Parameters parameters
    )
    {
        Map<String, Object> result = kaguraBean.getReportDetails(reportId, true);
//        if (restrictions.isReportAccess(identity))
        {
            ReportConnector reportConnector = kaguraBean.getConnector(reportId);
            if (reportConnector == null)
            {
                result.put("errors", new ArrayList<String>() {{ add("Report error."); }});
                return KaguraBean.makeResponse(result);
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
            result.put("columns",columns);
            result.put("rows",rows);
            result.put("errors", errors);
//        } else {
//            result.put("errors", new ArrayList<String>() {{ add("Not logged in."); }});
        }
        return KaguraBean.makeResponse(result);
    }

    @Override
    public Response exportReport(
            boolean allpages,
            String filetype,
            Integer pageLimit,
            int page,
            Parameters parameters
    )
    {
//        if (restrictions.isReportAccess(identity))
        {

            ExportHandler exportHandler = new ExportHandler();
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ReportConnector reportConnector = kaguraBean.getConnector(reportId);
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
            return KaguraBean.makeResponse(new ByteArrayInputStream(out.toByteArray()));
        } //else return null;
    }
}
