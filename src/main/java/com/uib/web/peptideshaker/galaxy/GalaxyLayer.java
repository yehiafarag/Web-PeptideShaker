package com.uib.web.peptideshaker.galaxy;

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

        GalaxyConnectionPanelLayout galaxyConnectionSettingsPanel = new GalaxyConnectionPanelLayout() {
            @Override
            public void connectedToGalaxy(GalaxyInstance Galaxy_Instance) {
                try {
                    if (Galaxy_Instance != null) {
                        System.out.println("at not null galaxy");
                     toolsHandler = new ToolsHandler(Galaxy_Instance.getToolsClient(), Galaxy_Instance.getWorkflowsClient(), Galaxy_Instance.getHistoriesClient());
                        historyHandler = new HistoryHandler(Galaxy_Instance) {
                            @Override
                            public String reIndexFile(String id, String historyId, String workHistoryId) {
                                return GalaxyLayer.this.reIndexFile(id, historyId, workHistoryId);
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
     * Get the main Fasta files Map
     *
     * @return fastaFilesMap
     */
    public Map<String, DataSet> getFastaFilesMap() {
        if (historyHandler != null) {
            return historyHandler.getFastaFilesMap();
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
    public Map<String, DataSet> getMgfFilesMap() {
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
     * Run Online Peptide-Shaker work-flow
     *
     * @param fastaFileId FASTA file dataset id
     * @param mgfIdsList list of MGF file dataset ids
     * @param searchEnginesList List of selected search engine names
     * @param historyId galaxy history id that will store the results
     */
    public void executeWorkFlow(String fastaFileId, Set<String> mgfIdsList, Set<String> searchEnginesList) {
        toolsHandler.executeWorkFlow(fastaFileId, mgfIdsList, searchEnginesList, historyHandler.getWorkingHistoryId());
        historyHandler.updateHistoryDatastructure();
    }

}
