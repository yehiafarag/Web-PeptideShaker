package com.uib.web.peptideshaker.presenter.components;

import com.uib.web.peptideshaker.galaxy.SystemDataSet;
import com.uib.web.peptideshaker.presenter.core.ActionLabel;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import java.util.Map;

/**
 * This class represents the data view layout (equal to history in galaxy) the
 * class allows users to get an over view of their files on galaxy and allow
 * users to delete the files and datasets.
 *
 * @author Yehia Farag
 */
public class DataViewLayout extends Panel {

    private final VerticalLayout topPanelLayout;
    private final VerticalLayout dataTable;
    private final float[] expandingRatio = new float[]{5f, 35f, 15, 15, 15, 15f};

    /**
     * Constructor to initialize the main layout and attributes.
     */
    public DataViewLayout() {
        DataViewLayout.this.setWidth(100, Unit.PERCENTAGE);
        DataViewLayout.this.setHeight(100, Unit.PERCENTAGE);
        DataViewLayout.this.setStyleName("integratedframe");

        topPanelLayout = new VerticalLayout();
        topPanelLayout.setWidth(100, Unit.PERCENTAGE);
        topPanelLayout.setHeightUndefined();
        topPanelLayout.setSpacing(true);
        DataViewLayout.this.setContent(topPanelLayout);
        dataTable = new VerticalLayout();
        dataTable.setWidth(100, Unit.PERCENTAGE);
        dataTable.setHeightUndefined();
        dataTable.setSpacing(true);
        dataTable.setSpacing(true);
        topPanelLayout.addComponent(dataTable);
    }

    public void updateTable(Map<String, SystemDataSet> historyFilesMap) {

        Label headerName = new Label("Name");
        Label headerType = new Label("Type");
        Label headerStatus = new Label("Status");
        Label headerDownload = new Label("Download");
        headerDownload.addStyleName("textalignmiddle");
        Label headerDelete = new Label("Delete");
        headerDelete.addStyleName("textalignmiddle");

        HorizontalLayout headerRow = initializeRowData(new Component[]{new Label(""), headerName, headerType, headerStatus, headerDownload, headerDelete}, true);
        dataTable.addComponent(headerRow);

        int i = 1;
        for (SystemDataSet ds : historyFilesMap.values()) {
            if (ds.getName() == null) {
                continue;
            }
            Component nameLabel;
            if (ds.getType().equalsIgnoreCase("Web Peptide Shaker Dataset")) {
                nameLabel = new ActionLabel(VaadinIcons.EYE, ds.getName(), "View " + ds.getName());
            } else {
                nameLabel = new Label(ds.getName());
            }
            HorizontalLayout rowLayout = initializeRowData(new Component[]{new Label(i + ""), nameLabel, new Label(ds.getType()), new Label("Status"), new ActionLabel(VaadinIcons.DOWNLOAD_ALT, "Download File"), new ActionLabel(VaadinIcons.TRASH, "Delete File")}, false);
            dataTable.addComponent(rowLayout);
            i++;
        }

    }

    private HorizontalLayout initializeRowData(Component[] data, boolean header) {
        HorizontalLayout row = new HorizontalLayout();
        row.setSpacing(true);
        int i = 0;
        for (Component component : data) {
//            component.setSizeFull();
//            component.setSizeUndefined();
            component.addStyleName(ValoTheme.LABEL_NO_MARGIN);
            row.addComponent(component);
            row.setComponentAlignment(component, Alignment.MIDDLE_CENTER);
            row.setExpandRatio(component, expandingRatio[i]);
            i++;
        }
        row.setWidth(100, Unit.PERCENTAGE);
        row.setHeight(30, Unit.PIXELS);
        row.setStyleName("row");
        if (header) {
            row.addStyleName("header");
        }

        return row;
    }

}
