/*
   Copyright 2014 base2Services

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */
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
