package com.base2.kagura.core.report.configmodel;

import com.base2.kagura.core.report.configmodel.parts.ColumnDef;
import com.base2.kagura.core.report.parameterTypes.ParamConfig;
import com.base2.kagura.core.report.connectors.ReportConnector;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.List;
import java.util.Map;

/**
 * The base report configuration. Some of the fields are GIFO for the purpose of Kagura.js. some fields, particularly
 * on in child classes are required for the generation of the report. All these fields are designed to be deserialized
 * from a JSON file, then safely serialized again into a JSON message for REST communication with Kagura.js. When
 * creating new reports backends please bare this in mind.
 * User: aubels
 * Date: 24/07/13
 * Time: 4:46 PM
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

    /**
     * The parameter configuration.
     * @return
     */
    public List<ParamConfig> getParamConfig() {
        return paramConfig;
    }

    /**
     * Provides the report connector for the engine. The driver of the report. Use this to execute the report.
     * @return
     */
    @JsonIgnore
    abstract public ReportConnector getReportConnector();

    /**
     * @see #getParamConfig()
     */
    public void setParamConfig(List<ParamConfig> paramConfig) {
        this.paramConfig = paramConfig;
    }

    /**
     * The report identifier, namely the containing directory if using the @see FileConfigProvider
     * @return
     */
    @JsonIgnore
    public java.lang.String getReportId() {
        return reportId;
    }

    /**
     * @see #getReportId()
     */
    public void setReportId(String reportId) {
        this.reportId = reportId;
    }

    /**
     * Get the column configuration of the report. This is GIGO from the report configuration.
     * @return
     */
    public List<ColumnDef> getColumns() {
        return columns;
    }

    /**
     * @see #getColumns()
     */
    public void setColumns(List<ColumnDef> columns) {
        this.columns = columns;
    }

    /**
     * Extra options specific to the report, designed to be passed to kagura.js.
     * This is GIGO from the report configuration.
     * @return
     */
    public Map<String, String> getExtraOptions() {
        return extraOptions;
    }

    /**
     * @see #getExtraOptions()
     */
    public void setExtraOptions(Map<String, String> extraOptions) {
        this.extraOptions = extraOptions;
    }

    /**
     * Overwrites the page limit in the report. This defaults to 10 or 20.
     * @return
     */
    public Integer getPageLimit() {
        return pageLimit;
    }

    /**
     * @see #getPageLimit()
     */
    public void setPageLimit(Integer pageLimit) {
        this.pageLimit = pageLimit;
    }

    /**
     * Prepares the parameters for the report. This populates the Combo and ManyCombo box options if there is a groovy
     * or report backing the data.
     * @param extra Extra options from the middleware, this is passed down to the report or groovy execution.
     */
    public void prepareParameters(Map<String, Object> extra) {
        if (paramConfig == null) return;
        for (ParamConfig param : paramConfig)
        {
            param.prepareParameter(extra);
        }
    }
}
