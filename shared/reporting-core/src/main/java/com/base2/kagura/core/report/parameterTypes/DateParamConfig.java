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
