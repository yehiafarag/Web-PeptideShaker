package com.uib.web.peptideshaker.presenter.components;

import com.jensjansson.pagedtable.PagedTable;
import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;

/**
 * This class represents the data view layout (equal to history in galaxy) the
 * class allows users to get an over view of their files on galaxy and allow
 * users to delete the files and datasets.
 *
 * @author Yehia Farag
 */
public class DataViewLayout extends VerticalLayout {

    private final VerticalLayout topPanelLayout;
//    private final VerticalLayout bottomPanelLayout;

    /**
     * Constructor to initialize the main layout and attributes.
     */
    public DataViewLayout() {
        DataViewLayout.this.setSizeFull();
        DataViewLayout.this.setSpacing(true);

//         Label titleLabel = new Label("Data on Galaxy");
//        titleLabel.setStyleName("frametitle");
//        DataViewLayout.this.addComponent(titleLabel);
//        Panel topPanel = new Panel("Input Data");
////        topPanel.setStyleName("panelframe");
//        topPanel.addStyleName("subpanel");
//        topPanel.setWidth(80, Unit.PERCENTAGE);
//        topPanel.setHeight(100, Unit.PERCENTAGE);
//        DataViewLayout.this.addComponent(topPanel);
        topPanelLayout = new VerticalLayout();
        topPanelLayout.setWidth(100, Unit.PERCENTAGE);
        topPanelLayout.setHeight(100, Unit.PERCENTAGE);
        topPanelLayout.setSpacing(true);
         DataViewLayout.this.addComponent(topPanelLayout);

//        topPanelLayout.setSpacing(true);
//        topPanel.setContent(topPanelLayout);
        IndexedContainer container = new IndexedContainer();
        container.addContainerProperty("Name", String.class, null);
        container.addContainerProperty("Type", String.class, null);
        container.addContainerProperty("Status", String.class, null);
        container.addContainerProperty("Download", String.class, null);
        container.addContainerProperty("Delete", String.class, null);
        for (int i = 0; i < 50; i++) {
            Item item = container.addItem(i);
            item.getItemProperty("Name").setValue("foo " + i);
            item.getItemProperty("Type").setValue("bar");
            item.getItemProperty("Status").setValue("baz");
            item.getItemProperty("Download").setValue("baz");
            item.getItemProperty("Delete").setValue("baz");
        }
        PagedTable table = new PagedTable("Input Data");
        table.setContainerDataSource(container);
        table.setHeight(100,Unit.PERCENTAGE);
        table.setSizeFull();
       
       
        table.commit();
        
         topPanelLayout.addComponent(table);
         HorizontalLayout controlBar = table.createControls();
         controlBar.setSizeFull();
        topPanelLayout.addComponent(controlBar);
      
        
         HorizontalLayout pageSize = (HorizontalLayout)controlBar.getComponent(0);
        HorizontalLayout pageManagement = (HorizontalLayout)controlBar.getComponent(1);        
        controlBar.setComponentAlignment(pageManagement,Alignment.TOP_CENTER);
        ComboBox itemsPerPageSelect = (ComboBox)pageSize.getComponent(1);  
        itemsPerPageSelect.addItem("10");
        itemsPerPageSelect.select("10");
         table.setPageLength(10);
        pageSize.setVisible(false);  
        System.out.println("at get page count "+table.getTotalAmountOfPages()+ "   ");

//        Panel bottomPanel = new Panel("Results Data");
////        bottomPanel.setStyleName("panelframe");
//        bottomPanel.setWidth(445, Unit.PIXELS);
//        bottomPanel.setHeight(100, Unit.PERCENTAGE);
//        bottomPanel.addStyleName("subpanel");
//        DataViewLayout.this.addComponent(bottomPanel);
//        bottomPanelLayout = new VerticalLayout();
//        bottomPanelLayout.setWidth(100, Unit.PERCENTAGE);
//        bottomPanelLayout.setSpacing(true);
//        bottomPanel.setContent(bottomPanelLayout);
//        DataViewLayout.this.updataDatasets();
    }

    private void updataDatasets() {
//        topPanelLayout.removeAllComponents();
//        bottomPanelLayout.removeAllComponents();
//        for (int x = 0; x < 20; x++) {
//            DatasetView ds = new DatasetView();
////        topPanelLayout.addComponent(ds);
//            DatasetView ds1 = new DatasetView();
////        bottomPanelLayout.addComponent(ds1);
//
//        }

    }

}
