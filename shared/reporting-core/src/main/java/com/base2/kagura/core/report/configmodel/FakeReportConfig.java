package com.base2.kagura.core.report.configmodel;

import com.base2.kagura.core.report.connectors.FakeDataReportConnector;
import com.base2.kagura.core.report.connectors.ReportConnector;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: aubels
 * Date: 24/07/13
 * Time: 4:46 PM
 * To change this template use File | Settings | File Templates.
 */
public class FakeReportConfig extends ReportConfig {
    List<Map<String, Object>> rows;
    Map<String, ParamToColumnRule> paramRules;

    @Override
    public ReportConnector getReportConnector() {
        return new FakeDataReportConnector(this);
    }

    public List<Map<String, Object>> getRows() {
        return rows;
    }

    public void setRows(List<Map<String, Object>> rows) {
        this.rows = rows;
    }

    public Map<String, ParamToColumnRule> getParamRules() {
        return paramRules;
    }

    public void setParamRules(Map<String, ParamToColumnRule> paramRules) {
        this.paramRules = paramRules;
    }

    public static class ParamToColumnRule {
        public enum MapRules {Exact, SubString, IntegerRange};
        String toColumn;
        MapRules mapRule;

        public String getToColumn() {
            return toColumn;
        }

        public void setToColumn(String toColumn) {
            this.toColumn = toColumn;
        }

        public MapRules getMapRule() {
            return mapRule;
        }

        public void setMapRule(MapRules mapRule) {
            this.mapRule = mapRule;
        }
    }
}
