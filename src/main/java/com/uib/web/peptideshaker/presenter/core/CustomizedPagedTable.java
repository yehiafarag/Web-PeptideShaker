package com.uib.web.peptideshaker.presenter.core;

import com.jensjansson.pagedtable.PagedTable;
import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.Validator;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.server.PaintException;
import com.vaadin.server.PaintTarget;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Field;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.declarative.DesignContext;
import java.util.Collection;
import java.util.EventObject;
import org.jsoup.nodes.Element;

/**
 * This Class represents Paged Table to customize PagedTable add-on to suit the
 * application
 *
 * @author Yehia Farag
 */
public class CustomizedPagedTable extends PagedTable {

    private HorizontalLayout controlBar;
    private HorizontalLayout controlBarContainer;

    public CustomizedPagedTable(String caption) {
        super(caption);
        this.setSelectable(true);
        CustomizedPagedTable.this.addStyleName("disablescrolltable");
        CustomizedPagedTable.this.setHeight(100, Unit.PERCENTAGE);
        CustomizedPagedTable.this.setWidth(100, Unit.PERCENTAGE);
        controlBarContainer = new HorizontalLayout();
        CustomizedPagedTable.this.setPageLength(1000);
        CustomizedPagedTable.this.setCacheRate(0);
        ///dummy data 
        IndexedContainer container;
        container = new IndexedContainer();
        container.addContainerProperty("Name", ActionLabel.class, null);
        container.addContainerProperty("Type", String.class, null);
        container.addContainerProperty("Status", String.class, null);
        container.addContainerProperty("Download", String.class, null);
        container.addContainerProperty("Delete", String.class, null);
        for (int i = 0; i < 200; i++) {
            Item item = container.addItem(i);
//            item.getItemProperty("Name").setValue(new ActionLabel(i + ""));
            item.getItemProperty("Type").setValue("bar");
            item.getItemProperty("Status").setValue("baz");
            item.getItemProperty("Download").setValue("baz");
            item.getItemProperty("Delete").setValue("baz");
        }
        CustomizedPagedTable.this.setContainerDataSource(container);

    }

    public HorizontalLayout getControlBar() {
        return controlBarContainer;
    }
    Container newDataSource;

    public void applyDS() {
        super.setContainerDataSource(newDataSource);
    }

    @Override
    public void setContainerDataSource(Container newDataSource) {
        this.newDataSource = newDataSource;
        super.setContainerDataSource(newDataSource); //To change body of generated methods, choose Tools | Templates.
        if (controlBarContainer == null) {
            controlBarContainer = new HorizontalLayout();
        } else {
            controlBarContainer.removeAllComponents();
        }
        if (!enableVisibleRowCounter) {
//            ComboBox itemsPerPageSelect = (ComboBox) pageSize.getComponent(1);
            this.setCacheRate(2);
            itemId = itemId - 3;
            setPageLength(itemId);
            this.setImmediate(true);
        }
        controlBarContainer.removeAllComponents();
        this.controlBar = this.createControls();
        controlBarContainer.addComponent(controlBar);
        HorizontalLayout pageSize = (HorizontalLayout) controlBar.getComponent(0);
        HorizontalLayout pageManagement = (HorizontalLayout) controlBar.getComponent(1);
        controlBar.setComponentAlignment(pageManagement, Alignment.TOP_CENTER);
        pageSize.setVisible(false);
        if (this.getTotalAmountOfPages() == 1) {
            controlBar.setEnabled(true);
        }
        this.setSizeFull();
        TextField currentPageTextField = (TextField) pageManagement.getComponent(3);
        currentPageTextField.setValue(1 + "");
        this.refreshRowCache();
        this.setSizeFull();
        this.setValue(0);

    }

    int itemId = 0;
    private boolean enableVisibleRowCounter = true;

    @Override
    protected void paintRowAttributes(PaintTarget target, Object itemId) throws PaintException {
        if (enableVisibleRowCounter) {
            System.out.println("at visible  row " + itemId);
            this.itemId = Integer.valueOf(itemId + "");
        }
        super.paintRowAttributes(target, itemId); //To change body of generated methods, choose Tools | Templates.
    }

    public void setEnableVisibleRowsCounter(boolean enable) {
        enableVisibleRowCounter = enable;
    }

    public int getVisibleItemsNumber() {
        return itemId;
    }

}
