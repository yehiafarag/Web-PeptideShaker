package com.uib.web.peptideshaker.presenter.components;

import com.compomics.util.experiment.identification.identification_parameters.SearchParameters;
import com.uib.web.peptideshaker.galaxy.DataSet;
import com.uib.web.peptideshaker.galaxy.GalaxyFile;
import com.uib.web.peptideshaker.presenter.core.DropDownList;
import com.uib.web.peptideshaker.presenter.core.MultiSelectOptionGroup;
import com.uib.web.peptideshaker.presenter.core.PopupWindow;
import com.vaadin.data.Property;
import com.vaadin.event.LayoutEvents;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.server.VaadinService;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
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
     * select MGF file list.
     */
    private final MultiSelectOptionGroup mgfFileList;
//    /**
//     * Create decoy database file list.
//     */
//    private final MultiSelectOptionGroup databaseOptionList;

    private final SearchSettingsLayout searchSettingsLayout;
    private final PopupWindow editSearchOption;
    private Map<String, GalaxyFile> searchSettingsMap;
    private SearchParameters searchParameters;

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

        searchSettingsFileList = new DropDownList("Search Settings (Select or Enter New Name)");
        content.addComponent(searchSettingsFileList);
        searchSettingsFileList.setFocous();

        HorizontalLayout btnsFrame = new HorizontalLayout();
        btnsFrame.setWidthUndefined();
        btnsFrame.setSpacing(true);
        btnsFrame.setStyleName("bottomformlayout");
        content.addComponent(btnsFrame);

        Label addNewSearchSettings = new Label("Add");
        addNewSearchSettings.addStyleName("windowtitle");
        btnsFrame.addLayoutClickListener(new LayoutEvents.LayoutClickListener() {
            @Override
            public void layoutClick(LayoutEvents.LayoutClickEvent event) {
                Component c = event.getClickedComponent();
                if (c != null && c instanceof Label && ((Label) c).getValue().equalsIgnoreCase("Add")) {
                    System.out.println("add new search");
                    String basepath = VaadinService.getCurrent().getBaseDirectory().getAbsolutePath();
                    File file = new File(basepath + "/VAADIN/default_searching.par");
                    SearchParameters searchParameters;
                    try {
                        searchParameters = SearchParameters.getIdentificationParameters(file);
                    } catch (IOException | ClassNotFoundException ex) {

                        ex.printStackTrace();
                        return;
                    }
                    searchParameters.setDefaultAdvancedSettings();
                    searchSettingsLayout.updateForms(searchParameters);
                    editSearchOption.setPopupVisible(true);
                }
            }
        });

        btnsFrame.addComponent(addNewSearchSettings);

        searchSettingsLayout = new SearchSettingsLayout() {
            @Override
            public void saveSearchingFile(SearchParameters searchParameters) {
                checkAndSaveSearchSettingsFile(searchParameters);
                editSearchOption.setPopupVisible(false);
            }

            @Override
            public void cancel() {
                editSearchOption.setPopupVisible(false);
            }

        };
        editSearchOption = new PopupWindow("Edit", searchSettingsLayout);
        editSearchOption.setSizeFull();
        editSearchOption.addStyleName("centerwindow");

        btnsFrame.addComponent(editSearchOption);

      

//        databaseOptionList = new MultiSelectOptionGroup("Database Options");
//        content.addComponent(databaseOptionList);
//        Map<String, String> paramMap = new LinkedHashMap<>();
//        paramMap.put("create_decoy", "Add Decoy Sequences");
//        paramMap.put("use_gene_mapping", "Use Gene Mappings (UniProt databases only)");
//        paramMap.put("update_gene_mapping", "Update gene mappings automatically from Ensembl (UniProt databases only)");
//
//        databaseOptionList.updateList(paramMap);
//        databaseOptionList.setSelectedValue("addDecoySequences");
//        databaseOptionList.setViewList(false);

        mgfFileList = new MultiSelectOptionGroup("Spectrum File(s)",false);
        content.addComponent(mgfFileList);
        mgfFileList.setRequired(true, "Select at least 1 MGF file");
        mgfFileList.setViewList(true);

        MultiSelectOptionGroup searchEngines = new MultiSelectOptionGroup("Search Engines",false);
        content.addComponent(searchEngines);
        searchEngines.setRequired(true, "Select at least 1 search engine");

        Map<String, String> searchEngienList = new LinkedHashMap<>();
        searchEngienList.put("X!Tandem", "X!Tandem");
        searchEngienList.put("MS-GF+", "MS-GF+");
        searchEngienList.put("OMSSA", "OMSSA");
        searchEngienList.put("Comet", "Comet");
        searchEngienList.put("Tide", "Tide");
        searchEngienList.put("MyriMatch", "MyriMatch");
        searchEngienList.put("MS_Amanda", "MS_Amanda");
        searchEngienList.put("DirecTag", "DirecTag");
        searchEngienList.put("Novor (Select for non-commercial use only)", "Novor (Select for non-commercial use only)");
        searchEngines.updateList(searchEngienList);
        searchEngines.setSelectedValue("X!Tandem");
        searchEngines.setSelectedValue("MS-GF+");
        searchEngines.setSelectedValue("OMSSA");
        HorizontalLayout bottomLayout = new HorizontalLayout();
        bottomLayout.setStyleName("bottomformlayout");
        bottomLayout.setSpacing(true);
        content.addComponent(bottomLayout);

        PopupWindow advancedSearchOption = new PopupWindow("Advanced Search Settings", new VerticalLayout());

        advancedSearchOption.setSizeFull();
        advancedSearchOption.addStyleName("centerwindow");
        bottomLayout.addComponent(advancedSearchOption);

        Button executeWorkFlow = new Button("Execute");
        executeWorkFlow.setStyleName(ValoTheme.BUTTON_SMALL);
        executeWorkFlow.addStyleName(ValoTheme.BUTTON_TINY);
        bottomLayout.addComponent(executeWorkFlow);

//        advancedSearchOption.addPopupVisibilityListener((PopupView.PopupVisibilityEvent event) -> {
//            if (event.isPopupVisible()) {
//                return;
//            }
//            if (!searchSettingsLayout.isValidForm()) {
//                advancedSearchOption.addStyleName("error");
//                executeWorkFlow.setEnabled(false);
//            } else {
//                System.out.println("at check the file and save it");
//                checkAndSaveSettingFile(advancedSearchOption.getData() + "");
//                advancedSearchOption.setData(null);
//                executeWorkFlow.setEnabled(true);
//
//            }
//        });
        executeWorkFlow.addClickListener((Button.ClickEvent event) -> {
            String fastFileId = searchSettingsLayout.getFataFileId();
            Set<String> spectrumIds = mgfFileList.getSelectedValue();
            Set<String> searchEnginesIds = searchEngines.getSelectedValue();
            if (fastFileId == null || spectrumIds == null || searchEnginesIds == null) {
                return;
            }
            Map<String, Boolean> otherSearchParameters = new HashMap<>();
//            for (String paramId : paramMap.keySet()) {
//                otherSearchParameters.put(paramId, databaseOptionList.getSelectedValue().contains(paramId));
//                if (databaseOptionList.getSelectedValue().contains(paramId)) {
//                    System.out.println("found selected " + paramId);
//                }
//            }
            for (String paramId : searchEngienList.keySet()) {
                otherSearchParameters.put(paramId, searchEngines.getSelectedValue().contains(paramId));
                if (searchEngines.getSelectedValue().contains(paramId)) {
                    System.out.println("found selected " + paramId);
                }
            }

            executeWorkFlow(fastFileId, spectrumIds, searchEnginesIds, searchParameters, otherSearchParameters);

        });
//        fastaFileList.setEnabled(false);
        mgfFileList.setEnabled(false);
        searchEngines.setEnabled(false);
        advancedSearchOption.setEnabled(false);
        editSearchOption.setEnabled(false);
        executeWorkFlow.setEnabled(false);
        searchSettingsFileList.addValueChangeListener((Property.ValueChangeEvent event) -> {
            if (searchSettingsFileList.getSelectedValue() != null) {
//                fastaFileList.setEnabled(true);
                mgfFileList.setEnabled(true);
                searchEngines.setEnabled(true);
                advancedSearchOption.setEnabled(true);
                editSearchOption.setEnabled(true);
                executeWorkFlow.setEnabled(true);
                File file = searchSettingsMap.get(searchSettingsFileList.getSelectedValue()).getFile();
                SearchParameters searchParameters;
                try {
                    searchParameters = SearchParameters.getIdentificationParameters(file);
                } catch (IOException | ClassNotFoundException ex) {
                    ex.printStackTrace();
                    return;
                }
                searchSettingsLayout.updateForms(searchParameters);

            }
        });
//        searchSettingsFileList.addNewItemHandler((String newItemCaption) -> {
//            for (GalaxyFile gf : searchSettingsMap.values()) {
//                if (gf.getDataset().getName().contains(newItemCaption)) {
//                    return;
//                }
//
//            }
//            searchSettingsFileList.addItem(newItemCaption);
//            editSearchOption.setData(newItemCaption);
//            String basepath = VaadinService.getCurrent().getBaseDirectory().getAbsolutePath();
//            File file = new File(basepath + "/VAADIN/default_searching.par");
//            SearchParameters searchParameters;
//            try {
//                searchParameters = SearchParameters.getIdentificationParameters(file);
//            } catch (IOException | ClassNotFoundException ex) {
//
//                ex.printStackTrace();
//                return;
//            }
//            searchParameters.setDefaultAdvancedSettings();
//            searchSettingsLayout.updateForms(searchParameters);
//
//            editSearchOption.setEnabled(true);
//            advancedSearchOption.setEnabled(true);
//            editSearchOption.setPopupVisible(true);
//            searchSettingsFileList.setSelected(newItemCaption);

//        }, "Add new settings name");
    }

    /**
     * Update the tools input forms
     *
     * @param searchSettingsMap search settings .par files map
     * @param fastaFilesMap FASTA files map
     * @param mgfFilesMap MGF file map
     */
    public void updateForm(Map<String, GalaxyFile> searchSettingsMap, Map<String, DataSet> fastaFilesMap, Map<String, DataSet> mgfFilesMap) {

        this.searchSettingsMap = searchSettingsMap;
        Map<String, String> searchSettingsFileIdToNameMap = new LinkedHashMap<>();
        Object selectedId = "";
        for (String id : searchSettingsMap.keySet()) {
            searchSettingsFileIdToNameMap.put(id, searchSettingsMap.get(id).getDataset().getName().replace(".par", ""));
            selectedId = id;
        }
        searchSettingsFileList.updateList(searchSettingsFileIdToNameMap);
        searchSettingsFileList.setSelected(selectedId);

       
        Map<String, String> mgfFileIdToNameMap = new LinkedHashMap<>();
        for (String id : mgfFilesMap.keySet()) {
            mgfFileIdToNameMap.put(id, mgfFilesMap.get(id).getName());
        }
        mgfFileList.updateList(mgfFileIdToNameMap);
        if (mgfFileIdToNameMap.size() == 1) {
            mgfFileList.setSelectedValue(mgfFileIdToNameMap.keySet());
        }
        searchSettingsLayout.updateFastaFileList(fastaFilesMap);

    }

    /**
     * Run Online Peptide-Shaker work-flow
     *
     * @param fastaFileId FASTA file dataset id
     * @param mgfIdsList list of MGF file dataset ids
     * @param searchEnginesList List of selected search engine names
     * @param historyId galaxy history id that will store the results
     */
    public abstract void executeWorkFlow(String fastaFileId, Set<String> mgfIdsList, Set<String> searchEnginesList, SearchParameters searchParameters, Map<String, Boolean> otherSearchParameters);

    private void checkAndSaveSearchSettingsFile(SearchParameters searchParameters) {
        this.searchParameters = searchParameters;
        searchSettingsMap = saveSearchGUIParameters(searchParameters, searchSettingsFileList.getSelectedValue());
        Map<String, String> searchSettingsFileIdToNameMap = new LinkedHashMap<>();
        String objectId = "";
        for (String id : searchSettingsMap.keySet()) {
            searchSettingsFileIdToNameMap.put(id, searchSettingsMap.get(id).getDataset().getName().replace(".par", ""));
            objectId = id;
        }
        searchSettingsFileList.updateList(searchSettingsFileIdToNameMap);
        searchSettingsFileList.setSelected(objectId);

    }

    /**
     * Save search settings file into galaxy
     *
     * @param fileName search parameters file name
     * @param searchParameters searchParameters .par file
     */
    public abstract Map<String, GalaxyFile> saveSearchGUIParameters(SearchParameters searchParameters, String fileName);
}
