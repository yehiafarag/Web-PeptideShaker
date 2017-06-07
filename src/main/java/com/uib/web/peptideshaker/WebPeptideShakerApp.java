package com.uib.web.peptideshaker;

import com.compomics.util.experiment.identification.identification_parameters.SearchParameters;
import com.uib.web.peptideshaker.galaxy.DataSet;
import com.uib.web.peptideshaker.galaxy.GalaxyFile;
import com.uib.web.peptideshaker.galaxy.GalaxyLayer;
import com.uib.web.peptideshaker.presenter.GalaxyFileSystemPresenter;
import com.uib.web.peptideshaker.presenter.PeptideShakerViewPresenter;
import com.uib.web.peptideshaker.presenter.ToolPresenter;
import com.uib.web.peptideshaker.presenter.WelcomePage;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.VerticalLayout;
import java.util.Map;
import java.util.Set;

/**
 * This class represents the main Web PeptideShaker application
 *
 * @author Yehia Farag
 */
public class WebPeptideShakerApp extends VerticalLayout {

    /**
     * The Galaxy server layer.
     */
    private final GalaxyLayer Galaxy_Layer;
    /**
     * The tools view component.
     */
    private final ToolPresenter toolsView;
    
    private final  GalaxyFileSystemPresenter fileSystemView;

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

        Galaxy_Layer = new GalaxyLayer() {
            @Override
            public void systemConnected() {
                presentationManager.setSideButtonsVisible(true);
                connectGalaxy();
            }

            @Override
            public void systemDisconnected() {
                presentationManager.setSideButtonsVisible(false);
            }

            @Override
            public void jobsInProgress(boolean inprogress) {
                fileSystemView.setBusy(inprogress);
                presentationManager.viewLayout(fileSystemView.getViewId());
                
            }

        };

        WelcomePage welcomePage = new WelcomePage(Galaxy_Layer.getGalaxyConnectionPanel());
        presentationManager.registerView(welcomePage);
        presentationManager.viewLayout(welcomePage.getViewId());

        toolsView = new ToolPresenter() {
            @Override
            public void executeWorkFlow(String fastaFileId, Set<String> mgfIdsList, Set<String> searchEnginesList,SearchParameters searchParameters,Map<String,Boolean>otherSearchParameters) {
                Galaxy_Layer.executeWorkFlow(fastaFileId, mgfIdsList, searchEnginesList,searchParameters,otherSearchParameters);
            }

            @Override
            public Map<String, GalaxyFile>  saveSearchGUIParameters(SearchParameters searchParameters, String fileName) {
                return Galaxy_Layer.saveSearchGUIParameters( searchParameters,  fileName);
                 
            }

        };
        presentationManager.registerView(toolsView);
//         
        fileSystemView = new GalaxyFileSystemPresenter();
        presentationManager.registerView(fileSystemView);
        
        PeptideShakerViewPresenter peptideShakerView = new PeptideShakerViewPresenter();
        presentationManager.registerView(peptideShakerView);

    }

    private void connectGalaxy() {
        if (Galaxy_Layer.checkToolsAvailable()) {
            toolsView.getRightView().setEnabled(true);
            toolsView.getTopView().setEnabled(true);
            toolsView.getRightView().setDescription("Click to view the tools layout");
            toolsView.getTopView().setDescription("Click to view the tools layout");
            toolsView.updateHistoryHandler(Galaxy_Layer.getSearchSettingsFilesMap(),Galaxy_Layer.getFastaFilesMap(), Galaxy_Layer.getMgfFilesMap());
        } else {
            toolsView.getRightView().setDescription("Tools are not available");
            toolsView.getTopView().setDescription("Tools are not available");
            toolsView.getRightView().setEnabled(false);
            toolsView.getTopView().setEnabled(false);
        }
    }

}
