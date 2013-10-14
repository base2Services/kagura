package com.base2.kagura.core.report;

import com.base2.kagura.core.report.configmodel.ReportConfig;
import com.base2.kagura.core.report.configmodel.ReportsConfig;
import com.base2.kagura.core.report.connectors.ReportConnector;

//import javax.inject.Inject;
//import javax.inject.Named;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URISyntaxException;

/**
 * Created with IntelliJ IDEA.
 * User: aubels
 * Date: 24/07/13
 * Time: 4:41 PM
 * To change this template use File | Settings | File Templates.
 */
//@Named
public class ReportConnectorFactorySingleton implements Serializable {

    java.lang.String report_directory;

    public ReportConnector getConnector(String name)
    {
        ReportsConfig reportsConfig = getReportsConfig();
        ReportConfig reportConfig = reportsConfig.getReports().get(name);
        if (reportConfig == null)
            return null;
        return reportConfig.getReportConnector();
    }

    public ReportsConfig getReportsConfig() {
        try {
            return ReportsConfig.getConfig(report_directory);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
