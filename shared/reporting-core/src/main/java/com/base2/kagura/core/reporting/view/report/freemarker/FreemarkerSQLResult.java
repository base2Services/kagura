package com.base2.kagura.core.reporting.view.report.freemarker;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: aubels
 * Date: 29/07/13
 * Time: 9:15 AM
 * To change this template use File | Settings | File Templates.
 */
public class FreemarkerSQLResult {
    String sql;
    List<Object> params;

    public FreemarkerSQLResult(String sql, List<Object> params) {
        this.sql = sql;
        this.params = params;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public List<Object> getParams() {
        return params;
    }

    public void setParams(List<Object> params) {
        this.params = params;
    }
}
