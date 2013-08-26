package com.base2.kagura.core.reporting.view;

import com.base2.kagura.core.reporting.view.report.ColumnDef;
import com.base2.kagura.core.reporting.view.report.ParamConfig;
import com.base2.kagura.core.reporting.view.report.ReportConnectorFactorySingleton;
import com.base2.kagura.core.reporting.view.report.connectors.FakeDataReportConnector;
import com.base2.kagura.core.reporting.view.report.connectors.ReportConnector;

import java.io.*;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: aubels
 * Date: 16/07/13
 * Time: 4:45 PM
 * To change this template use File | Settings | File Templates.
 */
public class ReportBean implements Serializable {
    public static final int EXPORT_PAGE_LIMIT = 10000;
    private String reportId;
    private List<ColumnDef> columns;
    private List<String> errors;
    private List<ParamConfig> parameterConfig;
    private List<Map<String, Object>> rows; // Second string may become "object" inlight of changes regarding columns and jdbc output.
    private ReportConnector reportConnector;

    private ReportExportBean reportExportBean;

    private ReportConnectorFactorySingleton reportConnectorFactorySingleton;

    public ReportBean(ReportExportBean reportExportBean, ReportConnectorFactorySingleton reportConnectorFactorySingleton) {
        this.reportExportBean = reportExportBean;
        this.reportConnectorFactorySingleton = reportConnectorFactorySingleton;
        errors = null;
    }

    public void init(){
            resetState();
    }

    private void resetState() {
        if ("N/A".equals(reportId))
        {
            Object reportParams = null;
            reportConnector = new FakeDataReportConnector(reportParams);
        } else {
            reportConnector = reportConnectorFactorySingleton.getConnector(reportId);
        }
        regenerate();
    }

    public void regenerate()
    {
        if (reportConnector == null) return;
        reportConnector.setPage(0);
        runNGet();
    }

    private void runNGet() {
        reportConnector.run();
        columns = reportConnector.getColumns();
        rows = reportConnector.getRows();
        parameterConfig = reportConnector.getParameterConfig();
        errors.addAll(reportConnector.getErrors());
        reportConnector.clearErrors();
    }

    public boolean hasNextPage()
    {
        return rows != null && rows.size() >= reportConnector.getPageLimit();
    }

    public boolean hasPreviousPage()
    {
        return rows != null && reportConnector.getPage() > 0;
    }

    public void setPage(int page)
    {
        if (reportConnector == null) return;
        reportConnector.setPage(page);
    }

    public void nextPage()
    {
        if (reportConnector == null || !hasNextPage()) return;
        setPage(getPage() + 1);
        runNGet();
    }

    public void previousPage()
    {
        if (reportConnector == null || !hasPreviousPage()) return;
        setPage(getPage() - 1);
        runNGet();
    }

    public int getPage()
    {
        if (reportConnector == null) return 0;
        return reportConnector.getPage();
    }

    public OutputStream render()
    {
        return null;
    }

    public String getReportId() {
        return reportId;
    }

    public void setReportId(String reportId) {
        this.reportId = reportId;
    }

    public List<Map<String, Object>> getRows() {
        return rows;
    }

    public void setRows(List<Map<String, Object>> rows) {
        this.rows = rows;
    }

    public List<ParamConfig> getParameterConfig() {
        return parameterConfig;
    }

    public void setParameterConfig(List<ParamConfig> parameterConfig) {
        this.parameterConfig = parameterConfig;
    }

    public List<ColumnDef> getColumns() {
        return columns;
    }

    public void setColumns(List<ColumnDef> columns) {
        this.columns = columns;
    }

    public OutputStream download(String filetype, boolean all) {
//        response.setHeader("Content-Disposition", "attachment;filename=" + getReportId() + "." + filetype);
        OutputStream out = new ByteArrayOutputStream();
        int prevpage = getPage();
        int prevlimit = reportConnector.getPageLimit();
        try {
            if (all)
            {
                setPage(0);
                reportConnector.setPageLimit(EXPORT_PAGE_LIMIT);
                runNGet();
            }
            if (filetype.equalsIgnoreCase("pdf"))
            {
                reportExportBean.generatePdf(out, getRows(), getColumns());
            } else if (filetype.equalsIgnoreCase("csv"))
            {
                reportExportBean.generateCsv(out, getRows(), getColumns());
            } else if (filetype.equalsIgnoreCase("xls"))
            {
                reportExportBean.generateXls(out, getRows(), getColumns());
            }
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException err) {
                err.printStackTrace();
            }
            setPage(prevpage);
            reportConnector.setPageLimit(prevlimit);
            runNGet();
        }
        return out;
    }

    public List<String> getErrors() {
        return errors;
    }
}
