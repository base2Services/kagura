package com.base2.kagura.services.camel.authentication;

import com.base2.kagura.core.reporting.view.authentication.AuthenticationProvider;
import com.base2.kagura.core.reporting.view.authentication.model.Group;
import com.base2.kagura.core.reporting.view.authentication.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author aubels
 *         Date: 7/10/13
 */
@Service()
public class HybridAuthentication extends AuthenticationProvider implements ApplicationContextAware {
    private static final Logger LOG = LoggerFactory.getLogger(HybridAuthentication.class);
    private ApplicationContext applicationContext;

    public HybridAuthentication() {
        super();
    }

    @Value("${com.base2.kagura.hybridAuth.userAuthenticator:file}")
    private String userAuthenticatorConf;

    @Value("${com.base2.kagura.hybridAuth.groupAuthenticator:file}")
    private String groupAuthenticatorConf;

    private AuthenticationProvider userAuthenticator;
    private AuthenticationProvider groupAuthenticator;

    @Override
    public List<Group> getGroups() {
        return groupAuthenticator.getGroups();
    }

    @Override
    public List<User> getUsers() {
        return userAuthenticator.getUsers();
    }

    @Override
    public void authenticateUser(String user, String pass) throws Exception {
        userAuthenticator.authenticateUser(user, pass);
    }

    public String getUserAuthenticatorConf() {
        return userAuthenticatorConf;
    }

    public void setUserAuthenticatorConf(String userAuthenticatorConf) {
        this.userAuthenticatorConf = userAuthenticatorConf;
        setUserAuthenticator((AuthenticationProvider) applicationContext.getBean(userAuthenticatorConf));
    }

    public String getGroupAuthenticatorConf() {
        return groupAuthenticatorConf;
    }

    public void setGroupAuthenticatorConf(String groupAuthenticatorConf) {
        this.groupAuthenticatorConf = groupAuthenticatorConf;
        setGroupAuthenticator((AuthenticationProvider) applicationContext.getBean(groupAuthenticatorConf));
    }

    public AuthenticationProvider getUserAuthenticator() {
        return userAuthenticator;
    }

    public void setUserAuthenticator(AuthenticationProvider userAuthenticator) {
        this.userAuthenticator = userAuthenticator;
    }

    public AuthenticationProvider getGroupAuthenticator() {
        return groupAuthenticator;
    }

    public void setGroupAuthenticator(AuthenticationProvider groupAuthenticator) {
        this.groupAuthenticator = groupAuthenticator;
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
