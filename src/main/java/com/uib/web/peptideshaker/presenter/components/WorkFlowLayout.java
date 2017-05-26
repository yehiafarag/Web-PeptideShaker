package com.uib.web.peptideshaker.presenter.components;

import com.compomics.util.experiment.identification.identification_parameters.SearchParameters;
import com.uib.web.peptideshaker.galaxy.DataSet;
import com.uib.web.peptideshaker.presenter.core.DropDownList;
import com.uib.web.peptideshaker.presenter.core.MultiSelectOptionGroup;
import com.vaadin.data.Property;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.server.VaadinService;
import com.vaadin.ui.AbstractSelect;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.PopupView;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import java.io.File;
import java.io.IOException;
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
     * Search settings .par file drop-down list .
     */
    private final DropDownList searchSettingsFileList;
    /**
     * Select FASTA file drop-down list .
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

        searchSettingsFileList = new DropDownList("Search Settings");
        content.addComponent(searchSettingsFileList);

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
        SearchSettingsLayout searchSettingsLayout = new SearchSettingsLayout();

        PopupView advancedSearchOption = new PopupView("Edit Search Settings", searchSettingsLayout);

        advancedSearchOption.setSizeFull();
        advancedSearchOption.addStyleName("centerwindow");
        advancedSearchOption.setHideOnMouseOut(false);
        bottomLayout.addComponent(advancedSearchOption);

        Button executeWorkFlow = new Button("Execute");
        executeWorkFlow.setStyleName(ValoTheme.BUTTON_SMALL);
        executeWorkFlow.addStyleName(ValoTheme.BUTTON_TINY);
        bottomLayout.addComponent(executeWorkFlow);

        advancedSearchOption.addPopupVisibilityListener((PopupView.PopupVisibilityEvent event) -> {
            if (event.isPopupVisible()) {
                return;
            }
            if (!searchSettingsLayout.isValidForm()) {
                advancedSearchOption.addStyleName("error");
                executeWorkFlow.setEnabled(false);
            } else {
                System.out.println("at check the file and save it");
                checkAndSaveSettingFile(advancedSearchOption.getData() + "");
                advancedSearchOption.setData(null);
                executeWorkFlow.setEnabled(true);

            }
        });

        executeWorkFlow.addClickListener((Button.ClickEvent event) -> {
            String fastFileId = fastaFileList.getSelectedValue();
            Set<String> spectrumIds = mgfFileList.getSelectedValue();
            Set<String> searchEnginesIds = searchEngines.getSelectedValue();
            if (fastFileId == null || spectrumIds == null || searchEnginesIds == null) {
                return;
            }
            executeWorkFlow(fastFileId, spectrumIds, searchEnginesIds);

        });
        fastaFileList.setEnabled(false);
        mgfFileList.setEnabled(false);
        searchEngines.setEnabled(false);
        advancedSearchOption.setEnabled(false);
        executeWorkFlow.setEnabled(false);
        searchSettingsFileList.addValueChangeListener((Property.ValueChangeEvent event) -> {
            if (searchSettingsFileList.getSelectedValue() != null) {
                fastaFileList.setEnabled(true);
                mgfFileList.setEnabled(true);
                searchEngines.setEnabled(true);
                advancedSearchOption.setEnabled(true);
                executeWorkFlow.setEnabled(true);
                String basepath = VaadinService.getCurrent().getBaseDirectory().getAbsolutePath();
                File file = new File(basepath + "/VAADIN/default_searching.par");
                SearchParameters searchParam = null;
                try {
                    searchParam = SearchParameters.getIdentificationParameters(file);
                } catch (IOException | ClassNotFoundException ex) {
                    ex.printStackTrace();
                }
                searchSettingsLayout.updateForms(searchParam);

            }
        });
        searchSettingsFileList.addNewItemHandler((String newItemCaption) -> {
            searchSettingsFileList.addItem(newItemCaption);
            advancedSearchOption.setEnabled(true);
            advancedSearchOption.setPopupVisible(true);
            advancedSearchOption.setData(newItemCaption);
        }, "Add new settings name");

    }
    private Map<String, DataSet> searchSettingsMap;

    /**
     * Update the tools input forms
     *
     * @param searchSettingsMap search settings .par files map
     * @param fastaFilesMap FASTA files map
     * @param mgfFilesMap MGF file map
     */
    public void updateForm(Map<String, DataSet> searchSettingsMap, Map<String, DataSet> fastaFilesMap, Map<String, DataSet> mgfFilesMap) {

        this.searchSettingsMap = searchSettingsMap;
        Map<String, String> searchSettingsFileIdToNameMap = new LinkedHashMap<>();
        for (String id : searchSettingsMap.keySet()) {
            searchSettingsFileIdToNameMap.put(id, searchSettingsMap.get(id).getName().replace(".par", ""));
        }
        searchSettingsFileList.updateList(searchSettingsFileIdToNameMap);

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

    private void checkAndSaveSettingFile(String settingId) {
        if (searchSettingsMap != null && !searchSettingsMap.containsKey(settingId)) {
            System.out.println("at bing lets save it :-D ");
        }

    }

}
