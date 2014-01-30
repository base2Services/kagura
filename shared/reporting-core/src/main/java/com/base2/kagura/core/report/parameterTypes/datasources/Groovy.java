package com.base2.kagura.core.report.parameterTypes.datasources;

import com.fasterxml.jackson.annotation.JsonIgnore;
import groovy.lang.GroovyShell;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * This allows groovy values to be used in a combo or multicombo box, usage is simple. you have 2 "properties" provided
 * to the script:
 * 1. "result" which is a List of Objects. Put what you want to show in the users parameters into that.
 * 2. Extra options. Provided from the middleware, inserted when ReportConnector.prepareParameters() is run.
 * @author aubels
 *         Date: 17/12/2013
 */
public class Groovy extends Source {
    private String groovy;

    /**
     * Setter only for groovy script. This is so when sending the values to the user they do not get the original
     * groovy script for producing the options.
     * @param groovy
     */
    public void setGroovy(String groovy) {
        this.groovy = groovy;
    }

    /**
     * Empty constructor
     */
    public Groovy() {
    }

    /**
     * Constructor which takes a single string as input, this allows Jackson to pass this class the entire remainder
     * of entry for this listing, and allows the configuration to become more simple. (One less indention.)
     * @param groovy
     */
    public Groovy(String groovy) {
        this.groovy = groovy;
    }

    /**
     * Runs the groovy and returns the values when serialized.
     * {@inheritDoc}
     */
    @JsonIgnore
    @Override
    public Collection<Object> getValues() {
        List<Object> result = new ArrayList<Object>();
        GroovyShell groovyShell = new GroovyShell();
        groovyShell.setProperty("result", result);
        groovyShell.setProperty("extra", extra);
        groovyShell.evaluate(groovy);
        return result;
    }

}
