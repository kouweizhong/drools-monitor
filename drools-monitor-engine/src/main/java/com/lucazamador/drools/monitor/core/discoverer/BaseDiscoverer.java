package com.lucazamador.drools.monitor.core.discoverer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;

import com.lucazamador.drools.monitor.core.mbean.DroolsMBeanConnector;
import com.lucazamador.drools.monitor.exception.DroolsMonitoringException;
import com.lucazamador.drools.monitor.listener.ResourceDiscoveredListener;
import com.lucazamador.drools.monitor.scanner.MetricScanner;

/**
 * Basic functionality for the knowledge discoverer. Discover the registered
 * Drools MBean resources.
 * 
 * @author Lucas Amador
 * 
 */
public abstract class BaseDiscoverer {

    private DroolsMBeanConnector connector;
    private ResourceDiscoveredListener discoveredListener;
    private Map<String, MetricScanner> resourceScanners = Collections
            .synchronizedMap(new HashMap<String, MetricScanner>());

    protected List<ObjectName> discoverResourceType(String resourceFilter) throws MalformedObjectNameException,
            IOException, DroolsMonitoringException {
        if (connector == null) {
            throw new DroolsMonitoringException("DroolsMBeanConnector must be provided");
        }
        Set<ObjectName> names = connector.getConnection().queryNames(new ObjectName(resourceFilter), null);
        List<ObjectName> resourceName = new ArrayList<ObjectName>();
        for (ObjectName objectName : names) {
            resourceName.add(objectName);
        }
        return resourceName;
    }

    public DroolsMBeanConnector getConnector() {
        return connector;
    }

    public void setConnector(DroolsMBeanConnector connector) {
        this.connector = connector;
    }

    public ResourceDiscoveredListener getResourceDiscoveredListener() {
        return this.discoveredListener;
    }

    public void setResourceDiscoveredListener(ResourceDiscoveredListener discoveredListener) {
        this.discoveredListener = discoveredListener;
    }

    public Map<String, MetricScanner> getResourceScanners() {
        return resourceScanners;
    }

    public abstract void discover() throws DroolsMonitoringException;

}
