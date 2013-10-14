package com.base2.kagura.core.report.connectors;

import com.base2.kagura.core.report.configmodel.JNDIReportConfig;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * Created with IntelliJ IDEA.
 * User: aubels
 * Date: 24/07/13
 * Time: 4:39 PM
 * To change this template use File | Settings | File Templates.
 */
public class JNDIDataReportConnector extends FreemarkerSQLDataReportConnector {

    private DataSource datasource;

    public JNDIDataReportConnector(JNDIReportConfig reportConfig) {
        super(reportConfig);

        try {
            InitialContext initContext = new InitialContext();
            datasource = (DataSource)initContext.lookup(reportConfig.getJndi());
            connection = datasource.getConnection();
        } catch (SQLException e) {
            errors.add(e.getMessage());
            e.printStackTrace();
        } catch (NamingException e) {
            errors.add(e.getMessage());
            e.printStackTrace();
        }
    }
}
