package com.base2.kagura.core.reporting.view.report.configmodel;

import com.base2.kagura.core.reporting.view.report.connectors.JNDIDataReportConnector;
import com.base2.kagura.core.reporting.view.report.connectors.ReportConnector;

/**
 * Created with IntelliJ IDEA.
 * User: aubels
 * Date: 24/07/13
 * Time: 4:46 PM
 * To change this template use File | Settings | File Templates.
 */
public class JNDIReportConfig extends FreeMarkerSQLReportConfig {
    String jndi;

    @Override
    public ReportConnector getReportConnector() {
        return new JNDIDataReportConnector(this);
    }

    public String getJndi() {
        return jndi;
    }

    public void setJndi(String jndi) {
        this.jndi = jndi;
    }
}
