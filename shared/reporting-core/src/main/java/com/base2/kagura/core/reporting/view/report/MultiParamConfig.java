package com.base2.kagura.core.reporting.view.report;

import java.util.Collection;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: aubels
 * Date: 17/07/13
 * Time: 2:55 PM
 * To change this template use File | Settings | File Templates.
 */
public class MultiParamConfig extends ParamConfig {
    public MultiParamConfig(String name, String type, Collection list) {
        super(name, type, list);
    }

    protected MultiParamConfig() {
    }

    List<String> value;

    public List<String> getValue() {
        return value;
    }

    public void setValue(List<String> value) {
        this.value = value;
    }
}
