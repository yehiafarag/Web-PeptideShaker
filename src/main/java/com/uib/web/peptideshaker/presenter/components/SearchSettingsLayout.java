package com.uib.web.peptideshaker.presenter.components;

import com.uib.web.peptideshaker.presenter.core.CloseButton;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import java.util.LinkedHashMap;
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
        HorizontalLayout titleLayout = new HorizontalLayout();
        titleLayout.setSizeFull();
        
        SearchSettingsLayout.this.addComponent(titleLayout);
        Label setteingsLabel = new Label("Search Settings");
        titleLayout.addComponent(setteingsLabel);
        
        CloseButton closeBtn = new CloseButton();
        titleLayout.addComponent(closeBtn);
        titleLayout.setComponentAlignment(closeBtn, Alignment.TOP_RIGHT);
        
        HorizontalLayout modificationContainer = inititModificationLayout();
        SearchSettingsLayout.this.addComponent(modificationContainer);
        
    }
    
    private HorizontalLayout inititModificationLayout() {
        HorizontalLayout modificationContainer = new HorizontalLayout();
        modificationContainer.setSizeFull();
        modificationContainer.setMargin(new MarginInfo(false, false, false, false));
        modificationContainer.setWidth(550, Unit.PIXELS);
        modificationContainer.setHeight(550, Unit.PIXELS);
        
        VerticalLayout leftSideLayout = new VerticalLayout();
        leftSideLayout.setSizeFull();
        leftSideLayout.setSpacing(true);
        leftSideLayout.setMargin(new MarginInfo(false, false, false, false));
        modificationContainer.addComponent(leftSideLayout);
        
        VerticalLayout spacer = new VerticalLayout();
        spacer.setSizeFull();
        leftSideLayout.addComponent(spacer);
        leftSideLayout.setExpandRatio(spacer, 4);
        
        HorizontalLayout leftTopLayout = new HorizontalLayout();
        leftTopLayout.setSizeFull();
        leftTopLayout.setSpacing(true);
        leftSideLayout.addComponent(leftTopLayout);
        leftSideLayout.setExpandRatio(leftTopLayout, 48);
        
        Table fixedModificationTable = initModificationTable();
        fixedModificationTable.setCaption("Fixed Modifications");
        leftTopLayout.addComponent(fixedModificationTable);
        leftTopLayout.setExpandRatio(fixedModificationTable, 80);
        
        VerticalLayout sideTopButtons = new VerticalLayout();
        sideTopButtons.setSizeUndefined();
        leftTopLayout.addComponent(sideTopButtons);
        leftTopLayout.setComponentAlignment(sideTopButtons, Alignment.MIDDLE_CENTER);
        
        leftTopLayout.setExpandRatio(sideTopButtons, 20);
        
        Button toFixedModBtn = new Button(VaadinIcons.ARROW_LEFT);
        toFixedModBtn.setStyleName(ValoTheme.BUTTON_ICON_ONLY);
        sideTopButtons.addComponent(toFixedModBtn);
        sideTopButtons.setComponentAlignment(toFixedModBtn, Alignment.MIDDLE_CENTER);
        
        Button fromFixedModBtn = new Button(VaadinIcons.ARROW_RIGHT);
        fromFixedModBtn.setStyleName(ValoTheme.BUTTON_ICON_ONLY);
        sideTopButtons.addComponent(fromFixedModBtn);
        sideTopButtons.setComponentAlignment(fromFixedModBtn, Alignment.MIDDLE_CENTER);
        
        HorizontalLayout leftBottomLayout = new HorizontalLayout();
        leftBottomLayout.setSizeFull();
        leftBottomLayout.setSpacing(true);
        leftSideLayout.addComponent(leftBottomLayout);
        leftSideLayout.setExpandRatio(leftBottomLayout, 48);
        
        Table variableModificationTable = initModificationTable();
        variableModificationTable.setCaption("Variable Modifications");
        leftBottomLayout.addComponent(variableModificationTable);
        leftBottomLayout.setExpandRatio(variableModificationTable, 80);
        
        VerticalLayout sideBottomButtons = new VerticalLayout();
        leftBottomLayout.addComponent(sideBottomButtons);
        leftBottomLayout.setExpandRatio(sideBottomButtons, 20);
        leftBottomLayout.setComponentAlignment(sideBottomButtons, Alignment.MIDDLE_CENTER);
        
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
        modificationContainer.addComponent(rightSideLayout);
        
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
        rightSideLayout.setExpandRatio(modificationListControl, 10);
        rightSideLayout.setComponentAlignment(modificationListControl, Alignment.MIDDLE_CENTER);
        Table mostUsedModificationsTable = initModificationTable();
        Map<Object, Object[]> modificationItems = new LinkedHashMap<>();
        for (int x = 0; x < 22; x++) {
            modificationItems.put(x, new Object[]{1 + x, "modi ", Double.valueOf(x + 10)});
        }
        for (Object id : modificationItems.keySet()) {
            mostUsedModificationsTable.addItem(modificationItems.get(id), id);
        }
        
        rightSideLayout.addComponent(mostUsedModificationsTable);
        
        rightSideLayout.setExpandRatio(mostUsedModificationsTable, 90);
        Table allModificationsTable = initModificationTable();
        rightSideLayout.addComponent(allModificationsTable);
        rightSideLayout.setExpandRatio(allModificationsTable, 90);
        allModificationsTable.setVisible(false);
        
        modificationListControl.addValueChangeListener((Property.ValueChangeEvent event) -> {
            if (modificationListControl.getValue().toString().equalsIgnoreCase("All Modifications")) {
                allModificationsTable.setVisible(true);
                mostUsedModificationsTable.setVisible(false);
            } else {
                allModificationsTable.setVisible(false);
                mostUsedModificationsTable.setVisible(true);
            }
        });
        toFixedModBtn.addClickListener((Button.ClickEvent event) -> {
            Table selectionTable = null;
            if (allModificationsTable.isVisible()) {
                selectionTable = allModificationsTable;
            } else {
                selectionTable = mostUsedModificationsTable;
            }
            System.out.println("at selected value " + ((Set<Object>) selectionTable.getValue()));
            Set<Object> selection = ((Set<Object>) selectionTable.getValue());
            for (Object id : selection) {
                fixedModificationTable.addItem(modificationItems.get(id), id);
                selectionTable.removeItem(id);
            }
            
        });
        
        return modificationContainer;
        
    }
    
    private Table initModificationTable() {
        Table modificationsTable = new Table();
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
    
}
