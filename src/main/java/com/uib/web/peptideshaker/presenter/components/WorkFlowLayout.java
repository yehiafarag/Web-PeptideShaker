package com.uib.web.peptideshaker.presenter.components;

import com.uib.web.peptideshaker.presenter.core.DropDownList;
import com.uib.web.peptideshaker.presenter.core.MultiSelectOptionGroup;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import java.util.HashSet;
import java.util.Set;

/**
 * This class represents SearchGUI-Peptide-Shaker work-flow which include input
 * form
 *
 * @author Yehia Farag
 */
public class WorkFlowLayout extends Panel {

    /**
     * Constructor to initialize the main attributes.
     */
    public WorkFlowLayout() {
        WorkFlowLayout.this.setWidth(100, Unit.PERCENTAGE);
        WorkFlowLayout.this.setHeight(100,Unit.PERCENTAGE);
        
        VerticalLayout content = new VerticalLayout();
        content.setHeightUndefined();
        content.setWidth(100,Unit.PERCENTAGE);
        this.setContent(content);
        this.setStyleName("subframe");
        
        
        content.setSpacing(true);

        Label titleLabel = new Label("SearchGUI-PeptideShaker-WorkFlow");
        titleLabel.setStyleName("frametitle");
        content.addComponent(titleLabel);

//        GridLayout container = new GridLayout(2, 2);
//        container.setSizeFull();
//        container.setStyleName("minwidth470");
//        container.addStyleName("maxwidth600");
//        container.setColumnExpandRatio(0, 3);
//         container.setColumnExpandRatio(1, 1);
//          WorkFlowLayout.this.addComponent(container);
        DropDownList fastaFileList = new DropDownList("Protein Database (FASTA)");
        content.addComponent(fastaFileList);

        MultiSelectOptionGroup mgfFileList = new MultiSelectOptionGroup("Spectrum File(s)");
        content.addComponent(mgfFileList);

        Set<String> data = new HashSet<>();
        data.add("KokowawaKokowawaKokowawaKokowawaKokowawaKokowawa");
        data.add("Kokowawa1KokowawaKokowawaKokowawaKokowawaKokowawa");
        data.add("Kokowawa2KokowawaKokowawaKokowawaKokowawaKokowawa");
        data.add("Kokowawa3KokowawaKokowawaKokowawaKokowawaKokowawa");
        data.add("Kokowawa4KokowawaKokowawaKokowawaKokowawaKokowawa");
        data.add("Kokowawa5KokowawaKokowawaKokowawaKokowawaKokowawa");
        data.add("Kokowawa6KokowawaKokowawaKokowawaKokowawaKokowawa");
        data.add("Kokowawa7KokowawaKokowawaKokowawaKokowawaKokowawa");
        data.add("Kokowawa8KokowawaKokowawaKokowawaKokowawaKokowawa");
        data.add("Kokowawa9KokowawaKokowawaKokowawaKokowawaKokowawa");
        mgfFileList.updateOptionGroupData(data);

        MultiSelectOptionGroup searchEngines = new MultiSelectOptionGroup("Search Engines");
       content.addComponent(searchEngines);

        Set<String> searchEngienList = new HashSet<>();
        searchEngienList.add("X!Tandem");
        searchEngienList.add("MS-GF+");
        searchEngienList.add("OMSSA");
        searchEngienList.add("Comet");
        searchEngines.updateOptionGroupData(searchEngienList);

    }

}
