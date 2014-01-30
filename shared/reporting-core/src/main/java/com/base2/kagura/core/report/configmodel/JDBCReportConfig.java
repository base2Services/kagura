package com.base2.kagura.core.report.configmodel;

import com.base2.kagura.core.report.connectors.JDBCDataReportConnector;
import com.base2.kagura.core.report.connectors.ReportConnector;

/**
 * JDBC backend for @see #FreeMarkerSQLReport
 * User: aubels
 * Date: 24/07/13
 * Time: 4:46 PM
 *
 */
public class JDBCReportConfig extends FreeMarkerSQLReportConfig {
    String jdbc;
    String username;
    String password;
    private String classLoaderPath;

    /**
     * {@inheritDoc}
     * @return
     */
    @Override
    public ReportConnector getReportConnector() {
        return new JDBCDataReportConnector(this);
    }

    /**
     * JDBC connection string
     * @return
     */
    public String getJdbc() {
        return jdbc;
    }

    /**
     * @see #getJdbc()
     */
    public void setJdbc(String jdbc) {
        this.jdbc = jdbc;
    }

    /**
     * JDBC Password if necessary
     * @return
     */
    public String getPassword() {
        return password;
    }

    /**
     * @see #getPassword()
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * JDBC username
     * @return
     */
    public String getUsername() {
        return username;
    }

    /**
     * @see #getUsername()
     * @param username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Class path loader for the Database driver, for instance: com.mysql.jdbc.Driver
     * @return
     */
    public String getClassLoaderPath() {
        return classLoaderPath;
    }

    /**
     * @see #getClassLoaderPath()
     */
    public void setClassLoaderPath(String classLoaderPath) {
        this.classLoaderPath = classLoaderPath;
    }
}
