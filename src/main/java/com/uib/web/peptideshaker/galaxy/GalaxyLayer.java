package com.uib.web.peptideshaker.galaxy;

import com.compomics.util.experiment.identification.identification_parameters.SearchParameters;
import com.github.jmchilton.blend4j.galaxy.GalaxyInstance;
import com.uib.web.peptideshaker.presenter.components.GalaxyConnectionPanelLayout;
import com.vaadin.server.Page;
import com.vaadin.server.Sizeable;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.ui.PopupView;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * This class represents Galaxy layer that work as middle layer between the
 * application and the galaxy server
 *
 * @author Yehia Farag
 */
public abstract class GalaxyLayer {

    private final HorizontalLayout galaxyConnectionPanel;
    private final PopupView connectionSettingsPanel;
//    /**
//     * The main galaxy Tools Client on galaxy server.
//     */
//    private  ToolsClient galaxyToolClient;
//    /**
//     * The main galaxy Work-Flow Client on galaxy server.
//     */
//    private  WorkflowsClient galaxyWorkFlowClient;
//    /**
//     * The main galaxy Work-Flow Client on galaxy server.
//     */
//    private  HistoriesClient galaxyHistoriesClient;
    /**
     * Galaxy server history management system
     *
     */
    private HistoryHandler historyHandler;

    /**
     * Galaxy server tools management handler
     *
     */
    private ToolsHandler toolsHandler;

    private File userFolder;

    private String galaxyURL;

    /**
     * Constructor to initialize Galaxy layer.
     */
    public GalaxyLayer() {

        galaxyConnectionPanel = new HorizontalLayout();
        galaxyConnectionPanel.setSizeFull();
        galaxyConnectionPanel.setSpacing(true);

        Label connectionStatuesLabel = new Label("Galaxy is<font color='red'>  not connected </font><font size='3' color='red'> &#128528;</font>");
        connectionStatuesLabel.setContentMode(ContentMode.HTML);

        connectionStatuesLabel.setHeight(20, Sizeable.Unit.PIXELS);
        connectionStatuesLabel.setWidth(160, Sizeable.Unit.PIXELS);
        connectionStatuesLabel.setStyleName(ValoTheme.LABEL_SMALL);
        connectionStatuesLabel.addStyleName(ValoTheme.LABEL_BOLD);
        connectionStatuesLabel.addStyleName(ValoTheme.LABEL_TINY);
        connectionStatuesLabel.addStyleName(ValoTheme.LABEL_NO_MARGIN);
        galaxyConnectionPanel.addComponent(connectionStatuesLabel);
        galaxyConnectionPanel.setComponentAlignment(connectionStatuesLabel, Alignment.BOTTOM_LEFT);

        HorizontalLayout galaxyConnectionBtnContainer = new HorizontalLayout();
        galaxyConnectionBtnContainer.setWidthUndefined();
        galaxyConnectionBtnContainer.setHeight(100, Sizeable.Unit.PERCENTAGE);
        galaxyConnectionBtnContainer.setSpacing(false);
        galaxyConnectionPanel.addComponent(galaxyConnectionBtnContainer);
        galaxyConnectionPanel.setComponentAlignment(galaxyConnectionBtnContainer, Alignment.BOTTOM_RIGHT);

        Button connectionBtn = new Button("Connect");
        connectionBtn.setDisableOnClick(true);
        connectionBtn.setStyleName("galaxybtn");
        connectionBtn.addStyleName(ValoTheme.BUTTON_LINK);
        connectionBtn.setDescription("Connect / Disconnect to Galaxy server");
        connectionBtn.addStyleName(ValoTheme.LABEL_NO_MARGIN);
        connectionBtn.setWidth(150, Sizeable.Unit.PIXELS);
        connectionBtn.setHeight(25, Sizeable.Unit.PIXELS);
        galaxyConnectionBtnContainer.addComponent(connectionBtn);
        galaxyConnectionBtnContainer.setComponentAlignment(connectionBtn, Alignment.BOTTOM_RIGHT);

        VerticalLayout settingBtn = new VerticalLayout();
        settingBtn.addStyleName("settingbtn");
        settingBtn.setDescription("Galaxy server settings");
        settingBtn.setWidth(25, Sizeable.Unit.PIXELS);
        settingBtn.setHeight(25, Sizeable.Unit.PIXELS);
        galaxyConnectionBtnContainer.addComponent(settingBtn);
        galaxyConnectionBtnContainer.setComponentAlignment(settingBtn, Alignment.BOTTOM_RIGHT);

         String userDataFolderUrl = VaadinSession.getCurrent().getSession().getAttribute("userDataFolderUrl")+"";
                           System.out.println("----------------- user folder ---- "+userDataFolderUrl);
         GalaxyConnectionPanelLayout galaxyConnectionSettingsPanel = new GalaxyConnectionPanelLayout() {
            @Override
            public void connectedToGalaxy(GalaxyInstance Galaxy_Instance) {
                try {
                    if (Galaxy_Instance != null) {
                        System.out.println("at not null galaxy");

                        if (VaadinSession.getCurrent().getSession().getAttribute("ApiKey") != null) {
                            String APIKey = VaadinSession.getCurrent().getSession().getAttribute("ApiKey").toString();
                            if (!APIKey.equalsIgnoreCase(Galaxy_Instance.getApiKey())) {
                                //clean history and create new folder
                                userFolder = new File(userDataFolderUrl,APIKey);
                                if (userFolder.exists()) {
                                    for (File tFile : userFolder.listFiles()) {
                                        tFile.delete();
                                    }
                                }
                                userFolder.delete();
                            }
                        }
                        userFolder = new File(userDataFolderUrl,Galaxy_Instance.getApiKey() + "");
                        userFolder.mkdir();
                        VaadinSession.getCurrent().getSession().setAttribute("ApiKey", Galaxy_Instance.getApiKey() + "");

                        galaxyURL = Galaxy_Instance.getGalaxyUrl();

                        toolsHandler = new ToolsHandler(Galaxy_Instance.getToolsClient(), Galaxy_Instance.getWorkflowsClient(), Galaxy_Instance.getHistoriesClient());
                        historyHandler = new HistoryHandler(Galaxy_Instance, userFolder) {
                            @Override
                            public String reIndexFile(String id, String historyId, String workHistoryId) {
                                return GalaxyLayer.this.reIndexFile(id, historyId, workHistoryId);
                            }

                            @Override
                            public void systemIsBusy(boolean busy, Map<String, SystemDataSet> historyFilesMap) {
                                //update history in the system 
                                jobsInProgress(busy, historyFilesMap);
                            }

                        };//                        
                        connectionBtn.setCaption("Disconnect");
                        connectionBtn.addStyleName("disconnect");
                        connectionStatuesLabel.setValue("Galaxy is <font color='green'>connected </font><font size='3' color='green'> &#128522;</font>");
                        systemConnected();
                    } else {
                        System.out.println("at null galaxy");
                        connectionSettingsPanel.setPopupVisible(true);
                        historyHandler = null;
                        toolsHandler = null;
                        systemDisconnected();
                    }
//
                    connectionBtn.setEnabled(true);
                } catch (Exception exp) {
                    exp.printStackTrace();
                    System.out.println("at err .connectedToGalaxy()");
                    historyHandler = null;
                    toolsHandler = null;
                    systemDisconnected();

                }
            }

            @Override
            public void hideGalaxyPanel() {
                connectionSettingsPanel.setPopupVisible(false);
            }

        };
        connectionSettingsPanel = new PopupView(null, galaxyConnectionSettingsPanel);
        connectionSettingsPanel.setSizeFull();
        connectionSettingsPanel.setHideOnMouseOut(false);
        settingBtn.addComponent(connectionSettingsPanel);

        connectionBtn.addClickListener((Button.ClickEvent event) -> {

            if (connectionBtn.getCaption().equalsIgnoreCase("Disconnect")) {
                //disconnect from galaxy
                connectionBtn.setCaption("Connect");
                connectionBtn.removeStyleName("disconnect");
                connectionStatuesLabel.setValue("Galaxy is<font color='red'>  not connected </font><font size='3' color='red'> &#128528;</font>");
                galaxyConnectionSettingsPanel.disconnectGalaxy();
                historyHandler = null;
                toolsHandler = null;
                connectionBtn.setEnabled(true);
                UI.getCurrent().getSession().close();
                VaadinSession.getCurrent().getSession().invalidate();
                Page.getCurrent().reload();

            } else {
                //connect to galaxy
                galaxyConnectionSettingsPanel.validateAndConnect();
            }

        });

    }

    public Layout getGalaxyConnectionPanel() {
        return galaxyConnectionPanel;

    }

//    /**
//     * Connect to Galaxy server
//     * @param username username. 
//     * @param password  user password. 
//     **/
//    public boolean connectToGalaxy(String username,String password){
//    
//    return true;
//    }
//    /**
//     * Connect to Galaxy server
//     * @param galaxyAPIKey personal API key. 
//     **/
//    public boolean connectToGalaxy(String galaxyAPIKey){
//    
//    return true;
//    }
    public abstract void systemConnected();

    public abstract void systemDisconnected();

    /**
     * Get the main search settings .par files Map
     *
     * @return searchSettingsFilesMap
     */
    public Map<String, GalaxyFile> getSearchSettingsFilesMap() {
        if (historyHandler != null) {
            return historyHandler.getSearchSettingsFilesMap();
        } else {
            return new HashMap<>();
        }
    }
    /**
     * Get the main search settings .par files Map
     *
     * @return searchSettingsFilesMap
     */
    public Map<String, PeptideShakerVisualizationDataset> getPeptideShakerVisualizationMap() {
        if (historyHandler != null) {
            return historyHandler.getPeptideShakerVisualizationMap();
        } else {
            return new HashMap<>();
        }
    }

    /**
     * Get the main FASTA files Map
     *
     * @return fastaFilesMap
     */
    public Map<String, SystemDataSet> getFastaFilesMap() {
        if (historyHandler != null) {
            return historyHandler.getFastaFilesMap();
        } else {
            return new HashMap<>();
        }
    }

    /**
     * Get the main FASTA files Map
     *
     * @return fastaFilesMap
     */
    public Map<String, SystemDataSet> getHistoryFilesMap() {
        if (historyHandler != null) {
            return historyHandler.getHistoryFilesMap();
        } else {
            return new HashMap<>();
        }
    }

    public boolean checkToolsAvailable() {

        return toolsHandler.isValidTools();
    }

    /**
     * Get the main MGF files Map
     *
     * @return mgfFilesMap
     *
     */
    public Map<String, SystemDataSet> getMgfFilesMap() {
        if (historyHandler != null) {
            return historyHandler.getMgfFilesMap();
        } else {
            return new HashMap<>();
        }
    }

    /**
     * Re-Index the files (FASTA or MGF files)
     *
     * @param id file id on galaxy server
     * @param historyId the history id that the file belong to
     * @param workHistoryId the history id that the new re-indexed file will be
     * stored in
     *
     * @return new re-indexed file id on galaxy
     *
     */
    public String reIndexFile(String id, String historyId, String workHistoryId) {

        if (toolsHandler != null) {
            return toolsHandler.reIndexFile(id, historyId, workHistoryId);
        }

        return null;
    }

    /**
     * Save search settings file into galaxy
     *
     * @param fileName search parameters file name
     * @param searchParameters searchParameters .par file
     */
    public Map<String, GalaxyFile> saveSearchGUIParameters(SearchParameters searchParameters, boolean editMode) {

        if (toolsHandler != null) {
            return toolsHandler.saveSearchGUIParameters(galaxyURL, userFolder, historyHandler.getSearchSettingsFilesMap(), historyHandler.getWorkingHistoryId(), searchParameters, editMode);
        }
        return null;

    }

    /**
     * Run Online Peptide-Shaker work-flow
     *
     * @param fastaFileId FASTA file dataset id
     * @param mgfIdsList list of MGF file dataset ids
     * @param searchEnginesList List of selected search engine names
     * @param historyId galaxy history id that will store the results
     */
    public void executeWorkFlow(String projectName, String fastaFileId, Set<String> mgfIdsList, Set<String> searchEnginesList, SearchParameters searchParameters, Map<String, Boolean> otherSearchParameters) {
        PeptideShakerVisualizationDataset tempWorkflowOutput = toolsHandler.executeWorkFlow(projectName, fastaFileId, mgfIdsList, searchEnginesList, historyHandler.getWorkingHistoryId(), searchParameters, otherSearchParameters);
        historyHandler.updateHistoryDatastructure(userFolder, tempWorkflowOutput);
    }

    public void deleteDataset(SystemDataSet ds) {
        if (ds.getType().equalsIgnoreCase("Web Peptide Shaker Dataset")) {
            PeptideShakerVisualizationDataset vDs = (PeptideShakerVisualizationDataset) ds;            
            toolsHandler.deleteDataset(galaxyURL, vDs.getHistoryId(), vDs.getCpsId());
            toolsHandler.deleteDataset(galaxyURL, vDs.getHistoryId(), vDs.getProteinFileId());
            toolsHandler.deleteDataset(galaxyURL, vDs.getHistoryId(), vDs.getPeptideFileId());
            toolsHandler.deleteDataset(galaxyURL, vDs.getHistoryId(), vDs.getPsmFileId());
            toolsHandler.deleteDataset(galaxyURL, vDs.getHistoryId(), vDs.getSearchGUIFileId());

        } else {
            toolsHandler.deleteDataset(galaxyURL, ds.getHistoryId(), ds.getGalaxyId());
        }

        historyHandler.updateHistoryDatastructure(userFolder, null);
    }

    public abstract void jobsInProgress(boolean inprogress, Map<String, SystemDataSet> historyFilesMap);

}
