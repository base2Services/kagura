package com.base2.kagura.core.report.connectors;

import com.base2.kagura.core.report.configmodel.FakeReportConfig;
import com.base2.kagura.core.report.parameterTypes.ParamConfig;
import com.base2.kagura.core.report.parameterTypes.SingleParamConfig;
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
 * Fake Data Report connector. This provides the fake data from the configuration back to the middleware to pass on
 * where-ever it's due. It also has a rough filtering method it applies to the data if the user were to select the
 * appropriate parameters.
 * User: aubels
 * Date: 22/07/13
 * Time: 11:57 AM
 */
public class FakeDataReportConnector extends ReportConnector {
    private Map<String, FakeReportConfig.ParamToColumnRule> paramRules;
    private List<Map<String, Object>> rows;
    private List<Map<String, Object>> data;

    /**
     * Empty constructor, for unit tests. Do not use.
     * @param reportParams Unused object.
     */
    public FakeDataReportConnector(Object reportParams) {
        super(null);
    }

    /**
     * Constructs the fake data report connector. Initializes appropriate values.
     * @param reportConfig
     */
    public FakeDataReportConnector(FakeReportConfig reportConfig) {
        super(reportConfig);
        data = reportConfig.getRows();
        paramRules = reportConfig.getParamRules();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void run(Map<String, Object> extra) {
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
                        rows = (List<Map<String, Object>>) CollectionUtils.select(rows, new ExactEquals(paramToColumnRule, paramConfig));
                        break;
                    case SubString:
                        rows = (List<Map<String, Object>>) CollectionUtils.select(rows, new SubstringOrExact(paramToColumnRule, paramConfig));
                        break;
                    case IntegerRange:
                        rows = (List<Map<String, Object>>) CollectionUtils.select(rows, new IntegerRange(paramToColumnRule, paramConfig));
                        break;
                }
            }
        }
    }

    /**
     * Filtered rows, returned to middleware.
     * @return
     */
    public List<Map<String, Object>> getRows() {
        return rows;
    }

    /**
     * @see #getRows()
     */
    public void setRows(List<Map<String, Object>> rows) {
        this.rows = rows;
    }

    /**
     * Exact equals predicate class. This provides the parameter filtering rules, this one is a simple one that filters
     * the element if it does not exactly match the value provided in the parameter. Basically it:
     * keep only if: parameter.value IS EMPTY OR row[column] == parameter.value
     */
    private static class ExactEquals implements Predicate {
        protected final FakeReportConfig.ParamToColumnRule paramToColumnRule;
        protected final ParamConfig paramConfig;

        /**
         * Constructor, allows copying the values from parent into class context. Required for the filtering rules and
         * parameter configuration / selection
         * @param paramToColumnRule The filtering rule
         * @param paramConfig The appropriate parameter configuration + selection
         */
        public ExactEquals(FakeReportConfig.ParamToColumnRule paramToColumnRule, ParamConfig paramConfig) {
            this.paramToColumnRule = paramToColumnRule;
            this.paramConfig = paramConfig;
        }

        /**
         * Checks to ensure that the value directly matches. Silently ignores any errors counting those as a match
         * failure.
         * {@inheritDoc}
         */
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

    /**
     * Provides a CollectionUtils usable filtering mechanism that is exactly like @see #ExactEquals however will pass
     * a row if the column contents contains the selected parameter value.
     */
    private static class SubstringOrExact extends ExactEquals {
        public SubstringOrExact(FakeReportConfig.ParamToColumnRule paramToColumnRule, ParamConfig paramConfig) {
            super(paramToColumnRule, paramConfig);
        }

        /**
         * In the case of a Single Parameter value, checks to see if the column value contains the parameter selection
         * if it does the value is kept in the list.
         * {@inheritDoc}
         */
        @Override
        public boolean evaluate(Object object) {
            String column = ((Map<String, Object>)object).get(paramToColumnRule.getToColumn()).toString();
            if (paramConfig instanceof SingleParamConfig)
                return column.contains(((SingleParamConfig) paramConfig).getValue());
            return super.evaluate(object);
        }
    }

    /**
     * Just like it's parent @see #ExactEquals provides a CollectionUtils filtering predicate, this one however determines
     * if the value in the parameter is a single value, a range of values (x-y) or a "value or greater" (ie x+) then
     * prases the column value as an integer and checks to see if it matches the selected value. If the selected value
     * is null, everything passes. If the value fails to parse, the value is rejected. An example of where could be used
     * is when a user is picking age ranges.
     */
    private static class IntegerRange extends ExactEquals {
        public IntegerRange(FakeReportConfig.ParamToColumnRule paramToColumnRule, ParamConfig paramConfig) {
            super(paramToColumnRule, paramConfig);
        }

        /**
         * See class description as to how it evaluates.
         * {@inheritDoc}
         */
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
