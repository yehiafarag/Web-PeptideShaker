package com.uib.web.peptideshaker.presenter;

import com.uib.web.peptideshaker.presenter.components.DataViewLayout;
import com.uib.web.peptideshaker.presenter.components.WorkFlowLayout;
import com.uib.web.peptideshaker.presenter.core.BigSideBtn;
import com.uib.web.peptideshaker.presenter.core.SmallSideBtn;
import com.vaadin.event.LayoutEvents;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * This class represent PeptideShaker view presenter which is responsible for
 * viewing the peptide shaker results on web
 *
 * @author Yehia Farag
 */
public class GalaxyFileSystemPresenter extends VerticalLayout implements ViewableFrame, LayoutEvents.LayoutClickListener {

    /**
     * The small side button.
     */
    private final SmallSideBtn toolsBtn;
    /**
     * The small top button.
     */
    private final SmallSideBtn topToolsBtn;
    private VerticalLayout btnContainer;
    private HorizontalLayout mobilebtnContainer;
    private final Map<BigSideBtn, Component> btnsLayoutMap;
    private DataViewLayout dataLayout;

    /**
     * Initialize the web tool main attributes
     *
     * @param searchGUITool SearchGUI web tool
     */
    public GalaxyFileSystemPresenter() {
        GalaxyFileSystemPresenter.this.setSizeFull();
        GalaxyFileSystemPresenter.this.setStyleName("activelayout");
        this.toolsBtn = new SmallSideBtn("img/jobs.png");
        this.toolsBtn.setData(GalaxyFileSystemPresenter.this.getViewId());
        
        this.topToolsBtn = new SmallSideBtn("img/jobs.png");
        this.topToolsBtn.setData(GalaxyFileSystemPresenter.this.getViewId());
        
        this.btnsLayoutMap = new LinkedHashMap<>();
        this.initLayout();
        GalaxyFileSystemPresenter.this.minimizeView();
        
    }
    
    public void setBusy(boolean busy) {
        if (busy) {
            toolsBtn.updateIconURL("img/loading.gif");
            topToolsBtn.updateIconURL("img/loading.gif");
        } else {
            toolsBtn.updateIconURL("img/jobs.png");
            topToolsBtn.updateIconURL("img/jobs.png");
        }
    }
    
    private void initLayout() {
        this.addStyleName("integratedframe");
        btnContainer = new VerticalLayout();
        btnContainer.setWidth(100, Unit.PERCENTAGE);
        btnContainer.setHeightUndefined();
        btnContainer.setSpacing(true);
        btnContainer.setMargin(new MarginInfo(false, false, true, false));
//
        BigSideBtn viewDataBtn = new BigSideBtn("img/jobs.png", "Show Data");
        viewDataBtn.setData("datasetoverview");
        btnContainer.addComponent(viewDataBtn);
        btnContainer.setComponentAlignment(viewDataBtn, Alignment.TOP_CENTER);
        viewDataBtn.addLayoutClickListener(GalaxyFileSystemPresenter.this);
        
        VerticalLayout dataContainerLayout = initDataViewLayout();
        btnsLayoutMap.put(viewDataBtn, dataContainerLayout);
        
        VerticalLayout dataViewFrame = new VerticalLayout();
        dataViewFrame.setSizeFull();
        dataViewFrame.setStyleName("viewframe");
        
        this.addComponent(dataViewFrame);
        this.setExpandRatio(dataViewFrame, 100);
        AbsoluteLayout dataViewFrameContent = new AbsoluteLayout();
        dataViewFrameContent.addStyleName("viewframecontent");
        dataViewFrameContent.setSizeFull();
        dataViewFrame.addComponent(dataViewFrameContent);
        
        dataViewFrameContent.addComponent(dataContainerLayout);
        
        mobilebtnContainer = new HorizontalLayout();
        mobilebtnContainer.setHeight(100, Unit.PERCENTAGE);
        mobilebtnContainer.setWidthUndefined();
        mobilebtnContainer.setSpacing(true);
        mobilebtnContainer.setStyleName("bottomsidebtncontainer");
        
        BigSideBtn viewDataBtnM = new BigSideBtn("img/jobs.png", "Show Data");
        viewDataBtnM.setData("datasetoverview");
        viewDataBtnM.addStyleName("zeropadding");
        mobilebtnContainer.addComponent(viewDataBtnM);
        mobilebtnContainer.setComponentAlignment(viewDataBtnM, Alignment.TOP_CENTER);
        viewDataBtnM.addLayoutClickListener(GalaxyFileSystemPresenter.this);
        viewDataBtnM.setSelected(true);
        viewDataBtn.setSelected(true);
        
    }
    
    @Override
    public VerticalLayout getMainView() {
        return this;
    }
    
    @Override
    public SmallSideBtn getRightView() {
        return toolsBtn;
    }
    
    @Override
    public String getViewId() {
        return GalaxyFileSystemPresenter.class.getName();
    }

    /**
     *
     */
    @Override
    public void minimizeView() {
        toolsBtn.setSelected(false);
        topToolsBtn.setSelected(false);
        this.addStyleName("hidepanel");
        this.btnContainer.removeStyleName("visible");
        this.mobilebtnContainer.addStyleName("hidepanel");
        
    }

    /**
     *
     */
    @Override
    public void maximizeView() {
        toolsBtn.setSelected(true);
        topToolsBtn.setSelected(true);
        this.btnContainer.addStyleName("visible");
        this.mobilebtnContainer.removeStyleName("hidepanel");
        this.removeStyleName("hidepanel");
    }
    
    @Override
    public void layoutClick(LayoutEvents.LayoutClickEvent event) {
        BigSideBtn comp = (BigSideBtn) event.getComponent();
        for (BigSideBtn bbt : btnsLayoutMap.keySet()) {
            if (comp.getData().toString().equalsIgnoreCase(bbt.getData().toString())) {
                bbt.setSelected(true);
                btnsLayoutMap.get(bbt).removeStyleName("hidepanel");
            } else {
                bbt.setSelected(false);
                btnsLayoutMap.get(bbt).addStyleName("hidepanel");
            }
        }
        if (comp.getData().toString().equalsIgnoreCase("nels")) {
        }
    }
    
    @Override
    public VerticalLayout getLeftView() {
        return btnContainer;
    }
    
    @Override
    public HorizontalLayout getBottomView() {
        return mobilebtnContainer;
    }
    
    @Override
    public SmallSideBtn getTopView() {
        return topToolsBtn;
    }
    
    private VerticalLayout initDataViewLayout() {
        VerticalLayout container = new VerticalLayout();
        container.setWidth(100,Unit.PERCENTAGE);
         container.setHeight(100,Unit.PERCENTAGE);
        container.setSpacing(true);
        container.setStyleName("subframe");       
       
        dataLayout = new DataViewLayout();
        container.addComponent(dataLayout);
        
        return container;
    }
    
}
