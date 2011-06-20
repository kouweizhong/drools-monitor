package com.lucazamador.drools.monitoring.cfg;

import java.io.InputStream;

import com.lucazamador.drools.monitoring.exception.DroolsMonitoringException;
import com.thoughtworks.xstream.XStream;

/**
 * 
 * @author Lucas Amador
 * 
 */
public class MonitoringConfigurationReader {

    private XStream xstream;
    private String configurationFile;

    public MonitoringConfigurationReader() {
        xstream = new XStream();
        xstream.processAnnotations(MonitoringConfiguration.class);
        xstream.processAnnotations(MonitoringAgentConfiguration.class);
        xstream.processAnnotations(PersistenceConfiguration.class);
    }

    public MonitoringConfiguration read() throws DroolsMonitoringException {
        if (configurationFile == null) {
            throw new DroolsMonitoringException("configuration file not specified");
        }
        InputStream inputStream = MonitoringConfigurationReader.class.getResourceAsStream(configurationFile);
        return (MonitoringConfiguration) xstream.fromXML(inputStream);
    }

    public String getConfigurationFile() {
        return configurationFile;
    }

    public void setConfigurationFile(String configurationFile) {
        this.configurationFile = configurationFile;
    }
}
