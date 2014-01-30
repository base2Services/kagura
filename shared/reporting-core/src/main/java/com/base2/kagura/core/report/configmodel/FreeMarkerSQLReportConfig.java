package com.base2.kagura.core.report.configmodel;

/**
 * Freemaker component which provides the freemarker, markup on the SQL queries. This is vital for parameter insertion
 * and customizable queries. Can be used to overcome a couple of SQL limitations.
 * User: aubels
 * Date: 24/07/13
 * Time: 4:46 PM
 *
 */
public abstract class FreeMarkerSQLReportConfig extends ReportConfig {
    protected String sql;
    protected String presqlsql;
    protected String postsqlsql;
    protected int queryTimeout = 10 * 60; // 10 minutes default query timeout.

    /**
     * The SQL that runs the query the report is expecting.
     * @return
     */
    public String getSql() {
        return sql;
    }

    /**
     * @see #getSql()
     */
    public void setSql(String sql) {
        this.sql = sql;
    }

    /**
     * A single statement to run before the request.
     * @return
     */
    public String getPresqlsql() {
        return presqlsql;
    }

    /**
     * @see #getPresqlsql()
     */
    public void setPresqlsql(String presqlsql) {
        this.presqlsql = presqlsql;
    }

    /**
     * A single sql statement to run after the request
     * @return
     */
    public String getPostsqlsql() {
        return postsqlsql;
    }

    /**
     * @see #getPostsqlsql()
     */
    public void setPostsqlsql(String postsqlsql) {
        this.postsqlsql = postsqlsql;
    }

    /**
     * Overwrite for the Query Timeout. Only set this if necessary.
     * @return
     */
    public int getQueryTimeout() {
        return queryTimeout;
    }

    /**
     * @see #getQueryTimeout()
     */
    public void setQueryTimeout(int queryTimeout) {
        this.queryTimeout = queryTimeout;
    }
}
