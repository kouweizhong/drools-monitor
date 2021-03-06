package com.lucazamador.drools.monitor.core.mbean;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Set;

import javax.management.MBeanServerConnection;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

import com.lucazamador.drools.monitor.exception.DroolsMonitoringException;

/**
 * Class used to connect with the JVM using JMX. Sad to say that this
 * implementation doesn't supports connection timeout.
 * 
 * @author Lucas Amador
 * 
 */
public class DroolsMBeanConnector {

    private static final String JMX_URL = "service:jmx:rmi:///jndi/rmi://$address:$port/jmxrmi";
    private static final String DROOLS_MANAGEMENT_RESOURCE_NAMESPACE = "org.drools:type=DroolsManagementAgent";

    private String address;
    private int port;
    private int recoveryInterval;
    private MBeanServerConnection connection;
    private boolean connected;

    public DroolsMBeanConnector(String address, int port, int recoveryInterval) {
        this.address = address;
        this.port = port;
        this.recoveryInterval = recoveryInterval;
    }

    /**
     * Create the connection with the JVM and start the discovery of the
     * registered knowledges resources
     * 
     * @return The connection created with the JVM MBeanServer
     * @throws DroolsMonitoringException
     */
    public void connect() throws DroolsMonitoringException {
        if (connected) {
            throw new IllegalStateException("Drools MBean Agent already connected");
        }
        if (address == null || address.trim().length() == 0) {
            throw new IllegalStateException("Monitoring agent address not configured");
        }
        if (port == 0) {
            throw new IllegalStateException("Monitoring agent port not configured");
        }
        String connectionURL = JMX_URL.replace("$address", address).replace("$port", String.valueOf(port));
        try {
            JMXConnector jmxConnector = JMXConnectorFactory.connect(new JMXServiceURL(connectionURL));
            this.connection = jmxConnector.getMBeanServerConnection();
            this.connected = true;
            if (!isDroolsMonitored()) {
                throw new DroolsMonitoringException("Drools Management MBeans aren't enabled in this application");
            }
        } catch (MalformedURLException e) {
            throw new DroolsMonitoringException("Incorrect JVM connection parameters. adress: " + address + " port: "
                    + port);
        } catch (IOException e) {
            throw new DroolsMonitoringException("Error connecting to JVM. address: " + address + " port: " + port, e);
        }
    }

    /**
     * Checks the existence of a DroolsManagmentAgent instance in the connected
     * JVM
     * 
     * @return
     * @throws DroolsMonitoringException
     */
    private boolean isDroolsMonitored() throws DroolsMonitoringException {
        try {
            Set<ObjectName> queryNames = connection.queryNames(new ObjectName(DROOLS_MANAGEMENT_RESOURCE_NAMESPACE),
                    null);
            return queryNames.size() > 0;
        } catch (IOException e) {
            throw new DroolsMonitoringException("JVM connection error", e);
        } catch (MalformedObjectNameException e) {
            throw new DroolsMonitoringException("JVM connection error", e);
        }
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getPort() {
        return this.port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getRecoveryInterval() {
        return recoveryInterval;
    }

    public void setRecoveryInterval(int recoveryInterval) {
        this.recoveryInterval = recoveryInterval;
    }

    public MBeanServerConnection getConnection() {
        return connection;
    }

    public boolean isConnected() {
        return connected;
    }

    public void disconnected() {
        this.connected = false;
    }

}
