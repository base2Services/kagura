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
package com.base2.kagura.core.report.parameterTypes;

/**
 * A parameter with a boolean backing value. PropertyUtils from the Apache Commons Bean library is used to populate
 * this value.
 * User: aubels
 * Date: 17/07/13
 * Time: 3:44 PM
 */
public class BooleanParamConfig extends ParamConfig {
    /**
     * Constructor with lots of helpers.
     * {@inheritDoc}
     */
    public BooleanParamConfig(String name, String type, String help, String placeholder) {
        super(name, type, help, placeholder);
    }

    /**
     * Protected constructor for deserialization
     */
    protected BooleanParamConfig() {
    }

    private Boolean value;

    /**
     * Gets the value.
     * @return
     */
    public Boolean getValue() {
        return value;
    }

    /**
     * Sets the value
     * @param value
     */
    public void setValue(Boolean value) {
        this.value = value;
    }
}
