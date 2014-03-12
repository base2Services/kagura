/*
   Copyright 2014 base2Services

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */
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
public class RestAuthentication extends com.base2.kagura.core.authentication.RestAuthentication {
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
