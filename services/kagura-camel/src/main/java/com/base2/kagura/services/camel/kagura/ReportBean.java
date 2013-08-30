package com.base2.kagura.services.camel.kagura;

import com.base2.kagura.core.reporting.view.report.configmodel.ReportConfig;
import com.base2.kagura.core.reporting.view.report.configmodel.ReportsConfig;
import com.base2.kagura.core.reporting.view.report.connectors.ReportConnector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author aubels
 *         Date: 28/08/13
 */
@Service
public class ReportBean {
    private ServerBean serverBean;

    private ReportConnector getConnector(String name)
    {
        ReportsConfig reportsConfig = getReportsConfig();
        ReportConfig reportConfig = reportsConfig.getReports().get(name);
        if (reportConfig == null)
            return null;
        return reportConfig.getReportConnector();
    }

    private ReportsConfig getReportsConfig() {
        return ReportsConfig.getConfig(serverBean.getConfigPath());
    }

    public Map<String, Object> getReportDetails(String reportName) {
        Map<String, Object> result = new HashMap<String, Object>();
        ReportConfig reportConfig = getReportConfig(reportName);
        result.put("reportId", reportName);
        result.put("extra", reportConfig.getExtraOptions());
        return result;
    }

    private ReportConfig getReportConfig(String reportName) {
        ReportsConfig reportsConfig = getReportsConfig();
        ReportConfig reportConfig = reportsConfig.getReports().get(reportName);
        if (reportConfig == null)
            return null;
        return reportConfig;
    }

    @Autowired
    public void setServerBean(ServerBean serverBean) {
        this.serverBean = serverBean;
    }

    public ServerBean getServerBean() {
        return serverBean;
    }
}
