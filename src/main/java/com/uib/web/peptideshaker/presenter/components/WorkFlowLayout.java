package com.uib.web.peptideshaker.presenter.components;

import com.uib.web.peptideshaker.galaxy.DataSet;
import com.uib.web.peptideshaker.presenter.core.DropDownList;
import com.uib.web.peptideshaker.presenter.core.MultiSelectOptionGroup;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.PopupView;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * This class represents SearchGUI-Peptide-Shaker work-flow which include input
 * form
 *
 * @author Yehia Farag
 */
public abstract class WorkFlowLayout extends Panel {

    /**
     * Select fasta file dropdown list .
     */
    private final DropDownList fastaFileList;
    /**
     * select MGF file list.
     */
    private final MultiSelectOptionGroup mgfFileList;

    /**
     * Constructor to initialize the main attributes.
     */
    public WorkFlowLayout() {
        WorkFlowLayout.this.setWidth(100, Unit.PERCENTAGE);
        WorkFlowLayout.this.setHeight(100, Unit.PERCENTAGE);

        VerticalLayout content = new VerticalLayout();
        content.setHeightUndefined();
        content.setWidth(100, Unit.PERCENTAGE);
        WorkFlowLayout.this.setContent(content);
        WorkFlowLayout.this.setStyleName("subframe");

        content.setSpacing(true);

        Label titleLabel = new Label("SearchGUI-PeptideShaker-WorkFlow");
        titleLabel.setStyleName("frametitle");
        content.addComponent(titleLabel);

        fastaFileList = new DropDownList("Protein Database (FASTA)");
        content.addComponent(fastaFileList);
        fastaFileList.setRequired(true, "Select FASTA file");

        mgfFileList = new MultiSelectOptionGroup("Spectrum File(s)");
        content.addComponent(mgfFileList);
        mgfFileList.setRequired(true, "Select at least 1 MGF file");

        MultiSelectOptionGroup searchEngines = new MultiSelectOptionGroup("Search Engines");
        content.addComponent(searchEngines);
        searchEngines.setRequired(true, "Select at least 1 search engine");

        Map<String, String> searchEngienList = new LinkedHashMap<>();
        searchEngienList.put("X!Tandem", "X!Tandem");
        searchEngienList.put("MS-GF+", "MS-GF+");
        searchEngienList.put("OMSSA", "OMSSA");
        searchEngienList.put("Comet", "Comet");
        searchEngines.updateList(searchEngienList);

        HorizontalLayout bottomLayout = new HorizontalLayout();
        bottomLayout.setStyleName("bottomformlayout");
        bottomLayout.setSpacing(true);
        content.addComponent(bottomLayout);
        SearchSettingsLayout searchSettingsLayout=new SearchSettingsLayout();
        
        PopupView advancedSearchOption = new PopupView("Search Settings", searchSettingsLayout);
        advancedSearchOption.setSizeFull();
        advancedSearchOption.setStyleName("centerwindow");
        advancedSearchOption.setHideOnMouseOut(false);
        bottomLayout.addComponent(advancedSearchOption);

        Button executeWorkFlow = new Button("Execute");
        executeWorkFlow.setStyleName(ValoTheme.BUTTON_SMALL);
        executeWorkFlow.addStyleName(ValoTheme.BUTTON_TINY);
        bottomLayout.addComponent(executeWorkFlow);

        executeWorkFlow.addClickListener((Button.ClickEvent event) -> {
            String fastFileId = fastaFileList.getSelectedValue();
            Set<String> spectrumIds = mgfFileList.getSelectedValue();
            Set<String> searchEnginesIds = searchEngines.getSelectedValue();
            if (fastFileId == null || spectrumIds == null || searchEnginesIds == null) {
                return;
            }
            executeWorkFlow(fastFileId,spectrumIds,searchEnginesIds);
            

        });

    }

    /**
     * Update the tools input forms
     *
     * @param fastaFilesMap fasta files map
     * @param mgfFilesMap MGF file map
     */
    public void updateForm(Map<String, DataSet> fastaFilesMap, Map<String, DataSet> mgfFilesMap) {
        Map<String, String> fastaFileIdToNameMap = new LinkedHashMap<>();
        for (String id : fastaFilesMap.keySet()) {
            fastaFileIdToNameMap.put(id, fastaFilesMap.get(id).getName());
        }
        fastaFileList.updateList(fastaFileIdToNameMap);
        Map<String, String> mgfFileIdToNameMap = new LinkedHashMap<>();
        for (String id : mgfFilesMap.keySet()) {
            mgfFileIdToNameMap.put(id, mgfFilesMap.get(id).getName());
        }
        mgfFileList.updateList(mgfFileIdToNameMap);

    }
     /**
     * Run Online Peptide-Shaker work-flow
     *
     * @param fastaFileId FASTA file dataset id
     * @param mgfIdsList list of MGF file dataset ids
     * @param searchEnginesList List of selected search engine names
     * @param historyId galaxy history id that will store the results
     */
    public abstract void executeWorkFlow(String fastaFileId, Set<String> mgfIdsList, Set<String> searchEnginesList);

}
