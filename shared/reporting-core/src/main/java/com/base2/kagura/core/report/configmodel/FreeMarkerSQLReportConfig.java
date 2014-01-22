package com.base2.kagura.core.report.configmodel;

/**
 * Created with IntelliJ IDEA.
 * User: aubels
 * Date: 24/07/13
 * Time: 4:46 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class FreeMarkerSQLReportConfig extends ReportConfig {
    protected String sql;
    protected String presqlsql;
    protected String postsqlsql;
    protected int queryTimeout = 10 * 60; // 10 minutes default query timeout.

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public String getPresqlsql() {
        return presqlsql;
    }

    public void setPresqlsql(String presqlsql) {
        this.presqlsql = presqlsql;
    }

    public String getPostsqlsql() {
        return postsqlsql;
    }

    public void setPostsqlsql(String postsqlsql) {
        this.postsqlsql = postsqlsql;
    }

    public int getQueryTimeout() {
        return queryTimeout;
    }

    public void setQueryTimeout(int queryTimeout) {
        this.queryTimeout = queryTimeout;
    }
}
