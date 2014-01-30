package com.base2.kagura.core.report.freemarker;

import freemarker.core.Environment;
import freemarker.template.*;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Where tag. Has two functions:
 * 1. Inserts WHERE if not nested and contains content.
 * 2. Acts like a JOIN statement for clauses, so that if a you have a couple of children clause statements some of which
 *      do return results, others that don't, it will insert an AND or OR between each one.
 *
 * For complicated logic, WHERE tags can be nested, the parent one provides the WHERE clause if required. Each where
 * clause allows you to specify if elements should be joined with a "AND" or an "OR" by specifying the "type" attribute.
 * User: aubels
 * Date: 29/07/13
 * Time: 2:50 PM
 */
public class FreemarkerWhere implements TemplateDirectiveModel
{
    private final List<String> errors;

    /**
     * Constructor
     * @param errors Location to append errors if there are any.
     */
    public FreemarkerWhere(List<String> errors) {
        this.errors = errors;
    }

    /**
     * Detects if it's a child where or a parent where, runs the child clauses and where statements, if there are any
     * results, adds a WHERE at the start if it's a parent, then adds the selected operator (and or or ) between each
     * clause.
     *
     * {@inheritDoc}
     */
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
