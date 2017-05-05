package com.uib.web.peptideshaker.galaxy;

import com.github.jmchilton.blend4j.galaxy.GalaxyInstance;
import com.github.jmchilton.blend4j.galaxy.beans.Dataset;
import com.github.jmchilton.blend4j.galaxy.beans.History;
import com.github.jmchilton.blend4j.galaxy.beans.HistoryContents;
import com.github.jmchilton.blend4j.galaxy.beans.HistoryContentsProvenance;
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
        fastaFilesMap.clear();
        mgfFilesMap.clear();
        searchGUIFilesMap.clear();
        peptideShakerVisualizationMap.clear();

        List<History> historiesList = Galaxy_Instance.getHistoriesClient().getHistories();

        if (historiesList.isEmpty()) {
            Galaxy_Instance.getHistoriesClient().create(new History("Online-PeptideShaker-History"));
            workingHistory = Galaxy_Instance.getHistoriesClient().create(new History("Online-PeptideShaker-Job-History"));
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
        historiesList = Galaxy_Instance.getHistoriesClient().getHistories();
        for (History history : historiesList) {
            if (history.getId().equalsIgnoreCase(workingHistory.getId())) {
                continue;
            }
            List<HistoryContents> contents = Galaxy_Instance.getHistoriesClient().showHistoryContents(history.getId());
            for (HistoryContents content : contents) {
                if (content.isDeleted()) {
                    continue;
                }
                Dataset lDs = Galaxy_Instance.getHistoriesClient().showDataset(history.getId(), content.getId());
                DataSet ds = new DataSet();
                ds.setName(lDs.getName());
                ds.setHistoryId(history.getId());
                ds.setGalaxyId(lDs.getId());

                if (lDs.getDataTypeExt().equalsIgnoreCase("fasta")) {
                    this.fastaFilesMap.put(ds.getGalaxyId(), ds);
                } else if (lDs.getDataTypeExt().equalsIgnoreCase("mgf")) {
                    this.mgfFilesMap.put(ds.getGalaxyId(), ds);
                }

            }
        };
        // check Fasta and MGF files need re-indexing
        List<HistoryContents> contents = Galaxy_Instance.getHistoriesClient().showHistoryContents(workingHistory.getId());
        for (HistoryContents content : contents) {
            if (content.isDeleted()) {
                continue;
            }
            Dataset ds = Galaxy_Instance.getHistoriesClient().showDataset(workingHistory.getId(), content.getId());

            if (fastaFilesMap.containsKey(ds.getName())) {
                fastaFilesMap.get(ds.getName()).setReIndexedHistoryId(workingHistory.getId());
                fastaFilesMap.get(ds.getName()).setReIndexedId(ds.getId());
            } else if (mgfFilesMap.containsKey(ds.getName())) {
                mgfFilesMap.get(ds.getName()).setReIndexedHistoryId(workingHistory.getId());
                mgfFilesMap.get(ds.getName()).setReIndexedId(ds.getId());

            } else if (ds.getDataTypeExt().equalsIgnoreCase("searchgui_archive")) {
                searchGUIFilesMap.put(ds.getId(), Galaxy_Instance.getHistoriesClient().showProvenance(workingHistory.getId(), content.getId()));
            } else if (ds.getDataTypeExt().equalsIgnoreCase("tabular") || ds.getDataTypeExt().equalsIgnoreCase("peptideshaker_archive")) {

                HistoryContentsProvenance prov = Galaxy_Instance.getHistoriesClient().showProvenance(workingHistory.getId(), content.getId());
                String jobId = prov.getJobId();
                if (!peptideShakerVisualizationMap.containsKey(jobId)) {
                    PeptideShakerVisualizationDataset vDs = new PeptideShakerVisualizationDataset(jobId);
                    peptideShakerVisualizationMap.put(jobId, vDs);
                }
                PeptideShakerVisualizationDataset vDs = peptideShakerVisualizationMap.get(jobId);
                if (ds.getName().endsWith("Protein Report")) {
                    vDs.setProteinFileId(ds.getId());
                } else if (ds.getName().endsWith("Peptide Report")) {
                    vDs.setPeptideFileId(ds.getId());
                } else if (ds.getName().endsWith("PSM Report")) {
                    vDs.setPsmFileId(ds.getId());
                } else if (ds.getDataTypeExt().endsWith("peptideshaker_archive")) {
                    vDs.setCpsId(ds.getId());
                }
                vDs.setSearchGUIFileId(prov.getParameters().get("searchgui_input").toString().split(",")[0].split("id=")[1]);

            }

        }

        //re-index un indexed fasta and mgf files
        for (String id : fastaFilesMap.keySet()) {
            if (fastaFilesMap.get(id).getReIndexedId() == null) {
                String reIndexId = this.reIndexFile(fastaFilesMap.get(id).getGalaxyId(), fastaFilesMap.get(id).getHistoryId(), workingHistory.getId());
                fastaFilesMap.get(id).setReIndexedHistoryId(workingHistory.getId());
                fastaFilesMap.get(id).setReIndexedId(reIndexId);
            }

        }
        for (String id : mgfFilesMap.keySet()) {
            if (mgfFilesMap.get(id).getReIndexedId() == null) {
                String reIndexId = this.reIndexFile(mgfFilesMap.get(id).getGalaxyId(), mgfFilesMap.get(id).getHistoryId(), workingHistory.getId());
                mgfFilesMap.get(id).setReIndexedHistoryId(workingHistory.getId());
                mgfFilesMap.get(id).setReIndexedId(reIndexId);
            }

        }
        for (PeptideShakerVisualizationDataset vDs : peptideShakerVisualizationMap.values()) {
            if (searchGUIFilesMap.containsKey(vDs.getSearchGUIFileId())) {
                Map<String, Object> params = searchGUIFilesMap.get(vDs.getSearchGUIFileId()).getParameters();
                vDs.setFastaFileId(fastaFilesMap.get(params.get("input_database").toString().split(",")[0].split("id=")[1]).getReIndexedId());
                int mgfFileNumber = params.keySet().toString().split("peak_lists").length - 2;
                for (int x = 1; x <= mgfFileNumber; x++) {
                    vDs.addMgfFiles(mgfFilesMap.get(params.get("peak_lists" + x).toString().split(",")[0].split("id=")[1]).getReIndexedId());
                }

            }
            System.out.println("at vds " + vDs.getJobId() + "   is valid " + vDs.isValidFile());

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

}
