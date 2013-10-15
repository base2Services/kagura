package com.base2.kagura.core.storage;

import com.base2.kagura.core.report.configmodel.ReportsConfig;
import com.google.common.io.PatternFilenameFilter;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FilenameFilter;

/**
 * @author aubels
 *         Date: 15/10/13
 */
public class FileReports extends AbstractReports<File> {
    String report_directory;

    public FileReports(String report_directory) {
        this.report_directory = report_directory;
    }

    public String getReport_directory() {
        return report_directory;
    }

    public void setReport_directory(String report_directory) {
        this.report_directory = report_directory;
    }

    @Override
    protected String loadReport(ReportsConfig result, File report) throws Exception {
        {
            if (report.isDirectory())
            {
                FilenameFilter configYamlFilter = new PatternFilenameFilter("^reportconf.(yaml|json)$");
                File[] selectYaml = report.listFiles(configYamlFilter);
                if (selectYaml != null && selectYaml.length == 1)
                {
                    File selectedYaml = selectYaml[0];
                    if (loadReport(result, FileUtils.openInputStream(selectedYaml), report.getName()))
                        return report.getName();
                }
            }
            return report.getName();
        }
    }

    @Override
    protected File[] getReportList() {
        File file = new File(report_directory);
        if (!file.exists()) {
            errors.add("Couldn't open report directory, doesn't exist.");
            return null;
        }
        return file.listFiles();
    }
}
