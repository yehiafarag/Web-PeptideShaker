package com.uib.web.peptideshaker;

import com.github.jmchilton.blend4j.galaxy.GalaxyInstance;
import com.uib.web.peptideshaker.presenter.ToolPresenter;
import com.uib.web.peptideshaker.presenter.WelcomePage;
import com.vaadin.ui.VerticalLayout;

/**
 * This class represents the main Web PeptideShaker application
 *
 * @author Yehia Farag
 */
public class WebPeptideShakerApp extends VerticalLayout {

    /**
     * Constructor to initialize the application
     */
    public WebPeptideShakerApp() {
        WebPeptideShakerApp.this.setSizeFull();
        WebPeptideShakerApp.this.setStyleName("margin");
        WebPeptideShakerApp.this.addStyleName("autooverflow");
        PresenterManager presentationManager = new PresenterManager();
        WebPeptideShakerApp.this.addComponent(presentationManager);
        
        
        WelcomePage welcomePage = new WelcomePage(){
            @Override
            public void systemConnected(GalaxyInstance galaxyInstant) {
                if(galaxyInstant==null)
                    presentationManager.setSideButtonsVisible(false);
                else
                      presentationManager.setSideButtonsVisible(true);
            }
            
        };
        presentationManager.registerView(welcomePage);
        presentationManager.viewLayout(welcomePage.getViewId());
        
        ToolPresenter toolsView = new ToolPresenter();
         presentationManager.registerView(toolsView);
        
        
    }
    
}
