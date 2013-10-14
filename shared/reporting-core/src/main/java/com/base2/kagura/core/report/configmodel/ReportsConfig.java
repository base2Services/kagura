package com.base2.kagura.core.report.configmodel;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.google.common.io.PatternFilenameFilter;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: aubels
 * Date: 24/07/13
 * Time: 4:45 PM
 * To change this template use File | Settings | File Templates.
 */
public class ReportsConfig {
    Map<String, ReportConfig> reports;
    List<String> errors;

    public List<String> getErrors() {
        return errors;
    }

    public Map<String, ReportConfig> getReports() {
        return reports;
    }

    protected void setReports(Map<String, ReportConfig> reports) {
        this.reports = reports;
    }

    protected ReportsConfig()
    {
        reports = new HashMap<String, ReportConfig>();
        errors = new ArrayList<String>();
    }

    public static ReportsConfig getConfig(String report_directory) throws URISyntaxException, MalformedURLException {
        ReportsConfig result = new ReportsConfig();
        File file = new File(report_directory);
        if (file == null || !file.exists()) {
            result.errors.add("Couldn't open report directory, doesn't exist.");
            return result;
        }
        File[] reports = file.listFiles();
        if (reports == null)
        {
            result.errors.add("No reports in report directory found.");
            return result;
        }
        for (File report : reports)
        {
            try {
                if (report.isDirectory())
                {
                    FilenameFilter configYamlFilter = new PatternFilenameFilter("^reportconf.(yaml|json)$");
                    File[] selectYaml = report.listFiles(configYamlFilter);
                    if (selectYaml != null && selectYaml.length == 1)
                    {
                        File selectedYaml = selectYaml[0];
                        if (loadReport(result, FileUtils.openInputStream(selectedYaml), report.getName())) continue;
                    }
                }
            } catch (Exception e) {
                result.errors.add("Error in report " + report.getName() + ": " + e.getMessage());
                e.printStackTrace();
                continue;
            }
        }
        return result;
    }

    public static ReportsConfig getConfig(URI report_directory, List<String> reportIDs) throws URISyntaxException, MalformedURLException {
        ReportsConfig result = new ReportsConfig();
        for (String reportID : reportIDs)
        {
            try {
                if (loadReport(result, report_directory.resolve(reportID).toURL().openStream(), reportID)) continue;
            } catch (Exception e) {
                result.errors.add("Error in report " + reportID + ": " + e.getMessage());
                e.printStackTrace();
                continue;
            }
        }
        return result;
    }

    private static boolean loadReport(ReportsConfig result, InputStream report, String reportName) throws IOException {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        ReportConfig reportConfig = mapper.readValue(report, ReportConfig.class);
        reportConfig.setReportId(reportName);
        result.getReports().put(reportName, reportConfig);
        return true;
    }
}
