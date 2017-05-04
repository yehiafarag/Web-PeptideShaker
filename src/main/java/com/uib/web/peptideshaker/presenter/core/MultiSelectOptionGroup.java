package com.uib.web.peptideshaker.presenter.core;

import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.VerticalLayout;
import java.util.Map;
import java.util.Set;

/**
 * This class represents drop-down list component
 *
 * @author Yehia Farag
 */
public class MultiSelectOptionGroup extends VerticalLayout {

    private final OptionGroup list;

    /**
     * Constructor to initialize the main attributes.
     */
    public MultiSelectOptionGroup(String title) {
        MultiSelectOptionGroup.this.setSizeUndefined();
        MultiSelectOptionGroup.this.setSpacing(true);
        MultiSelectOptionGroup.this.setStyleName("optiongroupframe");

        list = new OptionGroup(title);
        list.setSizeUndefined();
        list.setMultiSelect(true);
        list.setStyleName("optiongroup");
        MultiSelectOptionGroup.this.addComponent(list);

    }

    /**
     * Update the list
     *
     * @param idToCaptionMap list of ids and names
     */
    public void updateList(Map<String, String> idToCaptionMap) {
        list.removeAllItems();
        list.clear();
        for (String id : idToCaptionMap.keySet()) {
            list.addItem(id);
            list.setItemCaption(id, idToCaptionMap.get(id));           
        }

    }

}