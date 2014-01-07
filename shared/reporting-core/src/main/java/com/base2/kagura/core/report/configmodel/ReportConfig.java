package com.base2.kagura.core.report.configmodel;

import com.base2.kagura.core.report.configmodel.parts.ColumnDef;
import com.base2.kagura.core.report.parameterTypes.ParamConfig;
import com.base2.kagura.core.report.connectors.ReportConnector;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: aubels
 * Date: 24/07/13
 * Time: 4:46 PM
 * To change this template use File | Settings | File Templates.
 */
@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = JDBCReportConfig.class, name = "JDBC"),
        @JsonSubTypes.Type(value = JNDIReportConfig.class, name = "JNDI"),
        @JsonSubTypes.Type(value = GroovyReportConfig.class, name = "Groovy"),
        @JsonSubTypes.Type(value = FakeReportConfig.class, name = "Fake")
})
public abstract class ReportConfig {
    String reportId;
    List<ParamConfig> paramConfig;
    List<ColumnDef> columns;
    Map<String, String> extraOptions;
    private Integer pageLimit;

    public List<ParamConfig> getParamConfig() {
        return paramConfig;
    }

    @JsonIgnore
    abstract public ReportConnector getReportConnector();

    public void setParamConfig(List<ParamConfig> paramConfig) {
        this.paramConfig = paramConfig;
    }

    @JsonIgnore
    public java.lang.String getReportId() {
        return reportId;
    }

    public void setReportId(String reportId) {
        this.reportId = reportId;
    }

    public List<ColumnDef> getColumns() {
        return columns;
    }

    public void setColumns(List<ColumnDef> columns) {
        this.columns = columns;
    }

    public Map<String, String> getExtraOptions() {
        return extraOptions;
    }

    public void setExtraOptions(Map<String, String> extraOptions) {
        this.extraOptions = extraOptions;
    }

    public Integer getPageLimit() {
        return pageLimit;
    }

    public void setPageLimit(Integer pageLimit) {
        this.pageLimit = pageLimit;
    }

    public void prepareParameters(Map<String, Object> extra) {
        if (paramConfig == null) return;
        for (ParamConfig param : paramConfig)
        {
            param.prepareParameter(extra);
        }
    }
}
