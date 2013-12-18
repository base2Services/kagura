package com.base2.kagura.core.report.parameterTypes.datasources;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonRootName;
import groovy.lang.GroovyShell;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author aubels
 *         Date: 17/12/2013
 */
@JsonRootName("groovy")
public class Groovy extends Source {
    private String groovy;

    public String getGroovy() {
        return groovy;
    }

    public void setGroovy(String groovy) {
        this.groovy = groovy;
    }

    public Groovy() {
    }

    public Groovy(String groovy) {
        this.groovy = groovy;
    }

    @JsonIgnore
    @Override
    public Collection<Object> getValues() {
        List<Object> result = new ArrayList<Object>();
        GroovyShell groovyShell = new GroovyShell();
        groovyShell.setProperty("result", result);
        groovyShell.evaluate(groovy);
        return result;
    }

}
