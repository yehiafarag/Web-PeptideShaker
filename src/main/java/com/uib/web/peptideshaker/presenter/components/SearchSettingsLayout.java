package com.uib.web.peptideshaker.presenter.components;

import com.uib.web.peptideshaker.presenter.core.CloseButton;
import com.vaadin.data.Property;
import com.vaadin.event.LayoutEvents;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.PopupView;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * This class represents the search settings form layout
 *
 * @author Yehia Farag
 */
public class SearchSettingsLayout extends VerticalLayout {

    /**
     * Constructor to initialize the main setting parameters
     */
    public SearchSettingsLayout() {
        SearchSettingsLayout.this.setMargin(true);
        SearchSettingsLayout.this.setSizeUndefined();
        SearchSettingsLayout.this.setSpacing(true);
        HorizontalLayout titleLayout = new HorizontalLayout();
        titleLayout.setSizeFull();

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
        Map<Object, Object[]> mostUseModificationItems = new LinkedHashMap<>();
        Map<Object, Object[]> completeModificationItems = new LinkedHashMap<>();
        for (int x = 0; x < 22; x++) {
            Object[] modificationArr = new Object[]{1 + x, "modi ", Double.valueOf(x + 10)};
            mostUseModificationItems.put(x, modificationArr);
            completeModificationItems.put(x, modificationArr);
        }
        for (int x = 22; x < 30; x++) {
            Object[] modificationArr = new Object[]{1 + x, "compl modi ", Double.valueOf(x + 10)};
            completeModificationItems.put(x, modificationArr);
        }
        for (Object id : mostUseModificationItems.keySet()) {
            mostUsedModificationsTable.addItem(mostUseModificationItems.get(id), id);
        }

        rightSideLayout.addComponent(mostUsedModificationsTable);

        rightSideLayout.setExpandRatio(mostUsedModificationsTable, 96);
        Table allModificationsTable = initModificationTable("");
        rightSideLayout.addComponent(allModificationsTable);
        rightSideLayout.setExpandRatio(allModificationsTable, 96);
        allModificationsTable.setVisible(false);

        modificationListControl.addValueChangeListener((Property.ValueChangeEvent event) -> {
            if (modificationListControl.getValue().toString().equalsIgnoreCase("All Modifications")) {
                allModificationsTable.removeAllItems();
                for (Object id : completeModificationItems.keySet()) {
                    if (fixedModificationTable.containsId(id) || variableModificationTable.containsId(id)) {
                        continue;
                    }
                    allModificationsTable.addItem(completeModificationItems.get(id), id);
                }
                allModificationsTable.setVisible(true);
                mostUsedModificationsTable.setVisible(false);

            } else {
                mostUsedModificationsTable.removeAllItems();
                for (Object id : mostUseModificationItems.keySet()) {
                    if (fixedModificationTable.containsId(id) || variableModificationTable.containsId(id)) {
                        continue;
                    }
                    mostUsedModificationsTable.addItem(mostUseModificationItems.get(id), id);
                }

                allModificationsTable.setVisible(false);
                mostUsedModificationsTable.setVisible(true);
            }
        });
        toFixedModBtn.addClickListener((Button.ClickEvent event) -> {
            Table selectionTable;
            if (allModificationsTable.isVisible()) {
                selectionTable = allModificationsTable;
            } else {
                selectionTable = mostUsedModificationsTable;
            }
            System.out.println("at selected value " + ((Set<Object>) selectionTable.getValue()));
            Set<Object> selection = ((Set<Object>) selectionTable.getValue());
            for (Object id : selection) {
                fixedModificationTable.addItem(mostUseModificationItems.get(id), id);
                selectionTable.removeItem(id);
            }
            fixedModificationTable.sort(new Object[]{"color"}, new boolean[]{true});

        });
        fromFixedModBtn.addClickListener((Button.ClickEvent event) -> {
            Table selectionTable;
            if (allModificationsTable.isVisible()) {
                selectionTable = allModificationsTable;
            } else {
                selectionTable = mostUsedModificationsTable;
            }
            System.out.println("at selected value " + ((Set<Object>) selectionTable.getValue()));
            Set<Object> selection = ((Set<Object>) fixedModificationTable.getValue());
            for (Object id : selection) {
                selectionTable.addItem(mostUseModificationItems.get(id), id);
                fixedModificationTable.removeItem(id);
            }
            selectionTable.sort(new Object[]{"color"}, new boolean[]{true});

        });
        toVariableModBtn.addClickListener((Button.ClickEvent event) -> {
            Table selectionTable;
            if (allModificationsTable.isVisible()) {
                selectionTable = allModificationsTable;
            } else {
                selectionTable = mostUsedModificationsTable;
            }
            System.out.println("at selected value " + ((Set<Object>) selectionTable.getValue()));
            Set<Object> selection = ((Set<Object>) selectionTable.getValue());
            for (Object id : selection) {
                variableModificationTable.addItem(mostUseModificationItems.get(id), id);
                selectionTable.removeItem(id);
            }
            variableModificationTable.sort(new Object[]{"color"}, new boolean[]{true});

        });
        fromVariableModBtn.addClickListener((Button.ClickEvent event) -> {
            Table selectionTable;
            if (allModificationsTable.isVisible()) {
                selectionTable = allModificationsTable;
            } else {
                selectionTable = mostUsedModificationsTable;
            }
            System.out.println("at selected value " + ((Set<Object>) selectionTable.getValue()));
            Set<Object> selection = ((Set<Object>) variableModificationTable.getValue());
            for (Object id : selection) {
                selectionTable.addItem(mostUseModificationItems.get(id), id);
                variableModificationTable.removeItem(id);
            }
            selectionTable.sort(new Object[]{"color"}, new boolean[]{true});

        });

        return modificationContainer;

    }

    private Table initModificationTable(String cap) {
        Table modificationsTable = new Table(cap);
        modificationsTable.setSizeFull();
        modificationsTable.setStyleName(ValoTheme.TABLE_SMALL);
        modificationsTable.addStyleName(ValoTheme.TABLE_COMPACT);
        modificationsTable.addStyleName(ValoTheme.TABLE_NO_VERTICAL_LINES);
        modificationsTable.setMultiSelect(true);
        modificationsTable.setSelectable(true);
        modificationsTable.addContainerProperty("color", Integer.class, null, "", null, Table.Align.CENTER);
        modificationsTable.addContainerProperty("name", String.class, null, "Name", null, Table.Align.CENTER);
        modificationsTable.addContainerProperty("mass", Double.class, null, "Mass", null, Table.Align.CENTER);

        return modificationsTable;
    }

    private GridLayout inititProteaseFragmentationLayout() {
        GridLayout proteaseFragmentationContainer = new GridLayout(2, 6);
        proteaseFragmentationContainer.setStyleName("panelframe");
        proteaseFragmentationContainer.setColumnExpandRatio(0, 55);
        proteaseFragmentationContainer.setColumnExpandRatio(1, 45);
        proteaseFragmentationContainer.setMargin(new MarginInfo(false, false, true, false));
        proteaseFragmentationContainer.setWidth(700, Unit.PIXELS);
        proteaseFragmentationContainer.setHeight(220, Unit.PIXELS);
        proteaseFragmentationContainer.setSpacing(true);

        Label label = new Label("Protease & Fragmentation");
        label.setSizeFull();
        proteaseFragmentationContainer.addComponent(label, 0, 0);

        proteaseFragmentationContainer.addComponent(initLabelDropDounList("Digestion", new LinkedHashSet<>()), 0, 1);
        proteaseFragmentationContainer.addComponent(initLabelDropDounList("Digestion", new LinkedHashSet<>()), 0, 2);
        proteaseFragmentationContainer.addComponent(initLabelDropDounList("Digestion", new LinkedHashSet<>()), 0, 3);
        proteaseFragmentationContainer.addComponent(initLabelDropDounList("Digestion", new LinkedHashSet<>()), 0, 4);
        proteaseFragmentationContainer.addComponent(initLabelDropDounList("Digestion", new LinkedHashSet<>()), 0, 5);
        proteaseFragmentationContainer.addComponent(initLabelDropDounList("Digestion", new LinkedHashSet<>()), 1, 1);
        proteaseFragmentationContainer.addComponent(initLabelDropDounList("Digestion", new LinkedHashSet<>()), 1, 2);
        proteaseFragmentationContainer.addComponent(initLabelDropDounList("Digestion", new LinkedHashSet<>()), 1, 3);
        proteaseFragmentationContainer.addComponent(initLabelDropDounList("Digestion", new LinkedHashSet<>()), 1, 4);
        Button closeBtn = new Button("Close");
        closeBtn.setStyleName(ValoTheme.BUTTON_SMALL);
        closeBtn.addStyleName(ValoTheme.BUTTON_TINY);
        closeBtn.setHeight(25,Unit.PIXELS);
        closeBtn.addClickListener((Button.ClickEvent event) -> {
            ((PopupView) SearchSettingsLayout.this.getParent()).setPopupVisible(false);
        });
          proteaseFragmentationContainer.addComponent(closeBtn, 1, 5);
          proteaseFragmentationContainer.setComponentAlignment(closeBtn, Alignment.BOTTOM_RIGHT);

        return proteaseFragmentationContainer;

    }

    private HorizontalLayout initLabelDropDounList(String title, Set<String> values) {
        HorizontalLayout container = new HorizontalLayout();
        container.setSizeFull();
        ComboBox list = new ComboBox(title);
        list.setWidth(100, Unit.PERCENTAGE);
        list.setHeight(25, Unit.PIXELS);
        list.setStyleName(ValoTheme.COMBOBOX_SMALL);
        list.addStyleName(ValoTheme.COMBOBOX_TINY);
        list.addStyleName("inline-label");
        list.setNullSelectionAllowed(false);
        list.addItem("Most Used Modifications");
        list.addItem("All Modifications");
        list.setValue("Most Used Modifications");
        container.addComponent(list);
        container.setComponentAlignment(list, Alignment.MIDDLE_CENTER);

        return container;
    }

    private HorizontalLayout initLabelTextAreaDropDounList() {

        HorizontalLayout container = new HorizontalLayout();

        return container;

    }

}
