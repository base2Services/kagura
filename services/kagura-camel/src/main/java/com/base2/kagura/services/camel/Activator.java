package com.base2.kagura.services.camel;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author aubels
 *         Date: 26/08/13
 */
public class Activator implements BundleActivator {

    private static final Logger LOG = LoggerFactory.getLogger(Activator.class);

    public void start(BundleContext context) {
        LOG.info("Starting the bundle: " + this.getClass().getName());
    }

    public void stop(BundleContext context) {
        LOG.info("Stopping the bundle: " + this.getClass().getName());
    }

}
