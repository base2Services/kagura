package com.base2.kagura.core.reporting.view.report.connectors;

import com.base2.kagura.core.reporting.view.report.*;
import com.base2.kagura.core.reporting.view.report.configmodel.FakeReportConfig;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * User: aubels
 * Date: 22/07/13
 * Time: 11:57 AM
 * To change this template use File | Settings | File Templates.
 */
public class FakeDataReportConnector extends ReportConnector {
    private Map<String, FakeReportConfig.ParamToColumnRule> paramRules;
    private List<Map<String, Object>> rows;
    private List<Map<String, Object>> data;

    public FakeDataReportConnector(Object reportParams) {
        super(null);
    }

    public FakeDataReportConnector(FakeReportConfig reportConfig) {
        super(reportConfig);
        data = reportConfig.getRows();
        paramRules = reportConfig.getParamRules();
    }

    @Override
    public void run() {
        rows = new ArrayList<Map<String, Object>>(data != null ? data : new ArrayList<Map<String, Object>>());
        if (parameterConfig != null)
        {
            for (final ParamConfig paramConfig : parameterConfig)
            {
                if (paramRules == null) continue;
                final FakeReportConfig.ParamToColumnRule paramToColumnRule = paramRules.get(paramConfig.getName());
                if (paramToColumnRule == null) continue;
                if (paramConfig instanceof SingleParamConfig && StringUtils.isBlank(((SingleParamConfig) paramConfig).getValue())) continue;
                if (paramConfig instanceof MultiParamConfig && ((MultiParamConfig) paramConfig).getValue() == null) continue;
                if (paramConfig instanceof BooleanParamConfig && ((BooleanParamConfig) paramConfig).getValue() == null) continue;
                switch (paramToColumnRule.getMapRule())
                {
                    case Exact:
                        rows = (List<Map<String, Object>>) CollectionUtils.select(rows, new Predicate() {
                            @Override
                            public boolean evaluate(Object object) {
                                String column = ((Map<String, Object>)object).get(paramToColumnRule.getToColumn()).toString();
                                if (paramConfig instanceof SingleParamConfig)
                                    return column.equals(((SingleParamConfig) paramConfig).getValue());
                                if (paramConfig instanceof MultiParamConfig)
                                    return column.equals(((MultiParamConfig) paramConfig).getValue());
                                if (paramConfig instanceof BooleanParamConfig)
                                    return column.equals(((BooleanParamConfig) paramConfig).getValue());
                                return false;
                            }
                        });
                        break;
                    case SubString:
                        rows = (List<Map<String, Object>>) CollectionUtils.select(rows, new Predicate() {
                            @Override
                            public boolean evaluate(Object object) {
                                String column = ((Map<String, Object>)object).get(paramToColumnRule.getToColumn()).toString();
                                if (paramConfig instanceof SingleParamConfig)
                                    return column.contains(((SingleParamConfig) paramConfig).getValue());
                                if (paramConfig instanceof MultiParamConfig)
                                    return column.equals(((MultiParamConfig) paramConfig).getValue());
                                if (paramConfig instanceof BooleanParamConfig)
                                    return column.equals(((BooleanParamConfig) paramConfig).getValue());
                                return false;
                            }
                        });
                        break;
                    case IntegerRange:
                        rows = (List<Map<String, Object>>) CollectionUtils.select(rows, new Predicate() {
                            @Override
                            public boolean evaluate(Object object) {
                                Pattern isRange = Pattern.compile("^(\\d+)-(\\d+)$");
                                Pattern isOrMore = Pattern.compile("^(\\d+)\\+$");
                                String column = ((Map<String, Object>)object).get(paramToColumnRule.getToColumn()).toString();
                                if (paramConfig instanceof SingleParamConfig) {
                                    String paramRange = ((SingleParamConfig)paramConfig).getValue();
                                    try
                                    {
                                        Matcher isRangeMatch = isRange.matcher(paramRange);
                                        Matcher isOrMoreMatch = isOrMore.matcher(paramRange);
                                        if (isRangeMatch.find())
                                        {
                                            int low = Integer.parseInt(isRangeMatch.group(1));
                                            int high = Integer.parseInt(isRangeMatch.group(2));
                                            int value = Integer.parseInt(column);
                                            return low <= value && value <= high;
                                        }
                                        if (isOrMoreMatch.find())
                                        {
                                            int low = Integer.parseInt(isOrMoreMatch.group(1));
                                            int value = Integer.parseInt(column);
                                            return low <= value;
                                        }
                                    } catch (Exception ex)
                                    {
                                        ex.printStackTrace();
                                    }
                                }
                                if (paramConfig instanceof SingleParamConfig)
                                    return column.equals(((SingleParamConfig) paramConfig).getValue());
                                if (paramConfig instanceof MultiParamConfig)
                                    return column.equals(((MultiParamConfig) paramConfig).getValue());
                                if (paramConfig instanceof BooleanParamConfig)
                                    return column.equals(((BooleanParamConfig) paramConfig).getValue());
                                return false;
                            }
                        });
                        break;
                }
            }
        }
    }

    public List<Map<String, Object>> getRows() {
        return rows;
    }

    public void setRows(List<Map<String, Object>> rows) {
        this.rows = rows;
    }
}
