package com.base2.kagura.services.camel.kagura;

import com.base2.kagura.core.reporting.view.authentication.AuthenticationProvider;
import com.base2.kagura.services.camel.authentication.FileAuthentication;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.DateConverter;
import org.apache.commons.beanutils.converters.DateTimeConverter;
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
    @Value("${com.base2.kagura.reportloc:/TestReports/}")
    private String configPath;
    @Value("${com.base2.kagura.authtype:fileAuthentication}")
    private String authType;

    private ApplicationContext applicationContext;

    public ServerBean() {
        DateTimeConverter dtConverter = new DateConverter(null);
        dtConverter.setPatterns(new String[] {"yyyy-MM-dd", "yyyy-MM-dd hh:mm:ss"});
        ConvertUtils.register(dtConverter, Date.class);
    }

    public String getConfigPath() {
        File file = new File(configPath);
        if (file.exists())
            return file.getAbsolutePath() + "/";
        return ServerBean.class.getResource(configPath).getFile();
    }

    public void setConfigPath(String configPath) {
        this.configPath = configPath;
    }

    @Bean()
    public AuthenticationProvider authenticationProvider()
    {
        AuthenticationProvider authenticationProvider = (AuthenticationProvider) applicationContext.getBean(authType);
        return authenticationProvider;
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
}
