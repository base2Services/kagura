package com.base2.kagura.core.reporting.view.report.connectors;

import com.base2.kagura.core.reporting.view.report.ColumnDef;
import com.base2.kagura.core.reporting.view.report.parameterTypes.ParamConfig;
import com.base2.kagura.core.reporting.view.report.configmodel.ReportConfig;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: aubels
 * Date: 22/07/13
 * Time: 11:57 AM
 * To change this template use File | Settings | File Templates.
 */
public abstract class ReportConnector implements Serializable {
    public abstract void run();
    Integer page = 0;
    Integer pageLimit = 20;
    List<ColumnDef> columns;
    List<ParamConfig> parameterConfig;
    protected List<String> errors;

    public abstract List<Map<String,Object>> getRows();

    protected ReportConnector(ReportConfig reportConfig) {
        if (reportConfig == null) return;
        this.columns = reportConfig.getColumns();
        this.parameterConfig = reportConfig.getParamConfig();
        this.errors = new ArrayList<String>();
        if (reportConfig.getPageLimit() != null)
        {
            this.pageLimit = reportConfig.getPageLimit();
        }
    }

    public List<String> getErrors() {
        return errors;
    }

    public void clearErrors()
    {
        errors = new ArrayList<String>();
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public List<ColumnDef> getColumns() {
        return columns;
    }

    public void setColumns(List<ColumnDef> columns) {
        this.columns = columns;
    }

    public List<ParamConfig> getParameterConfig() {
        return parameterConfig;
    }

    public void setParameterConfig(List<ParamConfig> parameterConfig) {
        this.parameterConfig = parameterConfig;
    }

    public Integer getPageLimit() {
        return pageLimit;
    }

    public void setPageLimit(Integer pageLimit) {
        this.pageLimit = pageLimit;
    }
}
