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
package com.base2.kagura.rest.helpers;

import com.base2.kagura.core.report.connectors.ReportConnector;
import com.base2.kagura.core.report.parameterTypes.ParamConfig;
import com.base2.kagura.rest.model.Parameters;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConversionException;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.DateConverter;
import org.apache.commons.beanutils.converters.DateTimeConverter;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.List;

/**
 * @author aubels
 *         Date: 17/01/2014
 */
public class ParameterUtils {
    public static void insertParameters(Parameters parameters, ReportConnector reportConnector, List<String> errors) {
        if (reportConnector.getParameterConfig() != null)
        {
            for (ParamConfig paramConfig : reportConnector.getParameterConfig())
            {
                if (parameters.getParameters().containsKey(paramConfig.getId()))
                {
                    Object o = parameters.getParameters().get(paramConfig.getId());
                    try {
                        if (o != null && StringUtils.isNotBlank(o.toString()))
                            BeanUtils.setProperty(paramConfig, "value", o);
                        else
                            BeanUtils.setProperty(paramConfig, "value", null);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    } catch (ConversionException e){
                        e.printStackTrace();
                        errors.add("Could not convert parameter: " + paramConfig.getId() + " value " + o);
                    }
                }
            }
        }
    }

    public static void SetupDateConverters() {
        DateTimeConverter dtConverter = new DateConverter(null);
        dtConverter.setPatterns(new String[] {"yyyy-MM-dd", "yyyy-MM-dd hh:mm:ss"});
        ConvertUtils.register(dtConverter, Date.class);
    }
}
