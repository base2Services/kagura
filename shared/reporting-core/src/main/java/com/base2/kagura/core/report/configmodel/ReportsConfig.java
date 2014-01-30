package com.base2.kagura.core.report.configmodel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Container for reports loaded by a ReportsProvider. Also where you obtain errors from if you have issues loading
 * reports.
 * User: aubels
 * Date: 24/07/13
 * Time: 4:45 PM
 */
public class ReportsConfig {
    Map<String, ReportConfig> reports;
    List<String> errors;

    /**
     * A list of errors, when you check it, you will need to clear the entries. reports-core does not empty the errors
     * @return
     */
    public List<String> getErrors() {
        return errors;
    }

    /**
     * Map of report IDs to reports.
     * @return
     */
    public Map<String, ReportConfig> getReports() {
        return reports;
    }

    /**
     * @see #getReports()
     */
    protected void setReports(Map<String, ReportConfig> reports) {
        this.reports = reports;
    }

    /**
     * Constructor. Initializes reports and errors.
     */
    public ReportsConfig()
    {
        reports = new HashMap<String, ReportConfig>();
        errors = new ArrayList<String>();
    }

    /**
     * Retreives a set report. Delegate function for "getReports().get(reportId)"
     * @param reportId The report ID to load
     * @return The report or null if errored or doesn't exist.
     */
    public ReportConfig getReport(String reportId) {
        return reports.get(reportId);
    }
}
