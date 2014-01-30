package com.base2.kagura.core.report.configmodel;

import com.base2.kagura.core.report.connectors.JNDIDataReportConnector;
import com.base2.kagura.core.report.connectors.ReportConnector;

/**
 * JNDI backend for @see #FreeMarkerSQLReport
 * User: aubels
 * Date: 24/07/13
 * Time: 4:46 PM
 */
public class JNDIReportConfig extends FreeMarkerSQLReportConfig {
    String jndi;

    /**
     * {@inheritDoc}
     */
    @Override
    public ReportConnector getReportConnector() {
        return new JNDIDataReportConnector(this);
    }

    /**
     * JNDI URL.
     * @return
     */
    public String getJndi() {
        return jndi;
    }

    /**
     * @see #getJndi()
     */
    public void setJndi(String jndi) {
        this.jndi = jndi;
    }
}
