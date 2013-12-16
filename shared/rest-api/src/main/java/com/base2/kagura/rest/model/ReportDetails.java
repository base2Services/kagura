package com.base2.kagura.rest.model;

import com.base2.kagura.core.report.configmodel.parts.ColumnDef;
import com.base2.kagura.core.report.parameterTypes.ParamConfig;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

/**
 * @author aubels
 *         Date: 13/12/2013
 */
public class ReportDetails extends ResponseBase {
    private List<ParamConfig> params;
    private List<ColumnDef> columns;

    public ReportDetails() {
    }

    public ReportDetails(Map<String, Object> result) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        super(result);
    }

    public void setParams(List<ParamConfig> params) {
        this.params = params;
    }

    public List<ParamConfig> getParams() {
        return params;
    }

    public void setColumns(List<ColumnDef> columns) {
        this.columns = columns;
    }

    public List<ColumnDef> getColumns() {
        return columns;
    }
}
