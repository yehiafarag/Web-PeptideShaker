package com.uib.web.peptideshaker.presenter.components.peptideshakerview;

import com.uib.web.peptideshaker.galaxy.PeptideShakerVisualizationDataset;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import java.util.Map;

/**
 * This class represents the layout that contains PeptideShaker datasets
 * overview
 *
 * @author Yehia Farag
 */
public class PeptideShakerDatasesViewLayout extends HorizontalLayout {

    /**
     * Constructor to initialize the main layout and variables.
     */
    public PeptideShakerDatasesViewLayout() {
        PeptideShakerDatasesViewLayout.this.setSizeFull();
        PeptideShakerDatasesViewLayout.this.setSpacing(true);
        PeptideShakerDatasesViewLayout.this.setMargin(true);
    }

    /**
     * Update the dataset overview layout.
     *
     * @param peptideShakerVisualizationMap map of visualization datasets
     */
    public void updateData(Map<String, PeptideShakerVisualizationDataset> peptideShakerVisualizationMap) {
        this.removeAllComponents();
// #of datasets

        //mode of view
        for (PeptideShakerVisualizationDataset vDs : peptideShakerVisualizationMap.values()) {
            if (vDs.getStatus().equalsIgnoreCase("ok") && vDs.isValidFile()) {
                DatasetComponent dsComponent = new DatasetComponent(vDs){
                    @Override
                    public void select(String datasetId) {
                        PeptideShakerDatasesViewLayout.this.selectDataset(peptideShakerVisualizationMap.get(datasetId));
                    }
                
                };
                this.addComponent(dsComponent);
                this.setComponentAlignment(dsComponent, Alignment.TOP_CENTER);
                
//                 DatasetComponent ds29 = new DatasetComponent(vDs);
//                this.addComponent(ds29);
//                this.setComponentAlignment(ds29, Alignment.MIDDLE_CENTER);
//                
//                 DatasetComponent dsComponent1 = new DatasetComponent(vDs);
//                this.addComponent(dsComponent1);
//                this.setComponentAlignment(dsComponent1, Alignment.MIDDLE_CENTER);
//                
//                 DatasetComponent ds2 = new DatasetComponent(vDs);
//                this.addComponent(ds2);
//                this.setComponentAlignment(ds2, Alignment.MIDDLE_CENTER);
//                
//                 DatasetComponent ds3 = new DatasetComponent(vDs);
//                this.addComponent(ds3);
//                this.setComponentAlignment(ds3, Alignment.MIDDLE_CENTER);
//                
//                 DatasetComponent ds24 = new DatasetComponent(vDs);
//                this.addComponent(ds24);
//                this.setComponentAlignment(ds24, Alignment.MIDDLE_CENTER);
//                
//                 DatasetComponent ds25 = new DatasetComponent(vDs);
//                this.addComponent(ds25);
//                this.setComponentAlignment(ds25, Alignment.MIDDLE_CENTER);
//                
//                 DatasetComponent ds26 = new DatasetComponent(vDs);
//                this.addComponent(ds26);
//                this.setComponentAlignment(ds26, Alignment.MIDDLE_CENTER);
//                
//                 DatasetComponent ds27 = new DatasetComponent(vDs);
//                this.addComponent(ds27);
//                this.setComponentAlignment(ds27, Alignment.MIDDLE_CENTER);
                

            }

        }
    }
    public void selectDataset(PeptideShakerVisualizationDataset ds){
    
    
    }

}
