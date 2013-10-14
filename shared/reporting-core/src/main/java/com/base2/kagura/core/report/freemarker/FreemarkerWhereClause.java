package com.base2.kagura.core.report.freemarker;

import freemarker.core.Environment;
import freemarker.template.*;
import org.apache.commons.lang3.BooleanUtils;

import java.io.IOException;
import java.io.StringWriter;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: aubels
 * Date: 29/07/13
 * Time: 2:50 PM
 * To change this template use File | Settings | File Templates.
 */
public class FreemarkerWhereClause implements TemplateDirectiveModel
{
    private final List<String> errors;
    private String outputBody;

    public FreemarkerWhereClause(List<String> errors) {
        this.errors = errors;
    }

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

    public String getOutputBody() {
        return outputBody;
    }

    public void setOutputBody(String outputBody) {
        this.outputBody = outputBody;
    }
}
