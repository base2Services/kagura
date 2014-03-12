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
package com.base2.kagura.core.report.parameterTypes.datasources;

import com.base2.kagura.core.report.configmodel.ReportConfig;
import com.base2.kagura.core.report.connectors.ReportConnector;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * This uses another report to populate the combo box values. Essentially you can use any option you like, just be
 * aware that only one column is used, and that requiring parameters will do nothing. It's recommended that you keep
 * the content very simple.
 * @author aubels
 *         Date: 17/12/2013
 */
public class SQL extends Source {
    private ReportConfig report;

    /**
     * Sets the report to be used. No getter is provided so the contents doesn't get serialized.
     * @param report
     */
    public void setReport(ReportConfig report) {
        this.report = report;
    }

    String selectedColumn;

    /**
     * Executes the report, selecting the first column OR the column designated to make up the parameters values.
     * {@inheritDoc}
     */
    @JsonIgnore
    @Override
    public Collection<Object> getValues() {
        ReportConnector reportConnector = report.getReportConnector();
        reportConnector.run(extra);
        if (reportConnector.getRows() == null) return new ArrayList<Object>();
        return CollectionUtils.collect(reportConnector.getRows(), new Transformer() {
            @Override
            public Object transform(Object input) {
                Map<String, Object> map = (Map<String, Object>) input;
                if (map.size() == 1)
                    return map.values().iterator().next();
                else
                    return map.get(selectedColumn);
            }
        });
    }

    /**
     * The Selected column, basically the name of the column to extract the parameter values and names from.
     * (No distinction atm.)
     * @param selectedColumn
     */
    public void setSelectedColumn(String selectedColumn) {
        this.selectedColumn = selectedColumn;
    }

    /**
     * Constructor.
     */
    public SQL() {
    }

    /**
     * Reference copying constructor.
     * @param report             The new report config
     * @param selectedColumn     The column you wish to extract the values and names from
     * */
    public SQL(ReportConfig report, String selectedColumn) {
        this.report = report;
        this.selectedColumn = selectedColumn;
    }
}
