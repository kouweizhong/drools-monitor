package com.lucazamador.drools.monitor.eclipse;

import java.util.List;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;
import org.osgi.service.prefs.BackingStoreException;

import com.lucazamador.drools.monitor.core.DroolsMonitoring;
import com.lucazamador.drools.monitor.core.agent.MonitoringAgent;
import com.lucazamador.drools.monitor.eclipse.cfg.ConfigurationManager;
import com.lucazamador.drools.monitor.eclipse.model.MonitoringAgentInfo;
import com.lucazamador.drools.monitor.eclipse.model.MonitoringAgentInfoFactory;
import com.lucazamador.drools.monitor.eclipse.view.MonitoringAgentView;
import com.lucazamador.drools.monitor.exception.DroolsMonitoringException;

public class ApplicationWorkbenchWindowAdvisor extends WorkbenchWindowAdvisor {

    public static final String APP_TITLE = "Drools Monitor";

    public ApplicationWorkbenchWindowAdvisor(IWorkbenchWindowConfigurer configurer) {
        super(configurer);
    }

    public ActionBarAdvisor createActionBarAdvisor(IActionBarConfigurer configurer) {
        return new ApplicationActionBarAdvisor(configurer);
    }

    public void preWindowOpen() {
        IWorkbenchWindowConfigurer configurer = getWindowConfigurer();
        configurer.setTitle(APP_TITLE);
        configurer.setShowCoolBar(false);
        configurer.setShowStatusLine(true);
        configurer.setShowProgressIndicator(true);
    }

    @Override
    public void postWindowCreate() {
        IWorkbenchWindowConfigurer configurer = getWindowConfigurer();
        configurer.getWindow().getShell().setMaximized(true);
        initialize();
    }

    private void initialize() {
        ConfigurationManager configurationManager = new ConfigurationManager();
        DroolsMonitoring droolsMonitoring = Application.getDroolsMonitoring();
        try {
            List<MonitoringAgentInfo> agents = configurationManager.read();
            for (MonitoringAgentInfo agent : agents) {
                try {
                    droolsMonitoring.addMonitoringAgent(MonitoringAgentInfoFactory
                            .newMonitoringAgentConfiguration(agent));
                    MonitoringAgent monitoringAgent = droolsMonitoring.getMonitoringAgent(agent.getId());
                    if (monitoringAgent.isConnected()) {
                        agent.build(monitoringAgent);
                    }
                } catch (DroolsMonitoringException e) {
                    e.printStackTrace();
                    continue;
                }
                Application.getDroolsMonitor().addMonitoringAgent(agent);
            }
        } catch (BackingStoreException e) {
            MessageDialog.openError(getWindowConfigurer().getWindow().getShell(), "Error",
                    "Error reading default configuration");
        }
        MonitoringAgentView navigationView = (MonitoringAgentView) getWindowConfigurer().getWindow().getActivePage()
                .findView(MonitoringAgentView.ID);
        navigationView.refresh();
    }
}
