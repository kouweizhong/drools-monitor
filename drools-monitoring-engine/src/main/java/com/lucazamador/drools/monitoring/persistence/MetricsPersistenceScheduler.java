package com.lucazamador.drools.monitoring.persistence;

import java.util.Timer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Scheduler timer to executes a persistence task after a period of time
 * 
 * @author Lucas Amador
 * 
 */
public class MetricsPersistenceScheduler {

    private static final Logger logger = LoggerFactory.getLogger(MetricsPersistenceScheduler.class);
    private static final long DEFAULT_PERIOD = 60000;

    private Timer timerScheduler;
    private MetricsPersistenceSchedulerTask task;
    private long period;

    public MetricsPersistenceScheduler() {
        this.timerScheduler = new Timer();
    }

    public void start() {
        if (task != null) {
            if (period <= 0) {
                period = DEFAULT_PERIOD;
                logger.info("Time period wasn't provided or less-equal to zero. Using default period: " + period);
            }
            timerScheduler.scheduleAtFixedRate(task, 0, period);
        }
    }

    public void stop() {
        // TODO: persist the currents metrics in memory
        // if (task != null) {
        // timerScheduler.schedule(task, 0);
        // }
        timerScheduler.cancel();
    }

    public void setTask(MetricsPersistenceSchedulerTask task) {
        this.task = task;
    }

    public void setPeriod(long period) {
        this.period = period;
    }

}
