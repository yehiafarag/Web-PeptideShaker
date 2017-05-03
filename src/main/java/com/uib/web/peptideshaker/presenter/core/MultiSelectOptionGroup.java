package com.uib.web.peptideshaker.presenter.core;

import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.VerticalLayout;
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

//        Label subTitle = new Label(title);
//        subTitle.setStyleName("subtitle");
//        MultiSelectOptionGroup.this.addComponent(subTitle);
        list = new OptionGroup(title);
        list.setSizeUndefined();
        list.setMultiSelect(true);
        list.setStyleName("optiongroup");
        MultiSelectOptionGroup.this.addComponent(list);
       

    }

    public void updateOptionGroupData(Set<String> data) {
        list.removeAllItems();
        for (String item : data) {
            list.addItem(item);
        }

    }

}
