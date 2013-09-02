package com.base2.kagura.services.camel.kagura;

import org.springframework.stereotype.Service;
import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * @author aubels
 *         Date: 29/08/13
 */
@Service()
public class ServerBean {
    private String configPath;

    public ServerBean() {
        URL dir_url = ServerBean.class.getResource("/TestReports");
        try {
            configPath = dir_url.toURI().toString();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public String getConfigPath() {
        return configPath;
    }

    public void setConfigPath(String configPath) {
        this.configPath = configPath;
    }
}
