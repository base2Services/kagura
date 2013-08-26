package com.base2.kagura.core.reporting.view.report.configmodel;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.google.common.io.PatternFilenameFilter;

import java.io.File;
import java.io.FilenameFilter;
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

    public static ReportsConfig getConfig(String report_directory) {
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
                    FilenameFilter configYamlFilter = new PatternFilenameFilter("reportconf.yaml");
                    File[] selectYaml = report.listFiles(configYamlFilter);
                    if (selectYaml != null && selectYaml.length == 1)
                    {
                        File selectedYaml = selectYaml[0];
                        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
                        ReportConfig reportConfig = mapper.readValue(selectedYaml, ReportConfig.class);
                        reportConfig.setReportId(report.getName());
                        result.getReports().put(report.getName(), reportConfig);
                        continue;
                    }
                    FilenameFilter configJsonFilter = new PatternFilenameFilter("reportconf.json");
                    File[] selectJson = report.listFiles(configJsonFilter);
                    if (selectJson != null && selectJson.length == 1)
                    {
                        File selectedJson = selectJson[0];
                        ObjectMapper mapper = new ObjectMapper();
                        ReportConfig reportConfig = mapper.readValue(selectedJson, ReportConfig.class);
                        reportConfig.setReportId(report.getName());
                        result.getReports().put(report.getName(), reportConfig);
                        continue;
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
}
