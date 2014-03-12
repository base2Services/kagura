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

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Cached file provider. This uses the Decorator pattern to allow reports to be cached in-case the backend is a slow
 * external one, such as S3, Git, or some *TP://. Currently only designed for file.
 *
 * For security you will need to create one per user, and I recommend you restrict it to sessions.
 *
 * @author aubels
 *         Date: 15/10/13
 */
public class CachedFileReportsProvider extends FileReportsProvider {
    public CachedFileReportsProvider(String reportDirectory) {
        super(reportDirectory);
    }

    /**
     * The cache itself.
     */
    Map<String, CachedReport> cache = new HashMap<String, CachedReport>();

    /**
     * {@inheritDoc}
     * @param result
     * @param report
     * @param reportId
     * @throws IOException
     */
    @Override
    protected void loadReport(ReportsConfig result, File report, String reportId) throws IOException {
        // Need to refresh? -- CahcedReport should be made abstract and the type should define which class to use
        // and what refresh checking method is used.
        if (!cache.containsKey(reportId) || cache.get(reportId).getLastModified() != report.lastModified())
        {
            super.loadReport(result, report, reportId);
            cache.put(reportId,new CachedReport(result.getReports().get(reportId),report.lastModified()));
        }
        result.getReports().put(reportId, cache.get(reportId).getReportConfig());
    }

    /**
     * Cached Report structure. Keeps tabs on last modified, and the actual report config
     */
    class CachedReport
    {
        ReportConfig reportConfig;
        Long lastModified;

        /**
         * Constructor.
         * @param reportConfig
         * @param lastModified
         */
        CachedReport(ReportConfig reportConfig, Long lastModified) {
            this.reportConfig = reportConfig;
            this.lastModified = lastModified;
        }

        /**
         * Storing the report config
         * @return
         */
        ReportConfig getReportConfig() {
            return reportConfig;
        }

        /**
         * @see #getReportsConfig()
         * @param reportConfig
         */
        void setReportConfig(ReportConfig reportConfig) {
            this.reportConfig = reportConfig;
        }

        /**
         * The last modified time of the file when loaded.
         * @return
         */
        Long getLastModified() {
            return lastModified;
        }

        /**
         * @see #getLastModified()
         * @param lastModified
         */
        void setLastModified(Long lastModified) {
            this.lastModified = lastModified;
        }
    }
}
