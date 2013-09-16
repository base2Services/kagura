package com.base2.kagura.core.reporting.view.report.parameterTypes;

/**
 * Created with IntelliJ IDEA.
 * User: aubels
 * Date: 17/07/13
 * Time: 3:44 PM
 * To change this template use File | Settings | File Templates.
 */
public class BooleanParamConfig extends ParamConfig {
    public BooleanParamConfig(String name, String type, String help, String placeholder) {
        super(name, type, help, placeholder);
    }

    protected BooleanParamConfig() {
    }

    private Boolean value;

    public Boolean getValue() {
        return value;
    }

    public void setValue(Boolean value) {
        this.value = value;
    }
}
