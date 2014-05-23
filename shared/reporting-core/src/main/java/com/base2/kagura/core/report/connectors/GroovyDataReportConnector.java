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
package com.base2.kagura.core.report.connectors;

import com.base2.kagura.core.report.configmodel.GroovyReportConfig;
import com.base2.kagura.core.report.parameterTypes.ParamConfig;
import groovy.lang.GroovyShell;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Provides a groovy script based backend for reports. With this the script writer is required to manually construct
 * the result.
 * Values provided to groovy:
     List<Map<String, Object>> rows
     List<ColumnDef> columns
     Integer page
     Integer pageLimit
     List<ParamConfig> params
     Map<String, Object> extra

 To use this, keep in mind the page and pageLimit, refer to the parameters and insert new values into rows.

 * User: aubels
 * Date: 22/07/13
 * Time: 11:57 AM
 */
public class GroovyDataReportConnector extends ReportConnector {
    private List<Map<String, Object>> rows;
    private String groovyScript;

    /**
     * Constructor, shallow copies appropriate values.
     * @param reportConfig
     */
    public GroovyDataReportConnector(GroovyReportConfig reportConfig) {
        super(reportConfig);
        groovyScript = reportConfig.getGroovy();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void run(Map<String, Object> extra) {
        try
        {
            rows = new ArrayList<Map<String, Object>>();
            GroovyShell groovyShell = new GroovyShell();
            groovyShell.setProperty("rows", rows);
            groovyShell.setProperty("columns", getColumns());
            groovyShell.setProperty("page", getPage());
            groovyShell.setProperty("pageLimit", getPageLimit());
            groovyShell.setProperty("paramConfig", getParameterConfig());
            groovyShell.setProperty("param", new HashMap<String, ParamConfig>()
            {{
                for(ParamConfig paramConfig : getParameterConfig())
                {
                    put(paramConfig.getId(), paramConfig);
                }
            }});
            groovyShell.setProperty("extra", extra);
            groovyShell.evaluate(groovyScript);
        } catch (Exception ex)
        {
            ex.printStackTrace();
            getErrors().add(ex.getMessage());
        }
    }

    /**
     * Returns the rows retrieved.
     * @return
     */
    public List<Map<String, Object>> getRows() {
        return rows;
    }

    /**
     * @see #getRows()
     */
    public void setRows(List<Map<String, Object>> rows) {
        this.rows = rows;
    }

    /**
     * The groovy script used.
     * @return
     */
    public String getGroovyScript() {
        return groovyScript;
    }

    /**
     * @see #getGroovyScript()
     */
    public void setGroovyScript(String groovyScript) {
        this.groovyScript = groovyScript;
    }
}
