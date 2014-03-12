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
 * A parameter with a date backing value. PropertyUtils from the Apache Commons Bean library is used to populate
 * this value. Date AND date time exist, as the type reported to Kagura.js are different. There is no other reason
 * at the moment. It could probably be parametrized as a "pattern"
 * @author aubels
 *         Date: 10/09/13
 */
public class DateParamConfig extends DateTimeParamConfig {
}
