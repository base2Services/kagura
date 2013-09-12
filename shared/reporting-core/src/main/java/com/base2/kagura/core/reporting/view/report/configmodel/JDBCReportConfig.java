package com.base2.kagura.core.reporting.view.report.configmodel;

import com.base2.kagura.core.reporting.view.report.connectors.JDBCDataReportConnector;
import com.base2.kagura.core.reporting.view.report.connectors.ReportConnector;

/**
 * Created with IntelliJ IDEA.
 * User: aubels
 * Date: 24/07/13
 * Time: 4:46 PM
 * To change this template use File | Settings | File Templates.
 */
public class JDBCReportConfig extends FreeMarkerSQLReportConfig {
    String jdbc;
    String username;
    String password;
    private String classLoaderPath;

    @Override
    public ReportConnector getReportConnector() {
        return new JDBCDataReportConnector(this);
    }

    public String getJdbc() {
        return jdbc;
    }

    public void setJdbc(String jdbc) {
        this.jdbc = jdbc;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getClassLoaderPath() {
        return classLoaderPath;
    }

    public void setClassLoaderPath(String classLoaderPath) {
        this.classLoaderPath = classLoaderPath;
    }
}
