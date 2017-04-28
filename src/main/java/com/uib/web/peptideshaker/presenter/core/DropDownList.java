
package com.uib.web.peptideshaker.presenter.core;

import com.vaadin.ui.ComboBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

/**
 *This class represents drop-down list component 
 * @author YEhia Farag
 */
public class DropDownList extends VerticalLayout{

     /**
     * Constructor to initialize the main attributes.
     */
    public DropDownList(String title) {
        DropDownList.this.setSizeFull();
         DropDownList.this.setSpacing(true);
          DropDownList.this.setStyleName("dropdownlistframe");
        
        Label subTitle = new Label(title);
        subTitle.setStyleName("subtitle");
        DropDownList.this.addComponent(subTitle);
        
        ComboBox list = new ComboBox();
        list.setStyleName("dropdownlist");
          DropDownList.this.addComponent(list);
        
        
    }
    
}
