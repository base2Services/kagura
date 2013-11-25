package com.base2.kagura.core.storage;

import com.base2.kagura.core.report.configmodel.ReportConfig;
import com.base2.kagura.core.report.configmodel.ReportsConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.google.common.io.PatternFilenameFilter;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * @author aubels
 *         Date: 15/10/13
 */
public abstract class ReportsProvider<InternalType> {
    protected List<String> errors = new ArrayList<String>();

    protected abstract String loadReport(ReportsConfig result, InternalType report) throws Exception;
    protected abstract InternalType[] getReportList();

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

    public ReportsConfig getReportsConfig() {
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
            try {
                name = loadReport(result, report);
            } catch (Exception e) {
                errors.add("Error in report " + reportToName(report) + ": " + e.getMessage());
                e.printStackTrace();
                continue;
            }
        }
        return result;
    }

    protected abstract String reportToName(InternalType report);

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }

    public void resetErrors() {
        errors.clear();
    }
}
