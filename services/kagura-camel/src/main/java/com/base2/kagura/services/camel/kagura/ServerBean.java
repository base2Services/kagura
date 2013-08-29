package com.base2.kagura.services.camel.kagura;

import org.springframework.stereotype.Service;

import java.net.URI;

/**
 * @author aubels
 *         Date: 29/08/13
 */
@Service()
public class ServerBean {
    private String configPath = "";

    public String getConfigPath() {
        return configPath;
    }

    public void setConfigPath(String configPath) {
        this.configPath = configPath;
    }
}
