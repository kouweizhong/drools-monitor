package com.lucazamador.drools.monitoring.core.recovery;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lucazamador.drools.monitoring.core.MonitoringAgentRegistry;
import com.lucazamador.drools.monitoring.core.agent.MonitoringAgent;
import com.lucazamador.drools.monitoring.listener.MonitoringRecoveryListener;

/**
 * 
 * @author Lucas Amador
 * 
 */
public class MonitoringRecoveryAgent {

    private Logger logger = LoggerFactory.getLogger(MonitoringRecoveryAgent.class);

    private static final int DEFAULT_RECOVERY_INTERVAL = 10000;
    private MonitoringAgentRegistry registry;
    private MonitoringRecoveryListener recoveryListener;
    private Map<String, MonitoringRecoveryTask> recoveryTasks = new HashMap<String, MonitoringRecoveryTask>();

    public void reconnect(String agentId, String address, int port) {
        MonitoringAgent monitoringAgent = registry.getMonitoringAgent(agentId);
        int recoveryInterval = monitoringAgent.getConnector().getRecoveryInterval();
        monitoringAgent.stop();
        Timer reconnectionTimer = new Timer();
        int period = recoveryInterval > 0 ? recoveryInterval : DEFAULT_RECOVERY_INTERVAL;
        MonitoringRecoveryTask recoveryTask = new MonitoringRecoveryTask(agentId, address, port, period, registry,
                recoveryListener);
        logger.info("Recovery task created to reconnect with " + agentId + " at " + address + ":" + port);
        recoveryTasks.put(agentId, recoveryTask);
        recoveryListener.disconnected(agentId);
        reconnectionTimer.scheduleAtFixedRate(recoveryTask, 0, period);
    }

    public void removeRecoveryTask(String agentId) {
        MonitoringRecoveryTask recoveryTask = recoveryTasks.remove(agentId);
        if (recoveryTask != null) {
            recoveryTask.cancel();
        }
    }

    public void registerListener(MonitoringRecoveryListener recoveryListener) {
        this.recoveryListener = recoveryListener;
        for (MonitoringRecoveryTask recoveryTask : recoveryTasks.values()) {
            recoveryTask.setRecoveryListener(recoveryListener);
        }
    }

    public MonitoringAgentRegistry getMonitoringAgentRegistry() {
        return registry;
    }

    public void setMonitoringAgentRegistry(MonitoringAgentRegistry registry) {
        this.registry = registry;
    }

}
