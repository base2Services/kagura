package com.base2.kagura.core.reporting.view.report.configmodel;

import com.base2.kagura.core.reporting.view.report.connectors.GroovyDataReportConnector;
import com.base2.kagura.core.reporting.view.report.connectors.ReportConnector;

/**
 * Created with IntelliJ IDEA.
 * User: aubels
 * Date: 24/07/13
 * Time: 4:46 PM
 * To change this template use File | Settings | File Templates.
 */
public class GroovyReportConfig extends ReportConfig {
    String groovy;

    public String getGroovy() {
        return groovy;
    }

    public void setGroovy(String groovy) {
        this.groovy = groovy;
    }

    @Override
    public ReportConnector getReportConnector() {
        return new GroovyDataReportConnector(this);
    }
}
