/*
   Copyright 2014 base2Services

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */
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
