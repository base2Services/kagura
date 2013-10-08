package com.base2.kagura.core.reporting.view.report.connectors;

import com.base2.kagura.core.reporting.view.report.configmodel.GroovyReportConfig;
import groovy.lang.GroovyShell;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: aubels
 * Date: 22/07/13
 * Time: 11:57 AM
 * To change this template use File | Settings | File Templates.
 */
public class GroovyDataReportConnector extends ReportConnector {
    private List<Map<String, Object>> rows;
    private String groovyScript;

    public GroovyDataReportConnector(GroovyReportConfig reportConfig) {
        super(reportConfig);
        groovyScript = reportConfig.getGroovy();
    }

    @Override
    public void run(Map<String, Object> extra) {
        try
        {
            rows = new ArrayList<Map<String, Object>>();
            GroovyShell groovyShell = new GroovyShell();
            groovyShell.setProperty("rows", rows);
            groovyShell.setProperty("columns", getColumns());
            groovyShell.setProperty("page", getPage());
            groovyShell.setProperty("pageLimit", getPageLimit());
            groovyShell.setProperty("params", getParameterConfig());
            groovyShell.setProperty("extra", extra);
            groovyShell.evaluate(groovyScript);
        } catch (Exception ex)
        {
            ex.printStackTrace();
            getErrors().add(ex.getMessage());
        }
    }

    public List<Map<String, Object>> getRows() {
        return rows;
    }

    public void setRows(List<Map<String, Object>> rows) {
        this.rows = rows;
    }

    public String getGroovyScript() {
        return groovyScript;
    }

    public void setGroovyScript(String groovyScript) {
        this.groovyScript = groovyScript;
    }
}
