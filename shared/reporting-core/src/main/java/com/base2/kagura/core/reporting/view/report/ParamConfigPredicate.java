package com.base2.kagura.core.reporting.view.report;

import org.apache.commons.collections.Predicate;

/**
 * Created with IntelliJ IDEA.
 * User: aubels
 * Date: 22/07/13
 * Time: 12:25 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class ParamConfigPredicate implements Predicate {
    protected ParamConfig paramConfig;

    protected ParamConfigPredicate(ParamConfig paramConfig) {
        this.paramConfig = paramConfig;
    }

    public ParamConfig getParamConfig() {
        return paramConfig;
    }

    public void setParamConfig(ParamConfig paramConfig) {
        this.paramConfig = paramConfig;
    }
}
