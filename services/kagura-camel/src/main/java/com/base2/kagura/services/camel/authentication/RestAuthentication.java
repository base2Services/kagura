package com.base2.kagura.services.camel.authentication;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author aubels
 *         Date: 7/10/13
 */
@Service()
public class RestAuthentication extends com.base2.kagura.core.reporting.view.authentication.RestAuthentication {
    private static final Logger LOG = LoggerFactory.getLogger(RestAuthentication.class);

    public RestAuthentication() {
        super();
    }

    @Override
    @Value("${com.base2.kagura.rest.authUrl:http://localhost:8080/ReportAuth}")
    public void setUrl(String url) {
        super.setUrl(url);
        LOG.info("Got URL {}", url);
    }

    @Override
    public String getUrl() {
        return super.getUrl();
    }
}
