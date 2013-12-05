package com.base2.example.war.rest;

import com.base2.kagura.core.report.configmodel.ReportConfig;
import com.base2.kagura.core.report.configmodel.ReportsConfig;
import com.base2.kagura.core.report.connectors.ReportConnector;
import com.base2.kagura.core.report.parameterTypes.ParamConfig;
import com.base2.kagura.core.storage.FileReportsProvider;
import com.base2.kagura.core.storage.ReportsProvider;
import com.base2.kagura.rest.model.Parameters;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConversionException;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.DateConverter;
import org.apache.commons.beanutils.converters.DateTimeConverter;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import javax.ws.rs.core.Response;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * @author aubels
 *         Date: 14/11/2013
 */
@Named
@SessionScoped
public class KaguraBean implements Serializable {
    private static final Logger LOG = LoggerFactory.getLogger(KaguraBean.class);
    static final int EXPORT_PAGE_LIMIT = 10000;
    ReportsProvider<?> reportsProvider;

    {{
        DateTimeConverter dtConverter = new DateConverter(null);
        dtConverter.setPatterns(new String[] {"yyyy-MM-dd", "yyyy-MM-dd hh:mm:ss"});
        ConvertUtils.register(dtConverter, Date.class);
    }}

    public static <T> Response makeResponse(T input) {
        try {
//            return Response.ok(new GenericEntity<T>(input){}).build();
            return Response.ok(new ObjectMapper().writeValueAsString(input)).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.serverError().build();
        }
    }

    @PostConstruct
    void init()
    {
        reportsProvider = new FileReportsProvider("/opt/base2/rio/rio-releases/current/reports");
    }

    public ArrayList<String> getReportList() {
        return new ArrayList<String>()
        {{
                add("Assets");
                add("Meters");
                add("RIOAllAssetsExports");
                add("RIOAllMetersAndRecordingsExceptInitialLoad");
                add("ScheduleSummary");
            }};
    }


    public Map<String, Object> noSuchReport(String reportName) {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("reportId", reportName);
        result.put("error", "No such report");
        return result;
    }

    public Map<String, Object> getReportDetails(String reportName, boolean full) {
            Map<String, Object> result = new HashMap<String, Object>();
            ReportConfig reportConfig = getReportConfig(reportName, result);
            if (reportConfig != null)
            {
                result.put("reportId", reportName);
                result.put("extra", reportConfig.getExtraOptions());
                if (full)
                {
                    result.put("params", reportConfig.getParamConfig());
                    result.put("columns", reportConfig.getColumns());
                }
            }
            else
            {
                result = noSuchReport(reportName);
            }
            return result;
    }

    public ReportConfig getReportConfig(String reportName, Map<String, Object> result) {
        ReportsConfig reportsConfig = getReportsConfig();
        ReportConfig reportConfig = reportsConfig.getReports().get(reportName);
        if (reportConfig == null)
            return null;
        if (reportsConfig.getErrors() != null)
            result.put("errors", reportsConfig.getErrors());
        return reportConfig;
    }

    public ReportsConfig getReportsConfig() {
        final ReportsConfig reportsConfig = reportsProvider.getReportsConfig();
        if (reportsProvider.getErrors() != null)
            for (String e : reportsProvider.getErrors())
                LOG.info("Report error: {}", e);
        return reportsConfig;
    }

    public ReportConnector getConnector(String reportId) {
        final ReportConfig reportConfig = getReportsConfig().getReports().get(reportId);
        if (reportConfig != null)
            return reportConfig.getReportConnector();
        return null;
    }

    public Map<String, Object> generateExtraRunOptions() {
        return new HashMap<String, Object>()
        {{

        }};
    }

    public void insertParameters(Parameters parameters, ReportConnector reportConnector, List<String> errors) {
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
}
