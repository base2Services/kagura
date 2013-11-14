package com.base2.kagura.core.report.connectors;

import com.base2.kagura.core.report.configmodel.FreeMarkerSQLReportConfig;
import com.base2.kagura.core.report.configmodel.JDBCReportConfig;
import com.base2.kagura.core.report.configmodel.JNDIReportConfig;
import org.apache.commons.lang3.StringUtils;

import javax.naming.NamingException;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Created with IntelliJ IDEA.
 * User: aubels
 * Date: 24/07/13
 * Time: 4:39 PM
 * To change this template use File | Settings | File Templates.
 */
public class JDBCDataReportConnector extends FreemarkerSQLDataReportConnector {

    private String jdbc;
    private Properties connectionProps;

    public JDBCDataReportConnector(JDBCReportConfig reportConfig) {
        super(reportConfig);
        try {
            if (StringUtils.isNotBlank(reportConfig.getClassLoaderPath()))
                try
                {
                    Class.forName(reportConfig.getClassLoaderPath());
                } catch (ClassNotFoundException e) {
                    errors.add(e.getMessage());
                    e.printStackTrace();
                }
            connectionProps = new Properties();
            if (StringUtils.isNotBlank(reportConfig.getUsername()))
                connectionProps.put("user", reportConfig.getUsername());
            if (StringUtils.isNotBlank(reportConfig.getPassword()))
                connectionProps.put("password", reportConfig.getPassword());
            setJdbc(reportConfig.getJdbc());
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
        connection = DriverManager.getConnection(getJdbc(), connectionProps);
    }

    public String getJdbc() {
        return jdbc;
    }

    public void setJdbc(String jdbc) {
        this.jdbc = jdbc;
    }
}
