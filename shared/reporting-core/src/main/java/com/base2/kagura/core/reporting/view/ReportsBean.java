package com.base2.kagura.core.reporting.view;

import com.base2.kagura.core.reporting.view.report.ReportConnectorFactorySingleton;
import com.base2.kagura.core.reporting.view.report.configmodel.ReportConfig;
import com.base2.kagura.core.reporting.view.report.configmodel.ReportsConfig;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: aubels
 * Date: 29/07/13
 * Time: 11:44 AM
 * To change this template use File | Settings | File Templates.
 */
//@Named
public class ReportsBean implements Serializable {
    public static final String DISPLAY_PRIORITY_KEY = "displayPriority";
    public static final String IMAGE_KEY = "image";
    //    @Inject
    private ReportConnectorFactorySingleton reportConnectorFactorySingleton;
    private List<String> errors;

    public ReportsBean() {
        errors = new ArrayList<String>();
    }

    Collection<ReportConfig> configCollection = null;

    public Collection<ReportConfig> getHeaderReports()
    {
        getConfigCollection();
        ArrayList<ReportConfig> configList = (ArrayList<ReportConfig>) CollectionUtils.select(new ArrayList<ReportConfig>(configCollection), new Predicate() {
            @Override
            public boolean evaluate(Object object) {
                ReportConfig reportConfig = (ReportConfig)object;
                if (reportConfig == null) return false;
                try
                {
                    String displayPriorityStr = reportConfig.getExtraOptions().get(DISPLAY_PRIORITY_KEY);
                    if (displayPriorityStr == null) return false;
                    Integer displayPriority = Integer.valueOf(displayPriorityStr);
                    return displayPriority > 0 && StringUtils.isNotBlank(reportConfig.getExtraOptions().get(IMAGE_KEY));
                } catch (Exception e)
                {
                    errors.add("Report " + reportConfig.getReportId() + " raised error " + e.toString());
                    return false;
                }
            }
        });
        Collections.sort(configList, new ReportConfigComparator());
        return configList;
    }

    public Collection<ReportConfig> getIndexReports()
    {
        getConfigCollection();
        ArrayList<ReportConfig> configList = (ArrayList<ReportConfig>) CollectionUtils.select(new ArrayList<ReportConfig>(configCollection), new Predicate() {
            @Override
            public boolean evaluate(Object object) {
                ReportConfig reportConfig = (ReportConfig)object;
                if (reportConfig == null) return false;
                try
                {
                    String displayPriorityStr = reportConfig.getExtraOptions().get(DISPLAY_PRIORITY_KEY);
                    if (displayPriorityStr == null) return false;
                    Integer displayPriority = Integer.valueOf(displayPriorityStr);
                    return displayPriority > 0;
                } catch (Exception e)
                {
                    errors.add("Report " + reportConfig.getReportId() + " raised error " + e.toString());
                    return false;
                }
            }
        });
        Collections.sort(configList, new ReportConfigComparator());
        return configList;
    }

    private void getConfigCollection() {
        if (configCollection == null)
        {
            ReportsConfig reportsConfig = reportConnectorFactorySingleton.getReportsConfig();
            Map<String, ReportConfig> reports = reportsConfig.getReports();
            if (reports == null)
            {
                errors.add("Can not find reports.");
                return;
            }
            configCollection = reports.values();
            if (configCollection.size() == 0)
            {
                errors.add("Can not find reports.");
            }
            errors.addAll(reportsConfig.getErrors());
        }
    }

    public List<String> getErrors() {
        return errors;
    }

    private class ReportConfigComparator implements Comparator<ReportConfig> {
        @Override
        public int compare(ReportConfig reportConfig, ReportConfig reportConfig2) {
            try {
                Integer displayPriority = null;
                if (reportConfig != null) {
                    String displayPriorityStr = reportConfig.getExtraOptions().get(DISPLAY_PRIORITY_KEY);
                    displayPriority = displayPriorityStr != null ? Integer.valueOf(displayPriorityStr) : 0;
                }
                Integer displayPriority2 = null;
                if (reportConfig2 != null) {
                    String displayPriority2Str = reportConfig2.getExtraOptions().get(DISPLAY_PRIORITY_KEY);
                    displayPriority2 = displayPriority2Str != null ? Integer.valueOf(displayPriority2Str) : 0;
                }
                return displayPriority - displayPriority2;
            } catch (Exception e) {
                errors.add("Report " + reportConfig.getReportId() + " raised error " + e.toString());
                return 0;
            }
        }
    }
}
