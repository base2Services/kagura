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
package com.base2.kagura.core.report.configmodel;

import com.base2.kagura.core.report.connectors.FakeDataReportConnector;
import com.base2.kagura.core.report.connectors.ReportConnector;

import java.util.List;
import java.util.Map;

/**
 * Fake report. This is used for demonstration purposes. It has a basic filtering engine configured, all data is hard
 * coded. I would recommend using "groovy" for backendless demos.
 * User: aubels
 * Date: 24/07/13
 * Time: 4:46 PM
 */
public class FakeReportConfig extends ReportConfig {
    List<Map<String, Object>> rows;
    Map<String, ParamToColumnRule> paramRules; //*<

    /**
     * {@inheritDoc}
     */
    @Override
    public ReportConnector getReportConnector() {
        return new FakeDataReportConnector(this);
    }

    /**
     * Pre-populated rows. This is pure GIGO, set by configuration.
     * @return rows
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
     * Simple filtering rules, based on the parameters.
     * @return rules.
     */
    public Map<String, ParamToColumnRule> getParamRules() {
        return paramRules;
    }

    /**
     * @see #getParamRules()
     */
    public void setParamRules(Map<String, ParamToColumnRule> paramRules) {
        this.paramRules = paramRules;
    }

    /**
     * Simple Fake report parameter filtering rule configuration object. Values are stored in parameter configuration.
     */
    public static class ParamToColumnRule {
        /**
         * The Types of matches that can be preformed based on a parameter.
         * Exact: String or number has to match exactly
         * SubString: String is part of another string
         * IntegerRange: Integer is between, less than or greater than a particular range.
         */
        public enum MapRules {Exact, SubString, IntegerRange};
        String toColumn;
        MapRules mapRule;

        /**
         * Column name to apply the filter to. (Exact match.)
         * @return Returns the string
         */
        public String getToColumn() {
            return toColumn;
        }

        /**
         * @see #getToColumn()
         */
        public void setToColumn(String toColumn) {
            this.toColumn = toColumn;
        }

        /**
         * The rule type to apply. Please note the configuration of some of the values are stored in the parameters
         * themselves.
         * @return RuleMapping
         */
        public MapRules getMapRule() {
            return mapRule;
        }

        /**
         * @see #getMapRule()
         */
        public void setMapRule(MapRules mapRule) {
            this.mapRule = mapRule;
        }
    }
}
