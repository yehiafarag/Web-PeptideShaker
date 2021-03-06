package com.uib.web.peptideshaker.galaxy;

import com.github.jmchilton.blend4j.galaxy.GalaxyInstance;
import com.github.jmchilton.blend4j.galaxy.HistoriesClient;
import com.github.jmchilton.blend4j.galaxy.beans.History;
import com.github.jmchilton.blend4j.galaxy.beans.HistoryContents;
import com.github.jmchilton.blend4j.galaxy.beans.HistoryContentsProvenance;
import com.github.wolfie.refresher.Refresher;
import com.uib.web.peptideshaker.PeptidShakerUI;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Notification;

import com.vaadin.ui.UI;
import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * This class represents Galaxy history (galaxy file system) in Peptide-Shaker
 * web application the class is responsible for handling files in galaxy history
 * and shares it cross the application
 *
 * @author Yehia Farag
 */
public abstract class HistoryHandler {

    /**
     * The main Galaxy instance in the system.
     */
    private final GalaxyInstance Galaxy_Instance;
    /**
     * The main Search settings .par File Map.
     */
    private final Map<String, GalaxyFile> searchSetiingsFilesMap;
    /**
     * The main FASTA File Map.
     */
    private final Map<String, SystemDataSet> fastaFilesMap;
    /**
     * The main MGF Files Map.
     */
    private final Map<String, SystemDataSet> mgfFilesMap;
    /**
     * The Full historyFiles Map.
     */
    private final Map<String, SystemDataSet> historyFilesMap;

    /**
     * The main SearchGUI Files Map.
     */
    private final Map<String, HistoryContentsProvenance> searchGUIFilesMap;
    /**
     * The main PeptideShaker Visualization Map.
     */
    private final Map<String, PeptideShakerVisualizationDataset> peptideShakerVisualizationMap;
    /**
     * The Working galaxy history.
     */
    private History workingHistory;
    private final Set<String> historiesIds;
    private File userFolder;
    /**
     * History progress icon
     *
     */
//    private final Window progressWindow;
    /**
     * Refresher to keep tracking history state in galaxy
     *
     */
    private final Refresher REFRESHER;

    public Map<String, PeptideShakerVisualizationDataset> getPeptideShakerVisualizationMap() {
        return peptideShakerVisualizationMap;
    }

    /**
     * Constructor to initialize the main data structure and other variables.
     *
     * @param Galaxy_Instance the main Galaxy instance in the system
     *
     */
    public HistoryHandler(GalaxyInstance Galaxy_Instance, File userFolder) {
        this.Galaxy_Instance = Galaxy_Instance;
        this.searchSetiingsFilesMap = new LinkedHashMap<>();
        this.fastaFilesMap = new LinkedHashMap<>();
        this.mgfFilesMap = new LinkedHashMap<>();
        this.peptideShakerVisualizationMap = new LinkedHashMap<>();
        this.searchGUIFilesMap = new LinkedHashMap<>();
        this.historyFilesMap = new LinkedHashMap<>();
        this.historiesIds = new HashSet<>();

        REFRESHER = new Refresher();
        ((PeptidShakerUI) UI.getCurrent()).addExtension(REFRESHER);

//        this.progressWindow = new Window() {
//            @Override
//            public void setVisible(boolean visible) {
//                if (this.getStyleName().contains("blinkII")) {
//                    this.removeStyleName("blinkII");
//                    this.addStyleName("blink");
//                } else {
//                    this.removeStyleName("blink");
//                    this.addStyleName("blinkII");
//                }
//                super.setVisible(visible); //To change body of generated methods, choose Tools | Templates.
//            }
//
//        };
//        this.progressWindow.setWidth(30, Unit.PIXELS);
//        this.progressWindow.setHeight(30, Unit.PIXELS);
//        progressWindow.setStyleName("progress");
//        progressWindow.center();
//        progressWindow.setWindowMode(WindowMode.NORMAL);
//        progressWindow.setClosable(false);
//        progressWindow.setResizable(false);
//        progressWindow.setDraggable(false);
//        progressWindow.setDescription("Galaxy is still processing data");
//        progressWindow.setVisible(false);
//        UI.getCurrent().addWindow(progressWindow);
        this.userFolder = userFolder;
        this.updateHistoryDatastructure(userFolder, null);

    }

    /**
     * Get the main Search settings par files Map
     *
     * @return searchSetiingsFilesMap
     */
    public Map<String, GalaxyFile> getSearchSettingsFilesMap() {
        return searchSetiingsFilesMap;
    }

    /**
     * Get the main FASTA files Map
     *
     * @return fastaFilesMap
     */
    public Map<String, SystemDataSet> getFastaFilesMap() {
        return fastaFilesMap;
    }

    /**
     * Get the main MGF files Map
     *
     * @return mgfFilesMap
     */
    public Map<String, SystemDataSet> getMgfFilesMap() {
        return mgfFilesMap;
    }

    public Map<String, SystemDataSet> getHistoryFilesMap() {
        return historyFilesMap;
    }

    /**
     * Update the FASTA and MGF and peptide Shaker Visualization maps
     *
     * @return mgfFilesMap
     */
    public final void updateHistoryDatastructure(File userFolder, PeptideShakerVisualizationDataset tempWorkflowOutput) {
        try {

            fastaFilesMap.clear();
            mgfFilesMap.clear();
            searchGUIFilesMap.clear();
            historyFilesMap.clear();
            peptideShakerVisualizationMap.clear();
            searchSetiingsFilesMap.clear();
            historiesIds.clear();
            HistoriesClient galaxyHistoriesClient = Galaxy_Instance.getHistoriesClient();
            List<History> historiesList = galaxyHistoriesClient.getHistories();
            if (historiesList.isEmpty()) {
                galaxyHistoriesClient.create(new History("Online-PeptideShaker-History"));
                workingHistory = galaxyHistoriesClient.create(new History("Online-PeptideShaker-Job-History"));
            } else {
                for (History h : historiesList) {
                    if (h.getName().equalsIgnoreCase("Online-PeptideShaker-Job-History")) {
                        workingHistory = h;
                    }
                }
                if (workingHistory == null) {
                    workingHistory = Galaxy_Instance.getHistoriesClient().create(new History("Online-PeptideShaker-Job-History"));
                }
            }
            historiesList = galaxyHistoriesClient.getHistories();
            Map<String, Map<String, Object>> workHistoryData = new LinkedHashMap<>();

            for (History history : historiesList) {
                historiesIds.add(history.getId());
                final String query = "select * from hda where history_id= '" + history.getId() + "'";
                List<Map<String, Object>> results = Galaxy_Instance.getSearchClient().search(query).getResults();

                if (history.getId().equalsIgnoreCase(workingHistory.getId())) {
                    for (Map<String, Object> map : results) {
                        if (map.get("purged").toString().equalsIgnoreCase("true") || map.get("deleted").toString().equalsIgnoreCase("true")) {
                            continue;
                        }
                        if (map.get("data_type").toString().equalsIgnoreCase("galaxy.datatypes.binary.SearchGuiArchive")) {
                            HistoryContentsProvenance prov = galaxyHistoriesClient.showProvenance(workingHistory.getId(), map.get("id").toString());

                            searchGUIFilesMap.put(map.get("id").toString(), prov);
                        } else if (map.get("data_type").toString().equalsIgnoreCase("galaxy.datatypes.tabular.Tabular") || map.get("data_type").toString().equalsIgnoreCase("abc.CompressedArchive")) {
                            HistoryContentsProvenance prov = galaxyHistoriesClient.showProvenance(workingHistory.getId(), map.get("id").toString());

                            String jobId = prov.getJobId();

                            if (!peptideShakerVisualizationMap.containsKey(jobId)) {
                                PeptideShakerVisualizationDataset vDs = new PeptideShakerVisualizationDataset(jobId);
                                peptideShakerVisualizationMap.put(jobId, vDs);
                            }
                            PeptideShakerVisualizationDataset vDs = peptideShakerVisualizationMap.get(jobId);
                            if (map.get("name").toString().endsWith("(Proteins)")) {
                                if (!prov.getParameters().containsKey("searchgui_input")) {
                                    continue;
                                }
                                System.out.println("at int line number " + map.get("misc_blurb").toString() + "  " + map.get("name"));
                                vDs.setSearchGUIFileId(prov.getParameters().get("searchgui_input").toString().split(",")[0].replace("{id=", ""));
                                vDs.setName(map.get("name").toString().replace("(Proteins)", ""));
                                if (!map.get("misc_blurb").toString().equalsIgnoreCase("error")) {
                                    vDs.setProteinsNumber(Integer.valueOf(map.get("misc_blurb").toString().replace(" lines", "").replace(",", "")) - 1);
                                }
                                vDs.setGalaxyId(jobId);
                                vDs.setHistoryId(history.getId());
                                vDs.setType("Web Peptide Shaker Dataset");
                                vDs.setProteinFileId(map.get("id").toString());
                                vDs.setSearchGUIFileId(prov.getParameters().get("searchgui_input").toString().split(",")[0].split("id=")[1]);
                            } else if (map.get("name").toString().endsWith("(Peptides)")) {
                                vDs.setPeptideFileId(map.get("id").toString());
                                vDs.setSearchGUIFileId(prov.getParameters().get("searchgui_input").toString().split(",")[0].split("id=")[1]);
                            } else if (map.get("name").toString().endsWith("(PSM)")) {
                                vDs.setPsmFileId(map.get("id").toString());
                                vDs.setSearchGUIFileId(prov.getParameters().get("searchgui_input").toString().split(",")[0].split("id=")[1]);
                            } else if (map.get("name").toString().endsWith("(CPS)")) {
                                vDs.setCpsId(map.get("id").toString());
                                vDs.setDownloadUrl("to_ext=" + map.get("file_ext").toString());
                                vDs.setSearchGUIFileId(prov.getParameters().get("searchgui_input").toString().split(",")[0].split("id=")[1]);
                            } else {
                                workHistoryData.put(map.get("name").toString(), map);
                            }
                        } else if (map.get("data_type").toString().equalsIgnoreCase("galaxy.datatypes.text.Json") && map.get("name").toString().endsWith(".par")) {
                            SystemDataSet ds = new SystemDataSet();
                            ds.setName(map.get("name").toString());
                            ds.setType("Search Paramerters File (JSON)");
                            ds.setHistoryId(history.getId());
                            ds.setGalaxyId(map.get("id").toString());
                            ds.setDownloadUrl(Galaxy_Instance.getGalaxyUrl() + "/datasets/" + ds.getGalaxyId() + "/display");
                            GalaxyFile file = new GalaxyFile(userFolder, ds);
                            file.setDownloadUrl("to_ext=" + map.get("file_ext").toString());
                            this.searchSetiingsFilesMap.put(ds.getGalaxyId(), file);
                        }
                    }
                } else {

                    for (Map<String, Object> map : results) {
                        if (map.get("purged").toString().equalsIgnoreCase("true") || map.get("deleted").toString().equalsIgnoreCase("true")) {
                            continue;
                        }
                        SystemDataSet ds = new SystemDataSet();
                        ds.setName(map.get("name").toString());
                        ds.setHistoryId(history.getId());
                        ds.setGalaxyId(map.get("id").toString());

                        if (map.get("data_type").toString().equalsIgnoreCase("galaxy.datatypes.sequence.Fasta")) {
                            ds.setType("Fasta");
                            this.fastaFilesMap.put(ds.getGalaxyId(), ds);
                            ds.setDownloadUrl("to_ext=" + map.get("file_ext").toString());
                        } else if (map.get("data_type").toString().equalsIgnoreCase("galaxy.datatypes.proteomics.Mgf")) {
                            ds.setType("MGF");
                            ds.setDownloadUrl("to_ext=" + map.get("file_ext").toString());
                            this.mgfFilesMap.put(ds.getGalaxyId(), ds);
                        } else if (map.get("data_type").toString().equalsIgnoreCase("galaxy.datatypes.text.Json") && map.get("name").toString().endsWith(".par")) {
                            ds.setType("Search Paramerters File (JSON)");
                            ds.setDownloadUrl(Galaxy_Instance.getGalaxyUrl() + "/datasets/" + ds.getGalaxyId() + "/display");
                            GalaxyFile file = new GalaxyFile(userFolder, ds);
                            file.setDownloadUrl("to_ext=" + map.get("file_ext").toString());
                            this.searchSetiingsFilesMap.put(ds.getGalaxyId(), file);
                        }
                    }

                }

            }

            //if no search param file exist add default search param file 
            for (SystemDataSet ds : fastaFilesMap.values()) {
                ds.setReIndexedHistoryId(workingHistory.getId());
                if (workHistoryData.containsKey(ds.getGalaxyId())) {
                    ds.setReIndexedId(workHistoryData.get(ds.getGalaxyId()).get("id").toString());
                } else {
                    String reIndexId = this.reIndexFile(ds.getGalaxyId(), ds.getHistoryId(), workingHistory.getId());
                    ds.setReIndexedId(reIndexId);
                }

            }
            for (SystemDataSet ds : mgfFilesMap.values()) {
                ds.setReIndexedHistoryId(workingHistory.getId());
                if (workHistoryData.containsKey(ds.getGalaxyId())) {
                    ds.setReIndexedId(workHistoryData.get(ds.getGalaxyId()).get("id").toString());
                } else {
                    String reIndexId = this.reIndexFile(ds.getGalaxyId(), ds.getHistoryId(), workingHistory.getId());
                    ds.setReIndexedId(reIndexId);
                }

            }

            for (String key : peptideShakerVisualizationMap.keySet()) {
                PeptideShakerVisualizationDataset vDs = peptideShakerVisualizationMap.get(key);
                if (vDs.getSearchGUIFileId() == null || !searchGUIFilesMap.containsKey(vDs.getSearchGUIFileId())) {
                    continue;
                }
                Map<String, Object> parameters = searchGUIFilesMap.get(vDs.getSearchGUIFileId()).getParameters();
                String fastaFileId = parameters.get("input_database").toString().split(",")[0].replace("{id=", "");
                vDs.setFastaFileId(fastaFileId);
                vDs.setFastaFileIndex(fastaFilesMap.get(fastaFileId).getReIndexedId());
                for (String paramKey : parameters.keySet()) {
                    if (paramKey.contains("peak_list")) {
                        String mgfFileId = parameters.get(paramKey).toString().split(",")[0].replace("{id=", "");
                        vDs.addMgfFiles(mgfFileId);
                        vDs.addMGFFileIndex(mgfFileId, mgfFilesMap.get(mgfFileId).getReIndexedId());
                    }
                }

            }

            historyFilesMap.putAll(mgfFilesMap);
            historyFilesMap.putAll(fastaFilesMap);
            historyFilesMap.putAll(searchSetiingsFilesMap);
            historyFilesMap.putAll(peptideShakerVisualizationMap);
            if (tempWorkflowOutput != null) {
                historyFilesMap.put(tempWorkflowOutput.getJobId(), tempWorkflowOutput);
            }//
            updateDSStatus(false);
            systemIsBusy(true, historyFilesMap);
            checkNotReadyHistory();
//
        } catch (Exception e) {
            if (e.toString().contains("Service Temporarily Unavailable")) {
                Notification.show("Service Temporarily Unavailable", Notification.Type.ERROR_MESSAGE);
                UI.getCurrent().getSession().close();
                VaadinSession.getCurrent().getSession().invalidate();

            } else {
                System.out.println("at history are not available");
                UI.getCurrent().getSession().close();
                VaadinSession.getCurrent().getSession().invalidate();
                Notification.show(e.getMessage(), Notification.Type.ERROR_MESSAGE);
            }
        }

    }

    public String getWorkingHistoryId() {
        return workingHistory.getId();
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
    public abstract String reIndexFile(String id, String historyId, String workHistoryId);

//    /**
//     * Add default search parameter file to the user account
//     *
//     * @param workHistoryId the history id that the new re-indexed file will be
//     * stored in working history
//     *
//     * @return new dataset file from galaxy
//     *
//     */
//    public abstract SystemDataSet storeSearchParamfile(String workHistoryId);
    private void notReadyHistory(String name) {
        if (name == null) {
            updateDSStatus(true);
            systemIsBusy(false, historyFilesMap);
            return;
        }
//        progressWindow.setVisible(true);
        REFRESHER.setRefreshInterval(30000);

        HistoriesClient loopGalaxyHistoriesClient = Galaxy_Instance.getHistoriesClient();
        REFRESHER.addListener(new Refresher.RefreshListener() {
            @Override
            public void refresh(Refresher source) {
                boolean ready = (loopGalaxyHistoriesClient.showHistory(name).isReady());
                if (ready) {
                    REFRESHER.removeListener(this);
                    checkNotReadyHistory();
                } else {
                    System.out.println("--------------------- at the history not ready --------------------- " + name + "   ");

                }
            }
        });

    }

    private void checkNotReadyHistory() {

        HistoriesClient loopGalaxyHistoriesClient = Galaxy_Instance.getHistoriesClient();
        List<History> historiesList = Galaxy_Instance.getHistoriesClient().getHistories();
        for (History history : historiesList) {
            boolean ready = (loopGalaxyHistoriesClient.showHistory(history.getId()).isReady());
            if (!ready) {
                notReadyHistory(history.getId());
                return;
            }

        }
        notReadyHistory(null);
    }

    private void updateDSStatus(boolean readyHistory) {

        if (historyFilesMap.containsKey("tempID") && readyHistory) {
            updateHistoryDatastructure(userFolder, null);
        }
        HistoriesClient galaxyHistoriesClient = Galaxy_Instance.getHistoriesClient();
        for (String historyId : historiesIds) {
            List<HistoryContents> hcList = galaxyHistoriesClient.showHistoryContents(historyId);
            for (HistoryContents hc : hcList) {
                if (!historyFilesMap.containsKey(hc.getId())) {
                    continue;
                }
                if (historyFilesMap.get(hc.getId()).getName() == null) {
                    historyFilesMap.remove(hc.getId());
                } else {
                    SystemDataSet ds = historyFilesMap.get(hc.getId());
                    if (ds.getType().equalsIgnoreCase("Web Peptide Shaker Dataset")) {
                        PeptideShakerVisualizationDataset vDs = (PeptideShakerVisualizationDataset) ds;
                        ds.setStatus(Galaxy_Instance.getJobsClient().showJob(vDs.getJobId()).getState());
                        ds.setDownloadUrl(Galaxy_Instance.getGalaxyUrl() + "/api/histories/" + historyId + "/contents/" + vDs.getCpsId() + "/display?key=" + Galaxy_Instance.getApiKey() + "&" + ds.getDownloadUrl());

                    } else if (hc.getHistoryContentType().equalsIgnoreCase("dataset_collection")) {
                        ds.setStatus("ok");
                        ds.setDownloadUrl(Galaxy_Instance.getGalaxyUrl() + "/api/histories/" + historyId + "/contents/" + ds.getGalaxyId() + "/display?key=" + Galaxy_Instance.getApiKey() + "&" + ds.getDownloadUrl());

                    } else {
                        ds.setStatus(hc.getState());
                        ds.setDownloadUrl(Galaxy_Instance.getGalaxyUrl() + "/api/histories/" + historyId + "/contents/" + ds.getGalaxyId() + "/display?key=" + Galaxy_Instance.getApiKey() + "&" + ds.getDownloadUrl());

                    }
                    historyFilesMap.replace(ds.getGalaxyId(), ds);
                }

            }

        }

    }

    public abstract void systemIsBusy(boolean busy, Map<String, SystemDataSet> historyFilesMap);
}
