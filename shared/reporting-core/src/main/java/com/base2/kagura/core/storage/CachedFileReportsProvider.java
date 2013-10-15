package com.base2.kagura.core.storage;

import com.base2.kagura.core.report.configmodel.ReportConfig;
import com.base2.kagura.core.report.configmodel.ReportsConfig;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author aubels
 *         Date: 15/10/13
 */
public class CachedFileReportsProvider extends FileReportsProvider {
    public CachedFileReportsProvider(String reportDirectory) {
        super(reportDirectory);
    }

    Map<String, CachedReport> cache = new HashMap<String, CachedReport>();

    @Override
    protected void loadReport(ReportsConfig result, File report, String reportName) throws IOException {
        if (!cache.containsKey(reportName) || cache.get(reportName).getLastModified() != report.lastModified())
        {
            super.loadReport(result, report, reportName);
            cache.put(reportName,new CachedReport(result.getReports().get(reportName),report.lastModified()));
        }
        result.getReports().put(reportName, cache.get(reportName).getReportConfig());
    }

    class CachedReport
    {
        ReportConfig reportConfig;
        Long lastModified;

        CachedReport(ReportConfig reportConfig, Long lastModified) {
            this.reportConfig = reportConfig;
            this.lastModified = lastModified;
        }

        ReportConfig getReportConfig() {
            return reportConfig;
        }

        void setReportConfig(ReportConfig reportConfig) {
            this.reportConfig = reportConfig;
        }

        Long getLastModified() {
            return lastModified;
        }

        void setLastModified(Long lastModified) {
            this.lastModified = lastModified;
        }
    }
}
