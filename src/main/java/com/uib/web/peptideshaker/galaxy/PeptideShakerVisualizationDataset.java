package com.uib.web.peptideshaker.galaxy;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * This class represents dataset visualization that store data for viewing files
 * on web
 *
 * @author Yehia Farag
 */
public class PeptideShakerVisualizationDataset extends SystemDataSet{

    private String proteinFileId;
    private String peptideFileId;
    private String cpsId;
    private String psmFileId;
    private final String jobId;
    private String searchGUIFileId;
    private final Set<String> mgfFiles;
    private String fastaFileId;
    private String fastaFileIndex;
    private final Map<String, String> mgfFilesIndexes;

    public String getFastaFileIndex() {
        return fastaFileIndex;
    }

    public void setFastaFileIndex(String fastaFileIndex) {
        this.fastaFileIndex = fastaFileIndex;
    }

    public PeptideShakerVisualizationDataset(String jobId) {
        this.jobId = jobId;
        this.mgfFiles = new LinkedHashSet<>();
        mgfFilesIndexes = new LinkedHashMap<>();
    }

    public String getJobId() {
        return jobId;
    }

    public String getProteinFileId() {
        return proteinFileId;
    }

    public void setProteinFileId(String proteinFileId) {
        this.proteinFileId = proteinFileId;
    }

    public String getPeptideFileId() {
        return peptideFileId;
    }

    public void setPeptideFileId(String peptideFileId) {
        this.peptideFileId = peptideFileId;
    }

    public String getCpsId() {
        return cpsId;
    }

    public void setCpsId(String cpsId) {
        this.cpsId = cpsId;
    }

    public boolean isValidFile() {
        System.out.println((proteinFileId == null) +"||"+ (peptideFileId == null) +"||"+ (searchGUIFileId == null )+"||"+ (psmFileId == null )+"||"+ (mgfFiles.isEmpty() )+"||"+ (fastaFileId == null) +"||"+ (fastaFileIndex == null) +"||"+ (mgfFilesIndexes.isEmpty()));
        
        return !(proteinFileId == null || peptideFileId == null || searchGUIFileId == null || psmFileId == null || mgfFiles.isEmpty() || fastaFileId == null || fastaFileIndex == null || mgfFilesIndexes.isEmpty());
    }

    public String getPsmFileId() {
        return psmFileId;
    }

    public void setPsmFileId(String psmFileId) {
        this.psmFileId = psmFileId;
    }

    public String getSearchGUIFileId() {
        return searchGUIFileId;
    }

    public void setSearchGUIFileId(String searchGUIFileId) {
        this.searchGUIFileId = searchGUIFileId;
    }

    public Set<String> getMgfFiles() {
        return mgfFiles;
    }

    public void addMgfFiles(String mgfFileID) {
        this.mgfFiles.add(mgfFileID);
    }

    public String getFastaFileId() {
        return fastaFileId;
    }

    public void setFastaFileId(String fastaFileId) {
        this.fastaFileId = fastaFileId;
    }

    public void addMGFFileIndex(String mgfFileId, String mgfFileIndex) {
        mgfFilesIndexes.put(mgfFileId, mgfFileIndex);
    }

}
