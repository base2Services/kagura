package com.base2.kagura.core.reporting.view.report.configmodel.parts;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: aubels
 * Date: 16/07/13
 * Time: 5:49 PM
 * To change this template use File | Settings | File Templates.
 */
public class ColumnDef {
    private String name;
    private String label;
    private Map<String, Object> extraOptions;

    public ColumnDef() {
    }

    public ColumnDef(String name, String styleType) {
        this.name = name;
        this.extraOptions = new HashMap<String, Object>();
        extraOptions.put("styleType", styleType);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static ColumnDef Number(String name) {
        return new ColumnDef(name, "numbers");
    }

    public static ColumnDef Text(String name) {
        return new ColumnDef(name, "text");
    }

    public String getLabel() {
        return StringUtils.defaultString(label,name);
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Map<String, Object> getExtraOptions() {
        return extraOptions;
    }

    public void setExtraOptions(Map<String, Object> extraOptions) {
        this.extraOptions = extraOptions;
    }
}
