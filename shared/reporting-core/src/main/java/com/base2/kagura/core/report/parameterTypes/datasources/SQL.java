package com.base2.kagura.core.report.parameterTypes.datasources;

import com.base2.kagura.core.report.configmodel.ReportConfig;
import com.base2.kagura.core.report.connectors.ReportConnector;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonRootName;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author aubels
 *         Date: 17/12/2013
 */
@JsonRootName("sql")
public class SQL extends Source {
    private ReportConfig report;

    public void setReport(ReportConfig report) {
        this.report = report;
    }

    String selectedColumn;

    @JsonIgnore
    @Override
    public Collection<Object> getValues() {
        ReportConnector reportConnector = report.getReportConnector();
        reportConnector.run(new HashMap<String, Object>());
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

    public void setSelectedColumn(String selectedColumn) {
        this.selectedColumn = selectedColumn;
    }

    public SQL() {
    }

    public SQL(ReportConfig report, String selectedColumn) {

        this.report = report;
        this.selectedColumn = selectedColumn;
    }
}
