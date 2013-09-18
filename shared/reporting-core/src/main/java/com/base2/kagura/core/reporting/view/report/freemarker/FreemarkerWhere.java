package com.base2.kagura.core.reporting.view.report.freemarker;

import freemarker.core.Environment;
import freemarker.template.*;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: aubels
 * Date: 29/07/13
 * Time: 2:50 PM
 * To change this template use File | Settings | File Templates.
 */
public class FreemarkerWhere implements TemplateDirectiveModel
{
    private final List<String> errors;

    public FreemarkerWhere(List<String> errors) {
        this.errors = errors;
    }

    @Override
    public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body) throws TemplateException, IOException {
        StringWriter stringWriter = new StringWriter();
        WhereContext whereContext = new WhereContext(env.getCustomAttribute("whereTag"));
        env.setCustomAttribute("whereTag", whereContext);
        body.render(stringWriter);
        if (params.size() != 0 && !((Map.Entry)params.entrySet().toArray()[0]).getKey().equals("type"))
        {
            String message = "This directive only takes 'type' with a value of either 'AND' or 'OR'.";
            errors.add(message);
            throw new TemplateModelException(message);
        }
        Object typeParam = params.get("type");
        if (typeParam instanceof TemplateScalarModel)
        {
            TemplateScalarModel typeScalar = (TemplateScalarModel) typeParam;
            if (StringUtils.isNotBlank(typeScalar.getAsString()))
                whereContext.setConnector(typeScalar.getAsString().toUpperCase());
        }
        if (!Arrays.asList("AND", "OR").contains(whereContext.getConnector()))
        {
            String message = "This directive only takes 'type' with a value of either 'AND' or 'OR'.";
            errors.add(message);
            throw new TemplateModelException(message);
        }

        String outputBody = StringUtils.join(whereContext.getFreemarkerWhereClauses(), " " + whereContext.getConnector() + " ");
        if (whereContext.getFreemarkerWhereClauses().size() > 1 && whereContext.getParent() != null && !whereContext.getParent().getConnector().equals(whereContext.getConnector()))
            outputBody = "(" + outputBody + ")";
        if (StringUtils.isNotBlank(outputBody)) {
            if (whereContext.getParent() == null)
                env.getOut().write(" WHERE ");
            if (whereContext.getParent() != null)
                whereContext.getParent().addWhereBody(outputBody);
            else
                env.getOut().write(outputBody);
        }
        env.setCustomAttribute("whereTag", whereContext.getParent());
    }

}
