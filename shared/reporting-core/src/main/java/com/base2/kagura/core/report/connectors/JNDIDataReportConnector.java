package com.base2.kagura.core.report.connectors;

import com.base2.kagura.core.report.configmodel.FreeMarkerSQLReportConfig;
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
    private String jndi;

    public JNDIDataReportConnector(JNDIReportConfig reportConfig) {
        super(reportConfig);

        try {
            setJndi(reportConfig.getJndi());
            getStartConnection();
            connection.close();
            connection = null;
        } catch (SQLException e) {
            errors.add(e.getMessage());
            e.printStackTrace();
        } catch (NamingException e) {
            errors.add(e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    protected void getStartConnection() throws NamingException, SQLException {
        InitialContext initContext = new InitialContext();
        if (datasource == null)
            datasource = (DataSource) initContext.lookup(getJndi());
        connection = datasource.getConnection();
    }

    public String getJndi() {
        return jndi;
    }

    public void setJndi(String jndi) {
        this.jndi = jndi;
    }
}
