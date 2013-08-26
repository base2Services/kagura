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
                return reportConfig.getDiplayPriority() > 0 && StringUtils.isNotBlank(reportConfig.getImage());
            }
        });
        Collections.sort(configList, new Comparator<ReportConfig>() {
            @Override
            public int compare(ReportConfig reportConfig, ReportConfig reportConfig2) {
                return (reportConfig != null ? reportConfig.getDiplayPriority() : 0) - (reportConfig2 != null ? reportConfig2.getDiplayPriority() : 0);
            }
        });
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
                return reportConfig.getDiplayPriority() > 0 && StringUtils.isBlank(reportConfig.getImage());
            }
        });
        Collections.sort(configList, new Comparator<ReportConfig>() {
            @Override
            public int compare(ReportConfig reportConfig, ReportConfig reportConfig2) {
                return (reportConfig != null ? reportConfig.getDiplayPriority() : 0) - (reportConfig2 != null ? reportConfig2.getDiplayPriority() : 0);
            }
        });
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
}
