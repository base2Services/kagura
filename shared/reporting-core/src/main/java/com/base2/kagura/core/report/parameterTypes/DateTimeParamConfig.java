package com.base2.kagura.core.report.parameterTypes;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: aubels
 * Date: 17/07/13
 * Time: 3:44 PM
 * To change this template use File | Settings | File Templates.
 */
public class DateTimeParamConfig extends ParamConfig {
    public DateTimeParamConfig(String name, String type, String help, String placeholder) {
        super(name, type, help, placeholder);
    }

    protected DateTimeParamConfig() {
    }

    private Date value;

    public Date getValue() {
        return value;
    }

    public void setValue(Date value) {
        this.value = value;
    }
}
