package com.lucazamador.drools.monitor.eclipse.perspective;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;
import org.eclipse.ui.console.IConsoleConstants;

import com.lucazamador.drools.monitor.eclipse.view.GraphicView;
import com.lucazamador.drools.monitor.eclipse.view.KnowledgeBaseView;
import com.lucazamador.drools.monitor.eclipse.view.KnowledgeSessionView;
import com.lucazamador.drools.monitor.eclipse.view.MonitoringAgentView;

public class DroolsMonitoringPerspective implements IPerspectiveFactory {

    public static final String ID = "com.lucazamador.drools.monitor.eclipse.perspective.droolsMonitoringPerspective";

    public void createInitialLayout(IPageLayout layout) {
        String editorArea = layout.getEditorArea();
        layout.setEditorAreaVisible(false);

        layout.addStandaloneView(MonitoringAgentView.ID, true, IPageLayout.LEFT, 0.20f, editorArea);

        IFolderLayout folder = layout.createFolder("views", IPageLayout.TOP, 0.5f, editorArea);
        folder.addPlaceholder(KnowledgeBaseView.ID + ":*");
        folder.addPlaceholder(KnowledgeSessionView.ID + ":*");
        folder.addPlaceholder(GraphicView.ID + ":*");
        layout.getViewLayout(KnowledgeBaseView.ID).setMoveable(false);
        layout.getViewLayout(KnowledgeBaseView.ID).setCloseable(true);
        layout.getViewLayout(KnowledgeSessionView.ID).setMoveable(false);
        layout.getViewLayout(KnowledgeSessionView.ID).setCloseable(true);
        layout.getViewLayout(GraphicView.ID).setMoveable(false);
        layout.getViewLayout(GraphicView.ID).setCloseable(true);
        layout.getViewLayout(MonitoringAgentView.ID).setCloseable(false);

        IFolderLayout consoleFolder = layout.createFolder("console", IPageLayout.BOTTOM, 0.70f, "views");
        consoleFolder.addView(IConsoleConstants.ID_CONSOLE_VIEW);

        layout.addPerspectiveShortcut("Drools Monitoring");

    }
}
