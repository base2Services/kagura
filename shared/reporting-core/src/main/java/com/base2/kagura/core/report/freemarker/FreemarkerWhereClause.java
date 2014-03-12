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

import freemarker.core.Environment;
import freemarker.template.*;
import org.apache.commons.lang3.BooleanUtils;

import java.io.IOException;
import java.io.StringWriter;
import java.util.List;
import java.util.Map;

/**
 * Where clause, meant to be used in conjunction with a WHERE. The where clause is similar to an if statement, where it
 * will render the contents inside if the render= option has been set correctly. (The render option is optional in cases
 * you want it to always render.) Render= has to be a freemarker boolean expression. This has been designed for
 * continence when handling report parameters.
 * User: aubels
 * Date: 29/07/13
 * Time: 2:50 PM
 */
public class FreemarkerWhereClause implements TemplateDirectiveModel
{
    private final List<String> errors;
    private String outputBody;

    /**
     * Constructor.
     * @param errors location to append errors.
     */
    public FreemarkerWhereClause(List<String> errors) {
        this.errors = errors;
    }

    /**
     * Looks for a render option, to determine if it should be run. This is supposed to encapsulate a single SQL where
     * clause. Such as "name IS NOT NULL" or "id > ${method.value(param.lowLimit)}". This should be encapsulated within
     * a where tag. If this doesn't have an render attribute it will run regardless. When this is successful it will
     * populate the parent where clause with the contents.
     *
     * {@inheritDoc}
     */
    @Override
    public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body) throws TemplateException, IOException {
        if (params.size() > 1) {
            String message = "This directive doesn't allow multiple parameters.";
            errors.add(message);
            throw new TemplateModelException(message);
        }
        if (params.size() != 0 && !((Map.Entry)params.entrySet().toArray()[0]).getKey().equals("render"))
        {
            String message = "This directive only takes 'render'.";
            errors.add(message);
            throw new TemplateModelException(message);
        }
        Object renderParam = params.get("render");
        if (renderParam == null && params.size() != 0) return;
        if (params.size() == 0)
        {} else if (renderParam instanceof TemplateBooleanModel)
        {
            TemplateBooleanModel render = (TemplateBooleanModel) renderParam;
            if (BooleanUtils.isNotTrue(render.getAsBoolean()))
                return;
        } else {
            String message = "This directive only accepts boolean values for 'render'.";
            errors.add(message);
            throw new TemplateModelException(message);
        }
        if (loopVars.length != 0) {
            String message = "This directive doesn't allow loop variables.";
            errors.add(message);
            throw new TemplateModelException(message);
        }
        WhereContext whereContext = (WhereContext)env.getCustomAttribute("whereTag");
        if (whereContext == null) {
            String message = "Can not find parent where clause.";
            errors.add(message);
            throw new TemplateModelException(message);
        }
        StringWriter stringWriter = new StringWriter();
        body.render(stringWriter);
        outputBody = stringWriter.toString();
        whereContext.addWhereClause(this);
    }

    /**
     * The output of the last run instance of this tag.
     * @return rendered text
     */
    public String getOutputBody() {
        return outputBody;
    }
}
