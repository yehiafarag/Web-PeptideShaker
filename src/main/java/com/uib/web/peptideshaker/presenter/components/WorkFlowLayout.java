package com.uib.web.peptideshaker.presenter.components;

import com.uib.web.peptideshaker.presenter.core.DropDownList;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

/**
 * This class represents SearchGUI-Peptide-Shaker work-flow which include input
 * form
 *
 * @author Yehia Farag
 */
public class WorkFlowLayout extends VerticalLayout {

    /**
     * Constructor to initialize the main attributes.
     */
    public WorkFlowLayout() {
        WorkFlowLayout.this.setWidth(100,Unit.PERCENTAGE);
         WorkFlowLayout.this.setHeightUndefined();
          WorkFlowLayout.this.setSpacing(true);
         
        Label titleLabel = new Label("SearchGUI-PeptideShaker-WorkFlow");
        titleLabel.setStyleName("frametitle");
        WorkFlowLayout.this.addComponent(titleLabel);
        DropDownList fastaFileList = new DropDownList("Protein Database (FASTA)");
         WorkFlowLayout.this.addComponent(fastaFileList);
        
        
    }

}
