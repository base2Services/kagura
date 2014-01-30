package com.base2.kagura.core.report.freemarker;

import java.util.List;

/**
 * Result from running a freemaker query. This contains the preparedStatement safe query, along with an array of
 * parameters to pass the preparedStatement.
 * User: aubels
 * Date: 29/07/13
 * Time: 9:15 AM
 * To change this template use File | Settings | File Templates.
 */
public class FreemarkerSQLResult {
    String sql;
    List<Object> params;

    /**
     * Constructs, does a shallow copy of parameters.
     * @param sql
     * @param params
     */
    public FreemarkerSQLResult(String sql, List<Object> params) {
        this.sql = sql;
        this.params = params;
    }

    /**
     * Resulting sql
     * @return sql
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
     * List of parameters to pass preparedStatement in order.
     * @return list
     */
    public List<Object> getParams() {
        return params;
    }

    /**
     * @see #getParams()
     */
    public void setParams(List<Object> params) {
        this.params = params;
    }
}
