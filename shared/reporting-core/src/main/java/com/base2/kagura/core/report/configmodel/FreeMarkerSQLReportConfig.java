package com.base2.kagura.core.report.configmodel;

/**
 * Created with IntelliJ IDEA.
 * User: aubels
 * Date: 24/07/13
 * Time: 4:46 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class FreeMarkerSQLReportConfig extends ReportConfig {
    String sql;

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }
}
