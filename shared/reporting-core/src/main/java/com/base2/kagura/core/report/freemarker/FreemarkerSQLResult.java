/*
   Copyright 2014 base2Services

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */
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
