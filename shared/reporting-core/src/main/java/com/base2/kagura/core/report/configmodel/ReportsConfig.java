package com.base2.kagura.core.report.configmodel;

import com.base2.kagura.core.storage.AbstractReports;
import com.google.common.io.PatternFilenameFilter;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FilenameFilter;
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

    public ReportsConfig()
    {
        reports = new HashMap<String, ReportConfig>();
        errors = new ArrayList<String>();
    }

}
