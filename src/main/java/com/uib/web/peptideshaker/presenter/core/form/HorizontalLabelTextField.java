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
public class HorizontalLabelTextField extends HorizontalLayout {

    /**
     * Main drop-down list.
     */
    private final TextField textField;
    private String defaultValue;

    /**
     * Constructor to initialize the main attributes
     *
     * @param caption title
     * @param values the drop-down list values
     */
    public HorizontalLabelTextField(String caption, Object defaultValue, Validator validator) {
        HorizontalLabelTextField.this.setSizeFull();
        Label cap = new Label(caption);
        cap.addStyleName(ValoTheme.LABEL_TINY);
        cap.addStyleName(ValoTheme.LABEL_SMALL);
        cap.addStyleName("smallundecorated");
        HorizontalLabelTextField.this.addComponent(cap);
        HorizontalLabelTextField.this.setExpandRatio(cap, 45);

        if (defaultValue == null) {
            this.defaultValue = "0";
        } else {
            this.defaultValue = defaultValue.toString();
        }

        textField = new TextField();
        textField.setValidationVisible(true);
        textField.setConverter(Integer.class);

        textField.addValidator(validator);
        textField.addStyleName(ValoTheme.TEXTFIELD_ALIGN_CENTER);
        textField.setWidth(100, Unit.PERCENTAGE);
        textField.addStyleName(ValoTheme.TEXTFIELD_TINY);
        textField.setNullRepresentation(defaultValue.toString());
        textField.setWidth(100, Unit.PERCENTAGE);
        textField.setHeight(25, Unit.PIXELS);
        HorizontalLabelTextField.this.addComponent(textField);
        HorizontalLabelTextField.this.setExpandRatio(textField, 55);
    }

    public boolean isValid() {
        boolean check = textField.isValid();
        return check;

    }

    public void setSelectedValue(Object value){
        textField.setValue(value+"");
    
    }
    public String getSelectedValue() {
        if (textField.getValue() == null) {
            return defaultValue;
        
        }
        return textField.getValue();

    }

}
