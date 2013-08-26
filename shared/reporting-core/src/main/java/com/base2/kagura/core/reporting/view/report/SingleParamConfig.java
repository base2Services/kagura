package com.base2.kagura.core.reporting.view.report;

import java.util.Collection;

/**
 * Created with IntelliJ IDEA.
 * User: aubels
 * Date: 17/07/13
 * Time: 2:56 PM
 * To change this template use File | Settings | File Templates.
 */
public class SingleParamConfig extends ParamConfig {
    public SingleParamConfig(String name, String type, String help, String placeholder) {
        super(name, type, help, placeholder);
    }

    protected SingleParamConfig() {
    }

    private String value;

    public SingleParamConfig(String name, String type, Collection list) {
        super(name, type, list);
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
