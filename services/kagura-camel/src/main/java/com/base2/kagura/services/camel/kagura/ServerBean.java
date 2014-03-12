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
package com.base2.kagura.services.camel.kagura;

import com.base2.kagura.core.authentication.AuthenticationProvider;
import com.base2.kagura.core.storage.ReportsProvider;
import com.base2.kagura.rest.helpers.ParameterUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.DateConverter;
import org.apache.commons.beanutils.converters.DateTimeConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Date;

/**
 * @author aubels
 *         Date: 29/08/13
 */
@Service()
public class ServerBean implements ApplicationContextAware {
    private static final Logger LOG = LoggerFactory.getLogger(ServerBean.class);

    @Value("${com.base2.kagura.reportloc:/TestReports/}")
    private String configPath;
    @Value("${com.base2.kagura.authtype:fileAuthentication}")
    private String authType;
    @Value("${com.base2.kagura.reportStorage:fileReportsProvider}")
    private String reportStorage;
    @Value("${com.base2.kagura.exportLimit:10000}")
    private Integer exportLimit;

    private ApplicationContext applicationContext;

    public ServerBean() {
        ParameterUtils.SetupDateConverters();
    }

    public void setConfigPath(String configPath) {
        this.configPath = configPath;
    }

    @Bean()
    public AuthenticationProvider authenticationProvider()
    {
        LOG.info("Loading Authentication provider: " + authType);
        if (applicationContext == null)
        {
            LOG.info("No spring context can not load authentication provider");
            return null;
        }
        final Object bean = applicationContext.getBean(authType);
        if (bean == null)
            LOG.info("Warning bean is null.");
        return (AuthenticationProvider) bean;
    }

    @Bean()
    public ReportsProvider<?> reportsProvider()
    {
        final Object bean = applicationContext.getBean(reportStorage);
        if (applicationContext == null)
        {
            LOG.info("No spring context can not load reports provider");
            return null;
        }
        LOG.info("Loading reports provider: " + reportStorage);
        if (bean == null)
            LOG.info("Warning bean is null.");
        return (ReportsProvider<?>) bean;
    }

    /**
     * Set the ApplicationContext that this object runs in.
     * Normally this call will be used to initialize the object.
     * <p>Invoked after population of normal bean properties but before an init callback such
     * as {@link org.springframework.beans.factory.InitializingBean#afterPropertiesSet()}
     * or a custom init-method. Invoked after {@link org.springframework.context.ResourceLoaderAware#setResourceLoader},
     * {@link org.springframework.context.ApplicationEventPublisherAware#setApplicationEventPublisher} and
     * {@link org.springframework.context.MessageSourceAware}, if applicable.
     *
     * @param applicationContext the ApplicationContext object to be used by this object
     * @throws org.springframework.context.ApplicationContextException
     *          in case of context initialization errors
     * @throws org.springframework.beans.BeansException
     *          if thrown by application context methods
     * @see org.springframework.beans.factory.BeanInitializationException
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public String getAuthType() {
        return authType;
    }

    public void setAuthType(String authType) {
        this.authType = authType;
    }

    public String getReportStorage() {
        return reportStorage;
    }

    public void setReportStorage(String reportStorage) {
        this.reportStorage = reportStorage;
    }

    public Integer getExportLimit() {
        return exportLimit;
    }

    public void setExportLimit(Integer exportLimit) {
        this.exportLimit = exportLimit;
    }
}
