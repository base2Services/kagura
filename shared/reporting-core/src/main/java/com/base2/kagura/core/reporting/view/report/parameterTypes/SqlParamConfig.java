package com.base2.kagura.core.reporting.view.report.parameterTypes;

import com.base2.kagura.core.reporting.view.report.configmodel.ReportConfig;
import com.base2.kagura.core.reporting.view.report.connectors.ReportConnector;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import java.util.Collection;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: aubels
 * Date: 17/07/13
 * Time: 2:56 PM
 * To change this template use File | Settings | File Templates.
 */
public class SqlParamConfig extends SingleParamConfig {
    public SqlParamConfig(String name, String type, String help, String placeholder, ReportConfig reportConfig) {
        super(name, type, help, placeholder);
        this.reportConfig = reportConfig;
    }

    protected SqlParamConfig() {
    }

    public SqlParamConfig(String name, String type, Collection list, ReportConfig reportConfig) {
        super(name, type, list);
        this.reportConfig = reportConfig;
    }

    ReportConfig reportConfig;
    String selectedColumn;

    @Override
    public Collection<Object> getValues() {
        ReportConnector reportConnector = reportConfig.getReportConnector();
        reportConnector.run();
        return CollectionUtils.collect(reportConnector.getRows(), new Transformer() {
            @Override
            public Object transform(Object input) {
                Map<String, Object> map = (Map<String, Object>)input;
                if (map.size() == 1)
                    return map.values().iterator().next();
                else
                    return map.get(selectedColumn);
            }
        });
    }

    public ReportConfig getReportConfig() {
        return reportConfig;
    }

    public void setReportConfig(ReportConfig reportConfig) {
        this.reportConfig = reportConfig;
    }

    public String getSelectedColumn() {
        return selectedColumn;
    }

    public void setSelectedColumn(String selectedColumn) {
        this.selectedColumn = selectedColumn;
    }
}
