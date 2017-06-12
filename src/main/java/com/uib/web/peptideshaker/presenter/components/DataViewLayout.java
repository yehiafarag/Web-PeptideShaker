package com.uib.web.peptideshaker.presenter.components;

import com.uib.web.peptideshaker.galaxy.SystemDataSet;
import com.uib.web.peptideshaker.presenter.core.ActionLabel;
import com.uib.web.peptideshaker.presenter.core.CustomizedPagedTable;
import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import java.util.Map;

/**
 * This class represents the data view layout (equal to history in galaxy) the
 * class allows users to get an over view of their files on galaxy and allow
 * users to delete the files and datasets.
 *
 * @author Yehia Farag
 */
public class DataViewLayout extends VerticalLayout {

    private final VerticalLayout topPanelLayout;
    private CustomizedPagedTable table;
   
    /**
     * Constructor to initialize the main layout and attributes.
     */
    public DataViewLayout() {
        DataViewLayout.this.setSizeFull();
        DataViewLayout.this.setSpacing(true);     
        topPanelLayout = new VerticalLayout();
        topPanelLayout.setWidth(100, Unit.PERCENTAGE);
        topPanelLayout.setHeight(100, Unit.PERCENTAGE);
        topPanelLayout.setSpacing(true);
        DataViewLayout.this.addComponent(topPanelLayout);
        
        table = new CustomizedPagedTable("Input Data");
        topPanelLayout.addComponent(table);
        topPanelLayout.setExpandRatio(table, 90);
        HorizontalLayout controlBar = table.getControlBar();
        controlBar.setSizeFull();
        topPanelLayout.addComponent(controlBar);
        topPanelLayout.setExpandRatio(controlBar, 10);
    }

    public void updateTable(Map<String, SystemDataSet> historyFilesMap) {
        table.setEnableVisibleRowsCounter(false);        
        IndexedContainer container;
        container = new IndexedContainer();
        container.addContainerProperty("Name", ActionLabel.class, null);
        container.addContainerProperty("Type", String.class, null);
        container.addContainerProperty("Status", String.class, null);
        container.addContainerProperty("Download", String.class, null);
        container.addContainerProperty("Delete", String.class, null);
        int i= 0;
        
        for (SystemDataSet ds: historyFilesMap.values()) {
            
            Item item = container.addItem(i);
            item.getItemProperty("Name").setValue(new ActionLabel(ds.getName()));
            item.getItemProperty("Type").setValue(ds.getType());
            item.getItemProperty("Status").setValue("progress");
            item.getItemProperty("Download").setValue(ds.getDownloadUrl());
            item.getItemProperty("Delete").setValue("Delete");
            i++;
        }
        table.setContainerDataSource(container);
        
    }
    public void updateDS(){
        table.applyDS();
    }

}
