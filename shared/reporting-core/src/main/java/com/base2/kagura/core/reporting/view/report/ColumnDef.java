package com.base2.kagura.core.reporting.view.report;

/**
 * Created with IntelliJ IDEA.
 * User: aubels
 * Date: 16/07/13
 * Time: 5:49 PM
 * To change this template use File | Settings | File Templates.
 */
public class ColumnDef {
    private String name;
    private String styleType;

    public ColumnDef() {
    }

    public ColumnDef(String name, String styleType) {
        this.name = name;
        this.styleType = styleType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStyleType() {
        return styleType;
    }

    public void setStyleType(String styleType) {
        this.styleType = styleType;
    }

    public static ColumnDef Number(String name) {
        return new ColumnDef(name, "numbers");
    }

    public static ColumnDef Text(String name) {
        return new ColumnDef(name, "text");
    }
}
