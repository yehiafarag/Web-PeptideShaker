package com.uib.web.peptideshaker;

import com.github.jmchilton.blend4j.galaxy.GalaxyInstance;
import com.uib.web.peptideshaker.galaxy.HistoryHandler;
import com.uib.web.peptideshaker.presenter.PeptideShakerViewPresenter;
import com.uib.web.peptideshaker.presenter.ToolPresenter;
import com.uib.web.peptideshaker.presenter.WelcomePage;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.VerticalLayout;

/**
 * This class represents the main Web PeptideShaker application
 *
 * @author Yehia Farag
 */
public class WebPeptideShakerApp extends VerticalLayout {

    /**
     * The tools view component.
     */
    private final ToolPresenter toolsView;

    /**
     * Constructor to initialize the application.
     */
    public WebPeptideShakerApp() {
        WebPeptideShakerApp.this.setSizeFull();
        WebPeptideShakerApp.this.setMargin(new MarginInfo(true, true, true, true));
        WebPeptideShakerApp.this.addStyleName("autooverflow");
        WebPeptideShakerApp.this.addStyleName("frame");
        PresenterManager presentationManager = new PresenterManager();
        WebPeptideShakerApp.this.addComponent(presentationManager);

        WelcomePage welcomePage = new WelcomePage() {
            @Override
            public void systemConnected(GalaxyInstance galaxyInstant) {
                if (galaxyInstant == null) {
                    presentationManager.setSideButtonsVisible(false);
                } else {
                    presentationManager.setSideButtonsVisible(true);
                    connectGalaxy(galaxyInstant);
                }

            }

        };
        presentationManager.registerView(welcomePage);
        presentationManager.viewLayout(welcomePage.getViewId());

        toolsView = new ToolPresenter();
        presentationManager.registerView(toolsView);
//         
        PeptideShakerViewPresenter peptideShakerView = new PeptideShakerViewPresenter();
        presentationManager.registerView(peptideShakerView);

    }

    private void connectGalaxy(GalaxyInstance Galaxy_Instance) {
        HistoryHandler historyHandler = new HistoryHandler(Galaxy_Instance);
        toolsView.updateHistoryHandler(historyHandler);
    }

}
