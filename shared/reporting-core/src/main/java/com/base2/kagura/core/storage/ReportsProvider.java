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
package com.base2.kagura.core.storage;

import com.base2.kagura.core.report.configmodel.ReportConfig;
import com.base2.kagura.core.report.configmodel.ReportsConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.google.common.io.PatternFilenameFilter;
import org.apache.commons.io.FileUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * The base class for all ReportProviders. Allows a templated type, the "InternalType" type must be safe to pass
 * internally, and reversible, such as you can determine the report name from the report's instance of "InternalType"
 * @author aubels
 *         Date: 15/10/13
 */
public abstract class ReportsProvider<InternalType> {
    protected List<String> errors = new ArrayList<String>();

    /**
     * Loads the report, and stores it in ReportsConfig
     * @param result Where to store the report
     * @param report Which report to load
     * @return The name of the report / report id
     * @throws Exception
     */
    protected abstract String loadReport(ReportsConfig result, InternalType report) throws Exception;

    /**
     * This is one of the most important calls, it lists all the reports, which is then used to selectively (or in some
     * cases nonselectively) load the other reports.
     * @return
     */
    protected abstract InternalType[] getReportList();

    /**
     * Loads a report, this does the deserialization, this method can be substituted with another, called by child
     * classes. Once it has deserialized it, it put it into the map.
     * @param result
     * @param report
     * @param reportName
     * @return
     */
    protected boolean loadReport(ReportsConfig result, InputStream report, String reportName) {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        ReportConfig reportConfig = null;
        try {
            reportConfig = mapper.readValue(report, ReportConfig.class);
        } catch (IOException e) {
            e.printStackTrace();
            errors.add("Error parsing " + reportName + " " + e.getMessage());
            return false;
        }
        reportConfig.setReportId(reportName);
        result.getReports().put(reportName, reportConfig);
        return true;
    }

    /**
     * Returns a reports Configuration with all reports loaded. Strongly recommend using
     *          getReportsConfig(Collection<String> restrictToNamed)
     * as it will only load the reports with the names passed in. This is useful such as if you passed the reports which
     * the current user can access.
     * @return The reports configration
     */
    public ReportsConfig getReportsConfig() {
        return getReportsConfig(null);
    }

    /**
     * Returns a list of reports in a ReportsConfig-uration object, it only loads reports in "restrictedToNamed", this
     * is useful as a secondary report-restriction mechanism. So you would pass in all reports the user can access.
     * @param restrictToNamed A list of report names to load. No error if one is not found.
     * @return
     */
    public ReportsConfig getReportsConfig(Collection<String> restrictToNamed) {
        resetErrors();
        ReportsConfig result = new ReportsConfig();
        InternalType[] reports = getReportList();
        if (reports == null)
        {
            errors.add("No reports found.");
            return result;
        }
        for (InternalType report : reports)
        {
            String name = "Unknown";
            final String named = reportToName(report);
            if (restrictToNamed != null && !restrictToNamed.contains(named)) continue;
            try {
                name = loadReport(result, report);
            } catch (Exception e) {
                errors.add("Error in report " + named + ": " + e.getMessage());
                e.printStackTrace();
                continue;
            }
        }
        return result;
    }

    /**
     * Mapping of InternalType to a report name. In the case of a FileReportsProvider, it is the directory name containing
     * the reportconf.yaml or reportconf.json file.
     * @param report
     * @return
     */
    protected abstract String reportToName(InternalType report);

    /**
     * Stores loading errors here. Once checked #resetErrors()
     * @return
     */
    public List<String> getErrors() {
        return errors;
    }

    /**
     * @see #getErrors()
     * @param errors
     */
    public void setErrors(List<String> errors) {
        this.errors = errors;
    }

    /**
     * Clears reports.
     */
    public void resetErrors() {
        errors.clear();
    }
}
