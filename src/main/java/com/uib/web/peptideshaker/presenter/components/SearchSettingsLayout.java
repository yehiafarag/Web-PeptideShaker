package com.uib.web.peptideshaker.presenter.components;

import com.compomics.util.experiment.biology.Enzyme;
import com.compomics.util.experiment.biology.EnzymeFactory;
import com.compomics.util.experiment.biology.PTMFactory;
import com.compomics.util.experiment.identification.identification_parameters.SearchParameters;
import com.compomics.util.experiment.identification.protein_sequences.SequenceFactory;
import com.uib.web.peptideshaker.presenter.core.CloseButton;
import com.uib.web.peptideshaker.presenter.core.form.ColorLabel;
import com.uib.web.peptideshaker.presenter.core.form.HorizontalLabel2DropdownList;
import com.uib.web.peptideshaker.presenter.core.form.HorizontalLabelDropDounList;
import com.uib.web.peptideshaker.presenter.core.form.HorizontalLabelTextField;
import com.uib.web.peptideshaker.presenter.core.form.HorizontalLabelTextFieldDropdownList;
import com.uib.web.peptideshaker.presenter.core.form.SparkLine;
import com.vaadin.data.Property;
import com.vaadin.data.validator.DoubleRangeValidator;
import com.vaadin.data.validator.IntegerRangeValidator;
import com.vaadin.event.LayoutEvents;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.PopupView;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * This class represents the search settings form layout
 *
 * @author Yehia Farag
 */
public class SearchSettingsLayout extends VerticalLayout {

    private final SearchParameters searchParam;
    /**
     * The sequence factory.
     */
    private SequenceFactory sequenceFactory = SequenceFactory.getInstance();
    /**
     * The enzyme factory.
     */
    private EnzymeFactory enzymeFactory = EnzymeFactory.getInstance();
    /**
     * Convenience array for forward ion type selection.
     */
    private String[] forwardIons = {"a", "b", "c"};
    /**
     * Convenience array for rewind ion type selection.
     */
    private String[] rewindIons = {"x", "y", "z"};
    /**
     * The post translational modifications factory.
     */
    private PTMFactory PTM = PTMFactory.getInstance();

    private final Set<String> commonModificationIds;

    /**
     * Constructor to initialize the main setting parameters
     */
    public SearchSettingsLayout(SearchParameters searchParam) {
        SearchSettingsLayout.this.setMargin(true);
        SearchSettingsLayout.this.setSizeUndefined();
        SearchSettingsLayout.this.setSpacing(true);
        HorizontalLayout titleLayout = new HorizontalLayout();
        titleLayout.setSizeFull();

        this.commonModificationIds = new HashSet<>();
        String mod = "Acetylation of K//Acetylation of protein N-term//Carbamidomethylation of C//Oxidation of M//Phosphorylation of S//Phosphorylation of T//Phosphorylation of Y//Arginine 13C6//Lysine 13C6//iTRAQ 4-plex of peptide N-term//iTRAQ 4-plex of K//iTRAQ 4-plex of Y//iTRAQ 8-plex of peptide N-term//iTRAQ 8-plex of K//iTRAQ 8-plex of Y//TMT 6-plex of peptide N-term//TMT 6-plex of K//TMT 10-plex of peptide N-term//TMT 10-plex of K//Pyrolidone from E//Pyrolidone from Q//Pyrolidone from carbamidomethylated C//Deamidation of N//Deamidation of Q";
        commonModificationIds.addAll(Arrays.asList(mod.split("//")));
        this.searchParam = searchParam;
        SearchSettingsLayout.this.addComponent(titleLayout);
        Label setteingsLabel = new Label("Search Settings");
        titleLayout.addComponent(setteingsLabel);

        CloseButton closeBtn = new CloseButton();
        titleLayout.addComponent(closeBtn);
        titleLayout.setComponentAlignment(closeBtn, Alignment.TOP_RIGHT);
        closeBtn.addLayoutClickListener((LayoutEvents.LayoutClickEvent event) -> {
            ((PopupView) SearchSettingsLayout.this.getParent()).setPopupVisible(false);
        });

        HorizontalLayout modificationContainer = inititModificationLayout();
        SearchSettingsLayout.this.addComponent(modificationContainer);

        GridLayout proteaseFragmentationContainer = inititProteaseFragmentationLayout();
        SearchSettingsLayout.this.addComponent(proteaseFragmentationContainer);

    }

    private HorizontalLayout inititModificationLayout() {
        HorizontalLayout modificationContainer = new HorizontalLayout();
        modificationContainer.setStyleName("panelframe");
        modificationContainer.setSizeFull();
        modificationContainer.setMargin(new MarginInfo(false, false, false, false));
        modificationContainer.setWidth(700, Unit.PIXELS);
        modificationContainer.setHeight(350, Unit.PIXELS);

        VerticalLayout leftSideLayout = new VerticalLayout();
        leftSideLayout.setSizeFull();
        leftSideLayout.setSpacing(true);
        leftSideLayout.setMargin(new MarginInfo(false, false, false, false));
        modificationContainer.addComponent(leftSideLayout);
        modificationContainer.setExpandRatio(leftSideLayout, 45);

        Label modificationLabel = new Label("Modifications");
        modificationLabel.setSizeFull();
        leftSideLayout.addComponent(modificationLabel);
        leftSideLayout.setExpandRatio(modificationLabel, 4);

        HorizontalLayout leftTopLayout = new HorizontalLayout();
        leftTopLayout.setSizeFull();
        leftTopLayout.setSpacing(true);
        leftSideLayout.addComponent(leftTopLayout);
        leftSideLayout.setExpandRatio(leftTopLayout, 48);

        Table fixedModificationTable = initModificationTable("Fixed Modifications");
        leftTopLayout.addComponent(fixedModificationTable);
        HorizontalLayout leftBottomLayout = new HorizontalLayout();
        leftBottomLayout.setSizeFull();
        leftBottomLayout.setSpacing(true);
        leftSideLayout.addComponent(leftBottomLayout);
        leftSideLayout.setExpandRatio(leftBottomLayout, 48);

        Table variableModificationTable = initModificationTable("Variable Modifications");
        leftBottomLayout.addComponent(variableModificationTable);
        leftBottomLayout.setExpandRatio(variableModificationTable, 80);

        VerticalLayout middleSideLayout = new VerticalLayout();
        middleSideLayout.setSizeFull();
        modificationContainer.addComponent(middleSideLayout);
        modificationContainer.setExpandRatio(middleSideLayout, 10);

        VerticalLayout spacer = new VerticalLayout();
        spacer.setSizeFull();
        middleSideLayout.addComponent(spacer);
        middleSideLayout.setExpandRatio(spacer, 4);

        VerticalLayout sideTopButtons = new VerticalLayout();
        sideTopButtons.setSizeUndefined();
        middleSideLayout.addComponent(sideTopButtons);
        middleSideLayout.setComponentAlignment(sideTopButtons, Alignment.BOTTOM_CENTER);
        middleSideLayout.setExpandRatio(sideTopButtons, 48);

        Button toFixedModBtn = new Button(VaadinIcons.ARROW_LEFT);
        toFixedModBtn.setStyleName(ValoTheme.BUTTON_ICON_ONLY);
        sideTopButtons.addComponent(toFixedModBtn);
        sideTopButtons.setComponentAlignment(toFixedModBtn, Alignment.BOTTOM_CENTER);

        Button fromFixedModBtn = new Button(VaadinIcons.ARROW_RIGHT);
        fromFixedModBtn.setStyleName(ValoTheme.BUTTON_ICON_ONLY);
        sideTopButtons.addComponent(fromFixedModBtn);
        sideTopButtons.setComponentAlignment(fromFixedModBtn, Alignment.MIDDLE_CENTER);

        VerticalLayout sideBottomButtons = new VerticalLayout();
        middleSideLayout.addComponent(sideBottomButtons);
        middleSideLayout.setComponentAlignment(sideBottomButtons, Alignment.BOTTOM_CENTER);
        middleSideLayout.setExpandRatio(sideBottomButtons, 48);

        Button toVariableModBtn = new Button(VaadinIcons.ARROW_LEFT);
        toVariableModBtn.setStyleName(ValoTheme.BUTTON_ICON_ONLY);
        sideBottomButtons.addComponent(toVariableModBtn);
        sideBottomButtons.setComponentAlignment(toVariableModBtn, Alignment.MIDDLE_CENTER);

        Button fromVariableModBtn = new Button(VaadinIcons.ARROW_RIGHT);
        fromVariableModBtn.setStyleName(ValoTheme.BUTTON_ICON_ONLY);
        sideBottomButtons.addComponent(fromVariableModBtn);
        sideBottomButtons.setComponentAlignment(fromVariableModBtn, Alignment.MIDDLE_CENTER);

        VerticalLayout rightSideLayout = new VerticalLayout();
        rightSideLayout.setSizeFull();
        rightSideLayout.setSpacing(true);
        modificationContainer.addComponent(rightSideLayout);
        modificationContainer.setExpandRatio(rightSideLayout, 45);

        ComboBox modificationListControl = new ComboBox();
        modificationListControl.setWidth(100, Unit.PERCENTAGE);
        modificationListControl.setHeight(30, Unit.PIXELS);
        modificationListControl.setStyleName(ValoTheme.COMBOBOX_SMALL);
        modificationListControl.addStyleName(ValoTheme.COMBOBOX_TINY);
        modificationListControl.setNullSelectionAllowed(false);
        modificationListControl.addItem("Most Used Modifications");
        modificationListControl.addItem("All Modifications");
        modificationListControl.setValue("Most Used Modifications");
        rightSideLayout.addComponent(modificationListControl);
        rightSideLayout.setExpandRatio(modificationListControl, 4);
        rightSideLayout.setComponentAlignment(modificationListControl, Alignment.MIDDLE_CENTER);

        Table mostUsedModificationsTable = initModificationTable("");
        Map<Object, Object[]> completeModificationItems = new LinkedHashMap<>();
        
        List<String> allModiList = PTM.getDefaultModifications();
         // get the min and max values for the mass sparklines
        double maxMass = Double.MIN_VALUE;
        double minMass = Double.MAX_VALUE;

        for (String ptm : PTM.getPTMs()) {
            if (PTM.getPTM(ptm).getMass() > maxMass) {
                maxMass = PTM.getPTM(ptm).getMass();
            }
            if (PTM.getPTM(ptm).getMass() < minMass) {
                minMass = PTM.getPTM(ptm).getMass();
            }
        }
        
        System.out.println("at all ------------------ param " + allModiList + "   ");

//        for (int x = 0; x < allModiList.size(); x++) {
//            Object[] modificationArr = new Object[]{1 + x, allModiList.get(x), PTM.getPTM(allModiList.get(x)).getMass()};
//            mostUseModificationItems.put(x, modificationArr);
//            
////            completeModificationItems.put(x, modificationArr);
//        }
        for (int x = 0; x < allModiList.size(); x++) {
            ColorLabel color = new ColorLabel(PTM.getColor(allModiList.get(x)));
            SparkLine sLine = new SparkLine(PTM.getPTM(allModiList.get(x)).getMass(), minMass, maxMass);
            Object[] modificationArr = new Object[]{color, allModiList.get(x),sLine };
//            System.out.println("at ptm info     "+modificationArr[1]+" -- "+ PTM.get(modificationArr[1].toString()).g);
            completeModificationItems.put(allModiList.get(x), modificationArr);
        }
//PTM.getPTM(allModiList.get(x)).getHtmlTooltip()
        rightSideLayout.addComponent(mostUsedModificationsTable);
        for (Object id : completeModificationItems.keySet()) {
            if (commonModificationIds.contains(id.toString())) {
                mostUsedModificationsTable.addItem(completeModificationItems.get(id), id);
            }
        }
        mostUsedModificationsTable.setVisible(false);
        rightSideLayout.setExpandRatio(mostUsedModificationsTable, 96);
        Table allModificationsTable = initModificationTable("");
        rightSideLayout.addComponent(allModificationsTable);
        rightSideLayout.setExpandRatio(allModificationsTable, 96);
        allModificationsTable.setVisible(false);

        modificationListControl.addValueChangeListener((Property.ValueChangeEvent event) -> {
            allModificationsTable.removeAllItems();
            mostUsedModificationsTable.removeAllItems();
            if (modificationListControl.getValue().toString().equalsIgnoreCase("All Modifications")) {                
                for (Object id : completeModificationItems.keySet()) {
                    if (fixedModificationTable.containsId(id) || variableModificationTable.containsId(id)) {
                        continue;
                    }
                    allModificationsTable.addItem(completeModificationItems.get(id), id);
                }
                allModificationsTable.setVisible(true);
                mostUsedModificationsTable.setVisible(false);

            } else {                
                for (Object id : completeModificationItems.keySet()) {
                    if (fixedModificationTable.containsId(id) || variableModificationTable.containsId(id)) {
                        continue;
                    }
                    if (commonModificationIds.contains(id.toString())) {
                        mostUsedModificationsTable.addItem(completeModificationItems.get(id), id);
                    }
                }

                allModificationsTable.setVisible(false);
                mostUsedModificationsTable.setVisible(true);
            }
            allModificationsTable.sort(new Object[]{"name"}, new boolean[]{true});
            mostUsedModificationsTable.sort(new Object[]{"name"}, new boolean[]{true});
        });
        toFixedModBtn.addClickListener((Button.ClickEvent event) -> {
            Table selectionTable;
            if (allModificationsTable.isVisible()) {
                selectionTable = allModificationsTable;
            } else {
                selectionTable = mostUsedModificationsTable;
            }
            Set<Object> selection = ((Set<Object>) selectionTable.getValue());
            for (Object id : selection) {
                selectionTable.removeItem(id);
                fixedModificationTable.addItem(completeModificationItems.get(id), id);
                
            }
            fixedModificationTable.sort(new Object[]{"name"}, new boolean[]{true});

        });
        fromFixedModBtn.addClickListener((Button.ClickEvent event) -> {
            Table selectionTable;
            if (allModificationsTable.isVisible()) {
                selectionTable = allModificationsTable;
            } else {
                selectionTable = mostUsedModificationsTable;
            }
            Set<Object> selection = ((Set<Object>) fixedModificationTable.getValue());
            for (Object id : selection) {
                fixedModificationTable.removeItem(id);
                selectionTable.addItem(completeModificationItems.get(id), id);
                
            }
            selectionTable.sort(new Object[]{"name"}, new boolean[]{true});

        });
        toVariableModBtn.addClickListener((Button.ClickEvent event) -> {
            Table selectionTable;
            if (allModificationsTable.isVisible()) {
                selectionTable = allModificationsTable;
            } else {
                selectionTable = mostUsedModificationsTable;
            }
            Set<Object> selection = ((Set<Object>) selectionTable.getValue());
            for (Object id : selection) {
                selectionTable.removeItem(id);
                variableModificationTable.addItem(completeModificationItems.get(id), id);
                
            }
            variableModificationTable.sort(new Object[]{"name"}, new boolean[]{true});

        });
        fromVariableModBtn.addClickListener((Button.ClickEvent event) -> {
            Table selectionTable;
            if (allModificationsTable.isVisible()) {
                selectionTable = allModificationsTable;
            } else {
                selectionTable = mostUsedModificationsTable;
            }
            Set<Object> selection = ((Set<Object>) variableModificationTable.getValue());
            for (Object id : selection) {
                variableModificationTable.removeItem(id);
                selectionTable.addItem(completeModificationItems.get(id), id);
                
            }
            selectionTable.sort(new Object[]{"name"}, new boolean[]{true});

        });
        mostUsedModificationsTable.setVisible(true);

        return modificationContainer;

    }

    private Table initModificationTable(String cap) {
        Table modificationsTable = new Table(cap){
           DecimalFormat df= new DecimalFormat("#.##");
           @Override
        protected String formatPropertyValue(Object rowId, Object colId,Property property) {
                            Object v = property.getValue();
                            if (v instanceof Double) {                             
                                return df.format(v);
                            }
                            return super.formatPropertyValue(rowId, colId, property);
                        }
            
        };
        modificationsTable.setSizeFull();
        modificationsTable.setStyleName(ValoTheme.TABLE_SMALL);
        modificationsTable.addStyleName(ValoTheme.TABLE_COMPACT);
        modificationsTable.addStyleName(ValoTheme.TABLE_NO_VERTICAL_LINES);
        modificationsTable.addStyleName("smalltable");
        modificationsTable.setMultiSelect(true);
        modificationsTable.setSelectable(true);
        modificationsTable.addContainerProperty("color", ColorLabel.class, null, "", null, Table.Align.CENTER);
        modificationsTable.addContainerProperty("name", String.class, null, "Name", null, Table.Align.LEFT);
        modificationsTable.addContainerProperty("mass", SparkLine.class, null, "Mass", null, Table.Align.LEFT);
        modificationsTable.setColumnExpandRatio("color", 10);
        modificationsTable.setColumnExpandRatio("name", 55);
        modificationsTable.setColumnExpandRatio("mass", 35);
        modificationsTable.sort(new Object[]{"name"}, new boolean[]{true});
        modificationsTable.setSortEnabled(false);
        modificationsTable.setItemDescriptionGenerator((Component source, Object itemId, Object propertyId) -> PTM.getPTM(itemId.toString()).getHtmlTooltip());
                

        return modificationsTable;
    }

    private HorizontalLabelDropDounList digestionList;
    private HorizontalLabelDropDounList enzymeList;
    private HorizontalLabelDropDounList specificityList;
    private HorizontalLabelTextField maxMissCleav;
    private HorizontalLabel2DropdownList fragmentIonTypes;
    private HorizontalLabelTextFieldDropdownList precursorTolerance;
    private HorizontalLabelTextFieldDropdownList fragmentTolerance;

    private GridLayout inititProteaseFragmentationLayout() {
        GridLayout proteaseFragmentationContainer = new GridLayout(2, 6);
        proteaseFragmentationContainer.setStyleName("panelframe");
        proteaseFragmentationContainer.setColumnExpandRatio(0, 50);
        proteaseFragmentationContainer.setColumnExpandRatio(1, 50);
        proteaseFragmentationContainer.setMargin(new MarginInfo(false, false, true, false));
        proteaseFragmentationContainer.setWidth(700, Unit.PIXELS);
        proteaseFragmentationContainer.setHeight(220, Unit.PIXELS);
        proteaseFragmentationContainer.setSpacing(true);

        Label label = new Label("Protease & Fragmentation");
        label.setSizeFull();
        proteaseFragmentationContainer.addComponent(label, 0, 0);

        Set<String> digestionOptionList = new LinkedHashSet<>();
        digestionOptionList.add("Enzyme");
        digestionOptionList.add("Unspecific");
        digestionOptionList.add("Whole Protein");

        digestionList = new HorizontalLabelDropDounList("Digestion", digestionOptionList);
        digestionList.addValueChangeListener((Property.ValueChangeEvent event) -> {
            if (digestionList.getSelectedValue().equalsIgnoreCase("Enzyme")) {
                enzymeList.setEnabled(true);
                specificityList.setEnabled(true);
                maxMissCleav.setEnabled(true);
            } else if (digestionList.getSelectedValue().equalsIgnoreCase("Whole Protein")) {
                maxMissCleav.setEnabled(false);
                enzymeList.setEnabled(false);
                specificityList.setEnabled(false);

            } else {
                enzymeList.setEnabled(false);
                specificityList.setEnabled(false);
                maxMissCleav.setEnabled(true);
            }

        });
        

        Set<String> enzList = new LinkedHashSet<>();
        Enzyme enzyme = searchParam.getEnzyme();
        if (enzyme != null) {
            String enzymeName = enzyme.getName();

            if (!enzymeFactory.enzymeLoaded(enzymeName)) {
                enzymeFactory.addEnzyme(searchParam.getEnzyme());
            }
            enzList.add(enzymeName);
        }
        List<Enzyme> enzObjList = enzymeFactory.getEnzymes();
        for (Enzyme enz : enzObjList) {
            enzList.add(enz.getName());
        }

        enzymeList = new HorizontalLabelDropDounList("Enzyme", enzList);

        Set<String> specificityOptionList = new LinkedHashSet<>();
        specificityOptionList.add("Specific");
        specificityOptionList.add("Semi-Specific");
        specificityOptionList.add("N-term Specific");
        specificityOptionList.add("C-term Specific");

        specificityList = new HorizontalLabelDropDounList("Specificity", specificityOptionList);       
        maxMissCleav = new HorizontalLabelTextField("Max Missed Cleavages", 2, new IntegerRangeValidator("Error Value", Integer.MIN_VALUE, Integer.MAX_VALUE));

        Set<String> ionListI = new LinkedHashSet<>();
        ionListI.add("a");
        ionListI.add("b");
        ionListI.add("c");
        Set<String> ionListII = new LinkedHashSet<>();
        ionListII.add("x");
        ionListII.add("y");
        ionListII.add("z");
        fragmentIonTypes = new HorizontalLabel2DropdownList("Fragment Ion Types", ionListI, ionListII);

         
        
        proteaseFragmentationContainer.addComponent(digestionList, 0, 1);
        proteaseFragmentationContainer.addComponent(enzymeList, 0, 2);
        proteaseFragmentationContainer.addComponent(specificityList, 0, 3);

        proteaseFragmentationContainer.addComponent(maxMissCleav, 0, 4);
        proteaseFragmentationContainer.addComponent(fragmentIonTypes, 0, 5);

        Set<String> mzToleranceList = new LinkedHashSet<>();
        mzToleranceList.add("ppm");
        mzToleranceList.add("Da");
        precursorTolerance = new HorizontalLabelTextFieldDropdownList("Precursor m/z Tolerance", 10.0, mzToleranceList, new DoubleRangeValidator("Error Value", Double.MIN_VALUE, Double.MAX_VALUE));
        fragmentTolerance = new HorizontalLabelTextFieldDropdownList("Fragment m/z Tolerance", 0.5, mzToleranceList, new DoubleRangeValidator("Error Value", Double.MIN_VALUE, Double.MAX_VALUE));

        digestionList.setSelected("Enzyme");
        enzymeList.setSelected("Trypsin");
        specificityList.setSelected("Specific");
        fragmentIonTypes.setSelectedI("b");
        fragmentIonTypes.setSelectedII("y");
        precursorTolerance.setSelected("ppm");
        fragmentTolerance.setSelected("Da");
        
        
        proteaseFragmentationContainer.addComponent(precursorTolerance, 1, 1);
        proteaseFragmentationContainer.addComponent(fragmentTolerance, 1, 2);
        proteaseFragmentationContainer.addComponent(initLabel2TextField("Precursor", "2", "4"), 1, 3);
        proteaseFragmentationContainer.addComponent(initLabel2TextField("Isotopes", "0", "1"), 1, 4);
        Button closeBtn = new Button("Close");
        closeBtn.setStyleName(ValoTheme.BUTTON_SMALL);
        closeBtn.addStyleName(ValoTheme.BUTTON_TINY);
        closeBtn.setHeight(25, Unit.PIXELS);
        closeBtn.addClickListener((Button.ClickEvent event) -> {
            ((PopupView) SearchSettingsLayout.this.getParent()).setPopupVisible(false);
        });
        proteaseFragmentationContainer.addComponent(closeBtn, 1, 5);
        proteaseFragmentationContainer.setComponentAlignment(closeBtn, Alignment.BOTTOM_RIGHT);

        return proteaseFragmentationContainer;

    }

    private HorizontalLayout initLabel2TextField(String title, String defaultValue1, String defaultValue2) {
        HorizontalLayout container = new HorizontalLayout();
        container.setSizeFull();
        Label cap = new Label(title);
        cap.addStyleName(ValoTheme.LABEL_TINY);
        cap.addStyleName(ValoTheme.LABEL_SMALL);
        cap.addStyleName("smallundecorated");
        container.addComponent(cap);
        container.setExpandRatio(cap, 45);

        TextField textField = new TextField();
        textField.setValue(defaultValue1);
        textField.addStyleName(ValoTheme.TEXTFIELD_ALIGN_CENTER);
        textField.setWidth(100, Unit.PERCENTAGE);
        textField.addStyleName(ValoTheme.TEXTFIELD_TINY);
        textField.setNullRepresentation(defaultValue1);
        textField.setWidth(100, Unit.PERCENTAGE);
        textField.setHeight(25, Unit.PIXELS);
        container.addComponent(textField);
        container.setExpandRatio(textField, 27.5f);

        TextField textField2 = new TextField();
        textField2.setValue(defaultValue2);
        textField2.addStyleName(ValoTheme.TEXTFIELD_ALIGN_CENTER);
        textField2.setWidth(100, Unit.PERCENTAGE);
        textField2.addStyleName(ValoTheme.TEXTFIELD_TINY);
        textField2.setNullRepresentation(defaultValue1);
        textField2.setWidth(100, Unit.PERCENTAGE);
        textField2.setHeight(25, Unit.PIXELS);
        container.addComponent(textField2);
        container.setExpandRatio(textField2, 27.5f);
        return container;

    }

}
