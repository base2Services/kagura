package com.base2.kagura.core.reporting.view.report.parameterTypes;

import groovy.lang.GroovyShell;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: aubels
 * Date: 17/07/13
 * Time: 2:56 PM
 * To change this template use File | Settings | File Templates.
 */
public class GroovyParamConfig extends SingleParamConfig {
    public GroovyParamConfig(String name, String type, String help, String placeholder, String groovy) {
        super(name, type, help, placeholder);
        this.groovy = groovy;
    }

    protected GroovyParamConfig() {
    }

    public GroovyParamConfig(String name, String type, Collection list, String groovy) {
        super(name, type, list);
        this.groovy = groovy;
    }

    String groovy;

    @Override
    public Collection<Object> getValues() {
        List<Object> result = new ArrayList<Object>();
        GroovyShell groovyShell = new GroovyShell();
        groovyShell.setProperty("result", result);
        groovyShell.evaluate(groovy);
        return result;
    }

    public String getGroovy() {
        return groovy;
    }

    public void setGroovy(String groovy) {
        this.groovy = groovy;
    }
}
