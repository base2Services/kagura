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
package com.base2.kagura.core.report.freemarker;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;

/**
 * Because the WHERE tag can be used multiple times, and it's one instance for the life time of the freemarker
 * engine, we require a stackable "context" to allow for multiple tags and nestable where statements. The previous
 * WhereContext is stored in the "parent" parameter. (These are inturn stored in FreeMakers attribute storage.)
 * @author aubels
 *         Date: 18/09/13
 */
public class WhereContext {
    private WhereContext parent;
    private String connector = "AND";
    private ArrayList<String> freemarkerWhereClauses = new ArrayList<String>();

    /**
     * Initializes, pointing to parent context.
     * @param parent Parent context, can be null.
     */
    public WhereContext(Object parent) {
        this.parent = (WhereContext)parent;
    }

    /**
     * Adds a where clause to the current context. Only performed on where clauses which are rendered.
     * @param freemarkerWhereClause
     */
    public void addWhereClause(FreemarkerWhereClause freemarkerWhereClause) {
        String outputBody = freemarkerWhereClause.getOutputBody();
        if (StringUtils.isNotBlank(outputBody))
            freemarkerWhereClauses.add(outputBody);
    }

    /**
     * Adds a nested where clause.
     * @param outputBody we aren't doing many smarts so we only care for the output.
     */
    public void addWhereBody(String outputBody) {
        if (StringUtils.isNotBlank(outputBody))
            freemarkerWhereClauses.add(outputBody);
    }

    /**
     * Parent where context.
     * @return Parent where context. Null if highest. (Means we output the WHERE sql token.
     */
    public WhereContext getParent() {
        return parent;
    }

    /**
     * @see #getParent()
     */
    public void setParent(WhereContext parent) {
        this.parent = parent;
    }

    /**
     * Returns the logical connector. AND or OR. Stored by the where clause. Used when constructing output.
     * @return logical connector to join with.
     */
    public String getConnector() {
        return connector;
    }

    /**
     * {@inheritDoc}
     */
    public void setConnector(String connector) {
        this.connector = connector;
    }

    /**
     * The array of bodies produced by the where clauses and nested wheres.
     * @return
     */
    public ArrayList<String> getFreemarkerWhereClauses() {
        return freemarkerWhereClauses;
    }

    /**
     * {@inheritDoc}
     */
    public void setFreemarkerWhereClauses(ArrayList<String> freemarkerWhereClauses) {
        this.freemarkerWhereClauses = freemarkerWhereClauses;
    }
}
