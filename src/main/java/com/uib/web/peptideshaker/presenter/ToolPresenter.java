package com.uib.web.peptideshaker.presenter;

import com.uib.web.peptideshaker.presenter.core.BigSideBtn;
import com.uib.web.peptideshaker.presenter.core.SmallSideBtn;
import com.vaadin.event.LayoutEvents;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import java.util.HashSet;
import java.util.Set;

/**
 * This class represent web tool presenter which is responsible for managing the
 * view and interactivity of the tool
 *
 * @author Yehia Farag
 */
public class ToolPresenter extends VerticalLayout implements PresenterViewable, LayoutEvents.LayoutClickListener {

    /**
     * The galaxy server connection panel.
     */
    private SmallSideBtn toolsBtn;
    private final Set<BigSideBtn>btnsSet;

    /**
     * Initialize the web tool main attributes
     *
     * @param searchGUITool SearchGUI web tool
     */
    public ToolPresenter() {
        ToolPresenter.this.setSizeFull();
        ToolPresenter.this.setStyleName("activelayout");
        ToolPresenter.this.toolsBtn = new SmallSideBtn("img/spectra.png");
        ToolPresenter.this.toolsBtn.setData(ToolPresenter.this.getViewId());
        ToolPresenter.this.minimizeView();
        this.btnsSet = new HashSet<>();
        this.initLayout();
        
    }
    
    private void initLayout() {
        HorizontalLayout container = new HorizontalLayout();
        container.setSizeFull();
        this.addComponent(container);
        
        AbsoluteLayout leftLayoutContainer = new AbsoluteLayout();
        leftLayoutContainer.setSizeFull();
        container.addComponent(leftLayoutContainer);
        container.setExpandRatio(leftLayoutContainer, 5);
        
        AbsoluteLayout rightLayoutContainer = new AbsoluteLayout();
        rightLayoutContainer.setSizeFull();
        leftLayoutContainer.setStyleName("leftsidebtncontainer");
        rightLayoutContainer.addStyleName("hide");
        container.addComponent(rightLayoutContainer);
        container.setExpandRatio(rightLayoutContainer, 95);
        
        VerticalLayout marker = new VerticalLayout();
        marker.setWidth(2, Unit.PIXELS);
        marker.setHeight(80, Unit.PERCENTAGE);
        marker.setStyleName("lightgraylayout");
        leftLayoutContainer.addComponent(marker, "left: 50%; top: 16px;");
        
        VerticalLayout btnContainer = new VerticalLayout();
        btnContainer.setSizeFull();
        leftLayoutContainer.addComponent(btnContainer);
        
        BigSideBtn nelsBtn = new BigSideBtn("img/NeLS.png");
        nelsBtn.setData("nels");
        btnContainer.addComponent(nelsBtn);
        btnContainer.setComponentAlignment(nelsBtn, Alignment.TOP_CENTER);
        nelsBtn.addLayoutClickListener(ToolPresenter.this);
        btnsSet.add(nelsBtn);
        
        BigSideBtn searchGUIBtn = new BigSideBtn("img/NeLS.png");
        searchGUIBtn.setData("searchgui");
        btnContainer.addComponent(searchGUIBtn);
        btnContainer.setComponentAlignment(searchGUIBtn, Alignment.TOP_CENTER);
        searchGUIBtn.addLayoutClickListener(ToolPresenter.this);
        btnsSet.add(searchGUIBtn);
        
        BigSideBtn peptideShakerBtn = new BigSideBtn("img/NeLS.png");
        peptideShakerBtn.setData("peptideshaker");
        btnContainer.addComponent(peptideShakerBtn);
        btnContainer.setComponentAlignment(peptideShakerBtn, Alignment.TOP_CENTER);
        peptideShakerBtn.addLayoutClickListener(ToolPresenter.this);
        btnsSet.add(peptideShakerBtn);
        
        BigSideBtn workFlowBtn = new BigSideBtn("img/NeLS.png");
        workFlowBtn.setData("workflow");
        btnContainer.addComponent(workFlowBtn);
        btnContainer.setComponentAlignment(workFlowBtn, Alignment.TOP_CENTER);
        workFlowBtn.addLayoutClickListener(ToolPresenter.this);
        btnsSet.add(workFlowBtn);

//        this.rightLayoutBtnsContainer = new VerticalLayout();
//        rightLayoutBtnsContainer.setSizeFull();
//        rightLayoutContainer.addComponent(this.rightLayoutBtnsContainer);
    }
    
    @Override
    public VerticalLayout getMainView() {
        return this;
    }
    
    @Override
    public SmallSideBtn getControlButton() {
        return toolsBtn;
    }
    
    @Override
    public String getViewId() {
        return ToolPresenter.class.getName();
    }
    
    @Override
    public void minimizeView() {
        toolsBtn.setSelected(false);
        this.addStyleName("hidepanel");
        
    }
    
    @Override
    public void maximizeView() {
        toolsBtn.setSelected(true);
        this.removeStyleName("hidepanel");
    }
    
    @Override
    public void layoutClick(LayoutEvents.LayoutClickEvent event) {
        for(BigSideBtn bbt :btnsSet)
            bbt.setSelected(false);
        BigSideBtn comp = (BigSideBtn) event.getComponent();
        comp.setSelected(true);
//        if (comp.getData().toString().equalsIgnoreCase("nels")) {
            System.out.println("at selected "+comp.getData().toString());
//        }
    }
    
}
