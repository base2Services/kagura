package com.base2.kagura.core.reporting.view.report.freemarker;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
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
    ArrayList<String> freemarkerWhereClauses = new ArrayList<String>();

    public FreemarkerWhere(List<String> errors) {
        this.errors = errors;
    }

    @Override
    public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body) throws TemplateException, IOException {
        StringWriter stringWriter = new StringWriter();
        env.setVariable("whereTag", this);
        body.render(stringWriter);
        String outputBody = StringUtils.join(freemarkerWhereClauses, " AND ");
        if (StringUtils.isNotBlank(outputBody)) {
            env.getOut().write(" WHERE ");
            env.getOut().write(outputBody);
        }
    }

    public void addWhereClause(FreemarkerWhereClause freemarkerWhereClause) {
        String outputBody = freemarkerWhereClause.getOutputBody();
        if (StringUtils.isNotBlank(outputBody))
            freemarkerWhereClauses.add(outputBody);
    }
}
