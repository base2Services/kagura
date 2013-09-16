package com.base2.kagura.core.reporting.view.report.connectors;

import com.base2.kagura.core.reporting.view.report.configmodel.FakeReportConfig;
import com.base2.kagura.core.reporting.view.report.parameterTypes.ParamConfig;
import com.base2.kagura.core.reporting.view.report.parameterTypes.SingleParamConfig;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
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
                try {
                    if (StringUtils.isBlank(BeanUtils.getProperty(paramConfig, "value"))) continue;
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                    continue;
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                    continue;
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                    continue;
                }
                switch (paramToColumnRule.getMapRule())
                {
                    case Exact:
                        rows = (List<Map<String, Object>>) CollectionUtils.select(rows, new exactEquals(paramToColumnRule, paramConfig));
                        break;
                    case SubString:
                        rows = (List<Map<String, Object>>) CollectionUtils.select(rows, new substringOrExact(paramToColumnRule, paramConfig));
                        break;
                    case IntegerRange:
                        rows = (List<Map<String, Object>>) CollectionUtils.select(rows, new IntegerRange(paramToColumnRule, paramConfig));
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

    private static class exactEquals implements Predicate {
        protected final FakeReportConfig.ParamToColumnRule paramToColumnRule;
        protected final ParamConfig paramConfig;

        public exactEquals(FakeReportConfig.ParamToColumnRule paramToColumnRule, ParamConfig paramConfig) {
            this.paramToColumnRule = paramToColumnRule;
            this.paramConfig = paramConfig;
        }

        @Override
        public boolean evaluate(Object object) {
            String column = ((Map<String, Object>)object).get(paramToColumnRule.getToColumn()).toString();
            try {
                return column.equals(BeanUtils.getProperty(paramConfig, "value"));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
            return false;
        }
    }

    private static class substringOrExact extends exactEquals {
        public substringOrExact(FakeReportConfig.ParamToColumnRule paramToColumnRule, ParamConfig paramConfig) {
            super(paramToColumnRule, paramConfig);
        }

        @Override
        public boolean evaluate(Object object) {
            String column = ((Map<String, Object>)object).get(paramToColumnRule.getToColumn()).toString();
            if (paramConfig instanceof SingleParamConfig)
                return column.contains(((SingleParamConfig) paramConfig).getValue());
            return super.evaluate(object);
        }
    }

    private static class IntegerRange extends exactEquals {
        public IntegerRange(FakeReportConfig.ParamToColumnRule paramToColumnRule, ParamConfig paramConfig) {
            super(paramToColumnRule, paramConfig);
        }

        @Override
        public boolean evaluate(Object object) {
            Pattern isRange = Pattern.compile("^(\\d+)-(\\d+)$");
            Pattern isOrMore = Pattern.compile("^(\\d+)\\+$");
            String column = ((Map<String, Object>)object).get(paramToColumnRule.getToColumn()).toString();
            if (paramConfig instanceof SingleParamConfig) {
                String paramRange = ((SingleParamConfig) paramConfig).getValue();
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
            return super.evaluate(object);
        }
    }
}
