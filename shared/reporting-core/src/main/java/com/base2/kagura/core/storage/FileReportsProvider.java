package com.base2.kagura.core.storage;

import com.base2.kagura.core.report.configmodel.ReportsConfig;
import com.google.common.io.PatternFilenameFilter;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;

/**
 * @author aubels
 *         Date: 15/10/13
 */
public class FileReportsProvider extends ReportsProvider<File> {
    String reportDirectory;

    /**
     * Constructs a file Report Provider. This is the first thing you call to get a File Based reports going.
     * @param reportDirectory The directory to load the reports from. Directory listing must be possible.
     */
    public FileReportsProvider(String reportDirectory) {
        this.reportDirectory = reportDirectory;
    }

    /**
     * The directory used to find reports.
     * @return
     */
    public String getReportDirectory() {
        return reportDirectory;
    }

    /**
     * Dynamically change the directory used to find reports. This is possible.
     * @param reportDirectory
     */
    public void setReportDirectory(String reportDirectory) {
        this.reportDirectory = reportDirectory;
    }

    /** {@inheritDoc} */
    @Override
    protected String loadReport(ReportsConfig result, File report) throws Exception {
        final String reportName = report.getName();
        loadReport(result, report, reportName);
        return reportName;
    }

    /**
     * Custom separate dload report component, so it can be called elsewhere, or overwritten by child Providers. Checks
     * the "report" to ensure it is a directory then looks for reportconf.yaml or reportconf.json inside the file. If it
     * exists loads it.
     * @param result The collection of reports to load the contents into.
     * @param report The directory that contains the report files.
     * @param reportId The report id
     * @throws IOException
     */
    protected void loadReport(ReportsConfig result, File report, String reportId) throws IOException {
        if (report.isDirectory())
        {
            FilenameFilter configYamlFilter = new PatternFilenameFilter("^reportconf.(yaml|json)$");
            File[] selectYaml = report.listFiles(configYamlFilter);
            if (selectYaml != null && selectYaml.length == 1)
            {
                File selectedYaml = selectYaml[0];
                loadReport(result, FileUtils.openInputStream(selectedYaml), reportId);
            }
        }
    }

    /** {@inheritDoc} */
    @Override
    protected File[] getReportList() {
        File file = new File(reportDirectory);
        if (!file.exists())
           file = new File(FileReportsProvider.class.getResource(reportDirectory).getFile());
        if (!file.exists()) {
            errors.add("Couldn't open report directory, doesn't exist.");
            return null;
        }
        return file.listFiles();
    }

    /** {@inheritDoc} */
    @Override
    protected String reportToName(File report) {
        if (report == null) return "<null>";
        return report.getName();
    }
}
