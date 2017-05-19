package com.uib.web.peptideshaker.presenter.core.form;

import com.vaadin.ui.ComboBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.themes.ValoTheme;
import java.util.HashSet;
import java.util.Set;

/**
 * This class represent form component (drop down list with caption on the left
 * side)
 *
 * @author Yehia Farag
 */
public class HorizontalLabelDropDounList extends HorizontalLayout {

    /**
     * Main drop-down list.
     */
    private final ComboBox list;

    /**
     * Constructor to initialize the main attributes
     *
     * @param caption title
     * @param values the drop-down list values
     */
    public HorizontalLabelDropDounList(String caption, Set<String> values) {
        HorizontalLabelDropDounList.this.setSizeFull();
        Label cap = new Label(caption);
        cap.addStyleName(ValoTheme.LABEL_TINY);
        cap.addStyleName(ValoTheme.LABEL_SMALL);
        cap.addStyleName("smallundecorated");
        HorizontalLabelDropDounList.this.addComponent(cap);
        HorizontalLabelDropDounList.this.setExpandRatio(cap, 45);
        if (values == null) {
            values = new HashSet<>();
        }
        if (values.isEmpty()) {
            values.add("N/A");
        }
        list = new ComboBox();
        list.setWidth(100, Unit.PERCENTAGE);
        list.setHeight(25, Unit.PIXELS);
        list.setStyleName(ValoTheme.COMBOBOX_SMALL);
        list.addStyleName(ValoTheme.COMBOBOX_TINY);
        list.setNullSelectionAllowed(false);
        for (String str : values) {
            list.addItem(str);
        }
        list.setValue(values.toArray()[0]);
        HorizontalLabelDropDounList.this.addComponent(list);
        HorizontalLabelDropDounList.this.setExpandRatio(list, 55);
    }

    public String getSelectedValue() {
        return list.getValue().toString();

    }

}
