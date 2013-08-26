package com.base2.kagura.core.reporting.view.report.configmodel;

import com.base2.kagura.core.reporting.view.report.ColumnDef;
import com.base2.kagura.core.reporting.view.report.ParamConfig;
import com.base2.kagura.core.reporting.view.report.connectors.ReportConnector;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.List;

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
        @JsonSubTypes.Type(value = FakeReportConfig.class, name = "Fake")
})
public abstract class ReportConfig {
    String reportName;
    String reportId;
    List<ParamConfig> paramConfig;
    List<ColumnDef> columns;
    int diplayPriority;
    String image;
    private String description;

    public List<ParamConfig> getParamConfig() {
        return paramConfig;
    }

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

    public String getReportName() {
        return reportName;
    }

    public void setReportName(String reportName) {
        this.reportName = reportName;
    }

    public int getDiplayPriority() {
        return diplayPriority;
    }

    public void setDiplayPriority(int diplayPriority) {
        this.diplayPriority = diplayPriority;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @JsonIgnore
    abstract public ReportConnector getReportConnector();

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
