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

import java.util.Collection;

/**
 * A combo / manycombo value list, which is a just a plain old list intialized by jackson / yaml.
 * @author aubels
 *         Date: 17/12/2013
 */
public class OptionList extends Source {
    private Collection<Object> values;

    /**
     * Returns the values..
     * @return a list of values
     */
    public Collection<Object> getValues() {
        return values;
    }

    /**
     * {@inheritDoc}
     * @param values
     */
    public void setValues(Collection<Object> values) {
        this.values = values;
    }

    /**
     * Constructor, with value / options.
     * @param values
     */
    public OptionList(Collection<Object> values) {
        this.values = values;
    }

    /**
     * Default constructor.
     */
    public OptionList() {

    }
    /**
     * Reduces parsing errors. Should be avoided. (Should be an error condition.)
     */
    public OptionList(String in) {

    }
}
