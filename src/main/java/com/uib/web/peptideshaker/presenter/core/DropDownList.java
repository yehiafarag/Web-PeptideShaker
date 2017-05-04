package com.uib.web.peptideshaker.presenter.core;

import com.vaadin.ui.ComboBox;
import com.vaadin.ui.VerticalLayout;
import java.util.Map;

/**
 * This class represents drop-down list component
 *
 * @author Yehia Farag
 */
public class DropDownList extends VerticalLayout {

    /**
     * Drop down list component.
     */
    private final ComboBox list;

    /**
     * Constructor to initialize the main attributes.
     * @param title the title of the list 
     */
    public DropDownList(String title) {
        DropDownList.this.setSizeFull();
        DropDownList.this.setSpacing(true);
        DropDownList.this.setStyleName("dropdownlistframe");

        list = new ComboBox(title);
        list.setStyleName("dropdownlist");
        list.setInputPrompt("Please select from the list");
        list.setNullSelectionAllowed(false);
        DropDownList.this.addComponent(list);

    }

    /**
     *Update the drop down list
     * @param idToCaptionMap list of ids and names
     */
    public void updateList(Map<String, String> idToCaptionMap) {
        list.removeAllItems();
        list.clear();
        for (String id : idToCaptionMap.keySet()) {
            list.addItem(id);
            list.setItemCaption(id, idToCaptionMap.get(id)); 
            list.setValue(id);
        }
       
    }

}
