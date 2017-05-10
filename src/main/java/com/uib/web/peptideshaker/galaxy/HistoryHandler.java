package com.uib.web.peptideshaker.galaxy;

import com.github.jmchilton.blend4j.galaxy.GalaxyInstance;
import com.github.jmchilton.blend4j.galaxy.HistoriesClient;
import com.github.jmchilton.blend4j.galaxy.beans.History;
import com.github.jmchilton.blend4j.galaxy.beans.HistoryContentsProvenance;
import com.github.wolfie.refresher.Refresher;
import com.uib.web.peptideshaker.PeptidShakerUI;
import com.vaadin.server.Page;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.ui.window.WindowMode;
import com.vaadin.ui.Notification;

import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
     * The main Fasta File Map.
     */
    private final Map<String, DataSet> fastaFilesMap;
    /**
     * The main MGF Files Map.
     */
    private final Map<String, DataSet> mgfFilesMap;

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
    /**
     * History progress icon
     *
     */
    private final Window progressWindow;
    /**
     * Refresher to keep tracking history state in galaxy
     *
     */
    private final Refresher REFRESHER;

    /**
     * Constructor to initialize the main data structure and other variables.
     *
     * @param Galaxy_Instance the main Galaxy instance in the system
     *
     */
    public HistoryHandler(GalaxyInstance Galaxy_Instance) {
        this.Galaxy_Instance = Galaxy_Instance;
        this.fastaFilesMap = new LinkedHashMap<>();
        this.mgfFilesMap = new LinkedHashMap<>();
        this.peptideShakerVisualizationMap = new LinkedHashMap<>();
        this.searchGUIFilesMap = new LinkedHashMap<>();

        REFRESHER = new Refresher();
        ((PeptidShakerUI) UI.getCurrent()).addExtension(REFRESHER);

        this.progressWindow = new Window();
        this.progressWindow.setWidth(30, Unit.PIXELS);
        this.progressWindow.setHeight(30, Unit.PIXELS);
        progressWindow.setStyleName("progress");
        progressWindow.setPosition(-1, -1);
        progressWindow.setWindowMode(WindowMode.NORMAL);
        progressWindow.setClosable(false);
        progressWindow.setResizable(false);
        progressWindow.setDraggable(false);
        progressWindow.setDescription("Galaxy is still processing data");
        progressWindow.setVisible(false);
        UI.getCurrent().addWindow(progressWindow);

        this.updateHistoryDatastructure();

    }

    /**
     * Get the main Fasta files Map
     *
     * @return fastaFilesMap
     */
    public Map<String, DataSet> getFastaFilesMap() {
        return fastaFilesMap;
    }

    /**
     * Get the main MGF files Map
     *
     * @return mgfFilesMap
     */
    public Map<String, DataSet> getMgfFilesMap() {
        return mgfFilesMap;
    }

    /**
     * Update the fasta and MGF and peptide Shaker Visualization maps
     *
     * @return mgfFilesMap
     */
    public final void updateHistoryDatastructure() {
        try {

            fastaFilesMap.clear();
            mgfFilesMap.clear();
            searchGUIFilesMap.clear();
            peptideShakerVisualizationMap.clear();
            HistoriesClient galaxyHistoriesClient = Galaxy_Instance.getHistoriesClient();
            HistoriesClient loopGalaxyHistoriesClient = Galaxy_Instance.getHistoriesClient();

            System.out.println("at get history 1");
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
            System.out.println("at get history 2");
            historiesList = galaxyHistoriesClient.getHistories();
            Map<String, Map<String, Object>> workHistoryData = new LinkedHashMap<>();
//
//            System.out.println("at --- hList is updated ");
            for (History history : historiesList) {
//                if (history.getId().equalsIgnoreCase(workingHistory.getId())) {
//                    continue;
//                }
                System.out.println("at start query");
                final String query = "select * from hda where history_id= '" + history.getId() + "'";
                List<Map<String, Object>> results = Galaxy_Instance.getSearchClient().search(query).getResults();

                if (history.getId().equalsIgnoreCase(workingHistory.getId())) {
                    for (Map<String, Object> map : results) {
                        if (map.get("purged").toString().equalsIgnoreCase("true") || map.get("deleted").toString().equalsIgnoreCase("true")) {
                            continue;
                        }
                        System.out.println("at type " + map.get("name").toString());
                        if (map.get("data_type").toString().equalsIgnoreCase("galaxy.datatypes.binary.SearchGuiArchive")) {
                            HistoryContentsProvenance prov = loopGalaxyHistoriesClient.showProvenance(workingHistory.getId(), map.get("id").toString());
                            searchGUIFilesMap.put(map.get("id").toString(), prov);
                        } else if (map.get("data_type").toString().equalsIgnoreCase("galaxy.datatypes.tabular.Tabular") || map.get("data_type").toString().equalsIgnoreCase("galaxy.datatypes.sequence.peptideshaker_archive")) {
                            HistoryContentsProvenance prov = loopGalaxyHistoriesClient.showProvenance(workingHistory.getId(), map.get("id").toString());
                            String jobId = prov.getJobId();
                            if (!peptideShakerVisualizationMap.containsKey(jobId)) {
                                PeptideShakerVisualizationDataset vDs = new PeptideShakerVisualizationDataset(jobId);
                                peptideShakerVisualizationMap.put(jobId, vDs);
                            }
                            PeptideShakerVisualizationDataset vDs = peptideShakerVisualizationMap.get(jobId);
                            if (map.get("name").toString().endsWith("Protein Report")) {
                                vDs.setProteinFileId(map.get("id").toString());

                                vDs.setSearchGUIFileId(prov.getParameters().get("searchgui_input").toString().split(",")[0].split("id=")[1]);
                            } else if (map.get("name").toString().endsWith("Peptide Report")) {
                                vDs.setPeptideFileId(map.get("id").toString());

                                vDs.setSearchGUIFileId(prov.getParameters().get("searchgui_input").toString().split(",")[0].split("id=")[1]);
                            } else if (map.get("name").toString().endsWith("PSM Report")) {
                                vDs.setPsmFileId(map.get("id").toString());

                                vDs.setSearchGUIFileId(prov.getParameters().get("searchgui_input").toString().split(",")[0].split("id=")[1]);
                            } else if (map.get("data_type").toString().endsWith("peptideshaker_archive")) {
                                vDs.setCpsId(map.get("id").toString());
                                vDs.setSearchGUIFileId(prov.getParameters().get("searchgui_input").toString().split(",")[0].split("id=")[1]);
                            } else {
                                workHistoryData.put(map.get("name").toString(), map);
                            }
                        }

                    }
                } else {

                    for (Map<String, Object> map : results) {
                        if (map.get("purged").toString().equalsIgnoreCase("true") || map.get("deleted").toString().equalsIgnoreCase("true")) {
                            continue;
                        }
                        DataSet ds = new DataSet();
                        ds.setName(map.get("name").toString());
                        ds.setHistoryId(history.getId());
                        ds.setGalaxyId(map.get("id").toString());
                        if (map.get("data_type").toString().equalsIgnoreCase("galaxy.datatypes.sequence.Fasta")) {
                            this.fastaFilesMap.put(ds.getGalaxyId(), ds);
                        } else if (map.get("data_type").toString().equalsIgnoreCase("galaxy.datatypes.proteomics.Mgf")) {
                            this.mgfFilesMap.put(ds.getGalaxyId(), ds);
                        }
//                    if(map.get("purged").)
//                    for (String key : map.keySet()) {
//                        System.err.println("searching resilts " + key);
//                    }
                    }
//                List<HistoryContents> contents = loopGalaxyHistoriesClient.showHistoryContents(history.getId());
//                HistoriesClient loopGalaxyHistoriesClient2 = Galaxy_Instance.getHistoriesClient();
//
//                for (HistoryContents content : contents) {
//                    if (content.isDeleted()) {
//                        continue;
//                    }
//                    Dataset lDs = loopGalaxyHistoriesClient2.showDataset(history.getId(), content.getId());
//                    DataSet ds = new DataSet();
//                    ds.setName(lDs.getName());
//                    ds.setHistoryId(history.getId());
//                    ds.setGalaxyId(lDs.getId());
//
//                    if (lDs.getDataTypeExt().equalsIgnoreCase("fasta")) {
//                        this.fastaFilesMap.put(ds.getGalaxyId(), ds);
//                    } else if (lDs.getDataTypeExt().equalsIgnoreCase("mgf")) {
//                        this.mgfFilesMap.put(ds.getGalaxyId(), ds);
//                    }
//
//                }
                }
            }
            System.out.println("at --- hList is stage 2 ");
//            loopGalaxyHistoriesClient = Galaxy_Instance.getHistoriesClient();
//            Map<String, Dataset> fulldsMap = new HashMap<>();
//            List<HistoryContents> contents = galaxyHistoriesClient.showHistoryContents(workingHistory.getId());
//
//            for (HistoryContents content : contents) {
//                if (content.isDeleted() || content.isPurged()) {
//                    continue;
//                }
//                Dataset ds = loopGalaxyHistoriesClient.showDataset(workingHistory.getId(), content.getId());
//                fulldsMap.put(ds.getId(), ds);
//
//            }
//            // check Fasta and MGF files need re-indexing
//
            System.out.println("at --- hList is stage 3 ");
            for (DataSet ds : fastaFilesMap.values()) {
                if (workHistoryData.containsKey(ds.getGalaxyId())) {
                    ds.setReIndexedHistoryId(workingHistory.getId());
                    ds.setReIndexedId(workHistoryData.get(ds.getGalaxyId()).get("id").toString());
                } else {
                    String reIndexId = this.reIndexFile(ds.getGalaxyId(), ds.getHistoryId(), workingHistory.getId());
                    ds.setReIndexedHistoryId(workingHistory.getId());
                    ds.setReIndexedId(reIndexId);
                }

            }
            for (DataSet ds : mgfFilesMap.values()) {
                if (workHistoryData.containsKey(ds.getGalaxyId())) {
                    ds.setReIndexedHistoryId(workingHistory.getId());
                    ds.setReIndexedId(workHistoryData.get(ds.getGalaxyId()).get("id").toString());
                } else {
                    String reIndexId = this.reIndexFile(ds.getGalaxyId(), ds.getHistoryId(), workingHistory.getId());
                    ds.setReIndexedHistoryId(workingHistory.getId());
                    ds.setReIndexedId(reIndexId);
                }

            }

//
//            loopGalaxyHistoriesClient = Galaxy_Instance.getHistoriesClient();
////            
//            for (HistoryContents content : contents) {
//                if (content.isDeleted()) {
//                    continue;
//                }
//                Dataset ds = fulldsMap.get(content.getId());// galaxyHistoriesClient.showDataset(workingHistory.getId(), content.getId());
//                if (fastaFilesMap.containsKey(ds.getName())) {
//                    fastaFilesMap.get(ds.getName()).setReIndexedHistoryId(workingHistory.getId());
//                    fastaFilesMap.get(ds.getName()).setReIndexedId(ds.getId());
//                } else if (mgfFilesMap.containsKey(ds.getName())) {
//                    mgfFilesMap.get(ds.getName()).setReIndexedHistoryId(workingHistory.getId());
//                    mgfFilesMap.get(ds.getName()).setReIndexedId(ds.getId());
//
//                } else if (ds.getDataTypeExt().equalsIgnoreCase("searchgui_archive")) {
//                    HistoryContentsProvenance prov = loopGalaxyHistoriesClient.showProvenance(workingHistory.getId(), content.getId());
//                    searchGUIFilesMap.put(ds.getId(), prov);
//                } else if (ds.getDataTypeExt().equalsIgnoreCase("tabular") || ds.getDataTypeExt().equalsIgnoreCase("peptideshaker_archive")) {
//                    HistoryContentsProvenance prov = loopGalaxyHistoriesClient.showProvenance(workingHistory.getId(), content.getId());
//                    String jobId = prov.getJobId();
//                    if (!peptideShakerVisualizationMap.containsKey(jobId)) {
//                        PeptideShakerVisualizationDataset vDs = new PeptideShakerVisualizationDataset(jobId);
//                        peptideShakerVisualizationMap.put(jobId, vDs);
//                    }
//                    PeptideShakerVisualizationDataset vDs = peptideShakerVisualizationMap.get(jobId);
//                    if (ds.getName().endsWith("Protein Report")) {
//                        vDs.setProteinFileId(ds.getId());
//                    } else if (ds.getName().endsWith("Peptide Report")) {
//                        vDs.setPeptideFileId(ds.getId());
//                    } else if (ds.getName().endsWith("PSM Report")) {
//                        vDs.setPsmFileId(ds.getId());
//                    } else if (ds.getDataTypeExt().endsWith("peptideshaker_archive")) {
//                        vDs.setCpsId(ds.getId());
//                    }
//                    vDs.setSearchGUIFileId(prov.getParameters().get("searchgui_input").toString().split(",")[0].split("id=")[1]);
//
//                }
//
//                System.out.println("at --- hList is stage 4 ");
//
//            }
//            //re-index un indexed fasta and mgf files
//
            System.out.println("at --- hList is stage 5 ");
//            for (String id : fastaFilesMap.keySet()) {
//                if (fastaFilesMap.get(id).getReIndexedId() == null) {
//                    String reIndexId = this.reIndexFile(fastaFilesMap.get(id).getGalaxyId(), fastaFilesMap.get(id).getHistoryId(), workingHistory.getId());
//                    fastaFilesMap.get(id).setReIndexedHistoryId(workingHistory.getId());
//                    fastaFilesMap.get(id).setReIndexedId(reIndexId);
//                }
//
//            }
//            for (String id : mgfFilesMap.keySet()) {
//                if (mgfFilesMap.get(id).getReIndexedId() == null) {
//                    String reIndexId = this.reIndexFile(mgfFilesMap.get(id).getGalaxyId(), mgfFilesMap.get(id).getHistoryId(), workingHistory.getId());
//                    mgfFilesMap.get(id).setReIndexedHistoryId(workingHistory.getId());
//                    mgfFilesMap.get(id).setReIndexedId(reIndexId);
//                }
//
//            }
//            for (PeptideShakerVisualizationDataset vDs : peptideShakerVisualizationMap.values()) {
//                if (searchGUIFilesMap.containsKey(vDs.getSearchGUIFileId())) {
//                    Map<String, Object> params = searchGUIFilesMap.get(vDs.getSearchGUIFileId()).getParameters();
//                    vDs.setFastaFileId(fastaFilesMap.get(params.get("input_database").toString().split(",")[0].split("id=")[1]).getReIndexedId());
//                    int mgfFileNumber = params.keySet().toString().split("peak_lists").length - 2;
//                    for (int x = 1; x <= mgfFileNumber; x++) {
//                        vDs.addMgfFiles(mgfFilesMap.get(params.get("peak_lists" + x).toString().split(",")[0].split("id=")[1]).getReIndexedId());
//                    }
//
//                }
//                System.out.println("at vds " + vDs.getJobId() + "   is valid " + vDs.isValidFile());
//
//            }
//
//            System.out.println("at --- hList is stage 6 ");
            checkNotReadyHistory();
//
//            System.out.println("at --- hList is stage 7 ---- done ! ");
//
        } catch (Exception e) {
            if (e.toString().contains("Service Temporarily Unavailable")) {
                Notification.show("Service Temporarily Unavailable", Notification.Type.ERROR_MESSAGE);
                UI.getCurrent().getSession().close();
                VaadinSession.getCurrent().getSession().invalidate();
              

            }else{
            System.out.println("at error --- " + e.toString());
            System.out.println("at history are not available");
            UI.getCurrent().getSession().close();
            VaadinSession.getCurrent().getSession().invalidate();
            Page.getCurrent().reload();
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

    private void notReadyHistory(String name) {
        if (name == null) {
            progressWindow.setVisible(false);
            return;
        }
        progressWindow.setVisible(true);
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
                    System.out.println("--------------------- at the history not ready --------------------- " + name);

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

}
