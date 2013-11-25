package com.base2.kagura.core.storage;

import com.base2.kagura.core.report.configmodel.ReportsConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

/**
 * @author aubels
 *         Date: 15/10/13
 */
public class SuperFileReportsProvider extends ReportsProvider<File> {
    String superReportFile;

    public SuperFileReportsProvider(String superReportFile) {
        this.superReportFile = superReportFile;
    }

    public String getSuperReportFile() {
        return superReportFile;
    }

    public void setSuperReportFile(String superReportFile) {
        this.superReportFile = superReportFile;
    }

    @Override
    protected String loadReport(ReportsConfig result, File report) throws Exception {
        return null;
    }

    @Override
    protected File[] getReportList() {
        return null;
    }

    @Override
    public ReportsConfig getReportsConfig(Collection<String> restrictToNamed) {
        return getReportsConfig();
    }
    @Override
    public ReportsConfig getReportsConfig() {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        ReportsConfig reportsConfig = null;
        try {
            reportsConfig = mapper.readValue(new File(superReportFile), ReportsConfig.class);
        } catch (IOException e) {
            e.printStackTrace();
            errors.add("Error parsing super report " + superReportFile);
        }
        return reportsConfig;

    }

    @Override
    protected String reportToName(File report) {
        return null;
    }
}
