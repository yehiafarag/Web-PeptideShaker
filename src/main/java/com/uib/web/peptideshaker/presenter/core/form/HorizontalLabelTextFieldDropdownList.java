package com.uib.web.peptideshaker.presenter.core.form;

import com.vaadin.data.Validator;
import com.vaadin.data.validator.IntegerRangeValidator;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.themes.ValoTheme;
import java.util.HashSet;
import java.util.Set;

/**
 * This class represent form component (drop down list with caption on the left
 * side)
 *
 * @author Yehia Farag
 */
public class HorizontalLabelTextFieldDropdownList extends HorizontalLayout {

    /**
     * First drop-down list.
     */
    private final TextField textField;
    /**
     * Second drop-down list.
     */
    private final ComboBox list2;

    /**
     * Constructor to initialize the main attributes
     *
     * @param caption title
     * @param values the drop-down list values
     */
    public HorizontalLabelTextFieldDropdownList(String title, Object defaultValue, Set<String> values,Validator validator) {

        HorizontalLabelTextFieldDropdownList.this.setSizeFull();
        Label cap = new Label(title);
        cap.addStyleName(ValoTheme.LABEL_TINY);
        cap.addStyleName(ValoTheme.LABEL_SMALL);
        cap.addStyleName("smallundecorated");
        HorizontalLabelTextFieldDropdownList.this.addComponent(cap);
        HorizontalLabelTextFieldDropdownList.this.setExpandRatio(cap, 45);

        if (defaultValue == null) {
            defaultValue = 0.0;
        }
       
        if (values == null) {
            values = new HashSet<>();
        }
        if (values.isEmpty()) {
            values.add("N/A");
            values.add("x");
            values.add("y");
            values.add("z");
        }
        textField = new TextField();
        textField.setValidationVisible(true);
        textField.setConverter(Double.class);
     
        textField.addValidator(validator);
        textField.addStyleName(ValoTheme.TEXTFIELD_ALIGN_CENTER);
        textField.setWidth(100, Unit.PERCENTAGE);
        textField.addStyleName(ValoTheme.TEXTFIELD_TINY);
        textField.setNullRepresentation(defaultValue.toString());
        textField.setWidth(100, Unit.PERCENTAGE);
        textField.setHeight(25, Unit.PIXELS);
        HorizontalLabelTextFieldDropdownList.this.addComponent(textField);
        HorizontalLabelTextFieldDropdownList.this.setExpandRatio(textField, 27.5f);

        list2 = new ComboBox();
        list2.setWidth(100, Unit.PERCENTAGE);
        list2.setHeight(25, Unit.PIXELS);
        list2.setStyleName(ValoTheme.COMBOBOX_SMALL);
        list2.addStyleName(ValoTheme.COMBOBOX_TINY);
        list2.addStyleName(ValoTheme.COMBOBOX_ALIGN_CENTER);
//        list.addStyleName("inline-label");
        list2.setNullSelectionAllowed(false);

        for (String str : values) {
            list2.addItem(str);
        }
        list2.setValue(values.toArray()[0]);
        HorizontalLabelTextFieldDropdownList.this.addComponent(list2);
        HorizontalLabelTextFieldDropdownList.this.setExpandRatio(list2, 27.5f);
    }

    public String getFirstSelectedValue() {
        return textField.getValue();

    }

    public String getSecondSelectedValue() {
        return list2.getValue().toString();

    }
    public void setSelected(Object objectId){
        list2.select(objectId);
    
    }

}
