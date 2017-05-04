package com.uib.web.peptideshaker.galaxy;

import com.github.jmchilton.blend4j.galaxy.GalaxyInstance;
import com.github.jmchilton.blend4j.galaxy.beans.Dataset;
import com.github.jmchilton.blend4j.galaxy.beans.History;
import com.github.jmchilton.blend4j.galaxy.beans.HistoryContents;
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
public class HistoryHandler {

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
     * Constructor to initialize the main data structure and other variables.
     *
     * @param Galaxy_Instance the main Galaxy instance in the system
     *
     */
    public HistoryHandler(GalaxyInstance Galaxy_Instance) {
        this.Galaxy_Instance = Galaxy_Instance;
        this.fastaFilesMap = new LinkedHashMap<>();
        this.mgfFilesMap = new LinkedHashMap<>();
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

    private void updateHistoryDatastructure() {
        fastaFilesMap.clear();
        mgfFilesMap.clear();
        List<History> historiesList = Galaxy_Instance.getHistoriesClient().getHistories();
        History workingHistory = null;
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

            }
        }

        //re-index un indexed fasta and mgf files
        for (String id : fastaFilesMap.keySet()) {
            if (fastaFilesMap.get(id).getReIndexedId() == null) {
                String reIndexId = this.reIndexFile(fastaFilesMap.get(id));
                fastaFilesMap.get(id).setReIndexedHistoryId(workingHistory.getId());
                fastaFilesMap.get(id).setReIndexedId(reIndexId);
            }

        }
        for (String id : mgfFilesMap.keySet()) {
            if (mgfFilesMap.get(id).getReIndexedId() == null) {
                String reIndexId = this.reIndexFile(mgfFilesMap.get(id));
                mgfFilesMap.get(id).setReIndexedHistoryId(workingHistory.getId());
                mgfFilesMap.get(id).setReIndexedId(reIndexId);
            }

        }

    }

    private String reIndexFile(DataSet ds) {
        System.out.println("at in progress to implement re-index files "+ ds.getName());
        return null;
    }

}
