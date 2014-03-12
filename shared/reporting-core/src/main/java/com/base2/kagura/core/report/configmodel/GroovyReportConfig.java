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
package com.base2.kagura.core.report.configmodel;

import com.base2.kagura.core.report.connectors.GroovyDataReportConnector;
import com.base2.kagura.core.report.connectors.ReportConnector;

/**
 * Allows generation of reports from a groovy script. @see #getGroovy()
 * User: aubels
 * Date: 24/07/13
 * Time: 4:46 PM
 *
 */
public class GroovyReportConfig extends ReportConfig {
    String groovy;

    /**
     * Groovey script to run. Values provided to groovy:
     List<Map<String, Object>> rows
     List<ColumnDef> columns
     Integer page
     Integer pageLimit
     List<ParamConfig> params
     Map<String, Object> extra

     To use this, keep in mind the page and pageLimit, refer to the parameters and insert new values into rows.
     * @return
     */
    public String getGroovy() {
        return groovy;
    }

    /**
     *
     * @see #getGroovy()
     */
    public void setGroovy(String groovy) {
        this.groovy = groovy;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ReportConnector getReportConnector() {
        return new GroovyDataReportConnector(this);
    }
}
