package com.uib.web.peptideshaker.presenter;

import com.uib.web.peptideshaker.presenter.components.WorkFlowLayout;
import com.uib.web.peptideshaker.presenter.core.BigSideBtn;
import com.uib.web.peptideshaker.presenter.core.SmallSideBtn;
import com.vaadin.event.LayoutEvents;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import java.util.HashMap;
import java.util.Map;

/**
 * This class represent web tool presenter which is responsible for managing the
 * view and interactivity of the tool
 *
 * @author Yehia Farag
 */
public class ToolPresenter extends VerticalLayout implements PresenterViewable, LayoutEvents.LayoutClickListener {

    /**
     * The tools layout side button.
     */
    private SmallSideBtn toolsBtn;
    /**
     * The tools layout top button.
     */
    private SmallSideBtn topToolsBtn;
    private final Map<BigSideBtn,Component> btnsLayoutMap;
//    private VerticalLayout rightLayoutContainer;
    private  VerticalLayout btnContainer ;
    private HorizontalLayout mobilebtnContainer;

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
        
         ToolPresenter.this.topToolsBtn = new SmallSideBtn("img/spectra.png");
        ToolPresenter.this.topToolsBtn.setData(ToolPresenter.this.getViewId());
        
        
        
        this.btnsLayoutMap = new HashMap<>();
        this.initLayout();
        ToolPresenter.this.minimizeView();

    }

    private void initLayout() {
//        HorizontalLayout container = new HorizontalLayout();
//        container.setSizeFull();
//
//        this.addComponent(container);

//        AbsoluteLayout leftLayoutContainer = new AbsoluteLayout();
//        leftLayoutContainer.setSizeFull();
//        leftLayoutContainer.setStyleName("leftsidebtncontainer");
//        container.addComponent(leftLayoutContainer);
//        container.setExpandRatio(leftLayoutContainer, 10);

//        rightLayoutContainer = new VerticalLayout();
//        rightLayoutContainer.setSizeFull();
//        rightLayoutContainer.addStyleName("hide");
        this.addStyleName("integratedframe");
//        container.addComponent(rightLayoutContainer);
//        container.setExpandRatio(rightLayoutContainer, 100);

        btnContainer = new VerticalLayout();
        btnContainer.setWidth(100, Unit.PERCENTAGE);
        btnContainer.setHeightUndefined();
        btnContainer.setSpacing(true);
        btnContainer.setMargin(new MarginInfo(false, false, true, false));
//        leftLayoutContainer.addComponent(btnContainer);
        
        WorkFlowLayout workflowLayout = new WorkFlowLayout();
        VerticalLayout nelsLayout = new VerticalLayout();

        BigSideBtn nelsBtn = new BigSideBtn("img/NeLS2.png", "Get Data");
        nelsBtn.setData("nels");
        btnContainer.addComponent(nelsBtn);
        btnContainer.setComponentAlignment(nelsBtn, Alignment.TOP_CENTER);
        nelsBtn.addLayoutClickListener(ToolPresenter.this);
        btnsLayoutMap.put(nelsBtn,nelsLayout);

        BigSideBtn workFlowBtn = new BigSideBtn("img/workflow.png", "Work-Flow");
        workFlowBtn.setData("workflow");
        workFlowBtn.addStyleName("zeropadding");
        btnContainer.addComponent(workFlowBtn);
        btnContainer.setComponentAlignment(workFlowBtn, Alignment.TOP_CENTER);
        workFlowBtn.addLayoutClickListener(ToolPresenter.this);
        workFlowBtn.setSelected(true);
        btnsLayoutMap.put(workFlowBtn,workflowLayout);

//        BigSideBtn searchGUIBtn = new BigSideBtn("img/searchgui.png","SearchGUI");
//        searchGUIBtn.setData("searchgui");
//        btnContainer.addComponent(searchGUIBtn);
//        btnContainer.setComponentAlignment(searchGUIBtn, Alignment.TOP_CENTER);
//        searchGUIBtn.addLayoutClickListener(ToolPresenter.this);
//        btnsSet.add(searchGUIBtn);
//        
//        BigSideBtn peptideShakerBtn = new BigSideBtn("img/peptideshaker.png","PeptideShaker");
//        peptideShakerBtn.setData("peptideshaker");
//        btnContainer.addComponent(peptideShakerBtn);
//        btnContainer.setComponentAlignment(peptideShakerBtn, Alignment.TOP_CENTER);
//        peptideShakerBtn.addLayoutClickListener(ToolPresenter.this);
//        btnsSet.add(peptideShakerBtn);
//        
        VerticalLayout toolViewFrame = new VerticalLayout();
        toolViewFrame.setSizeFull();
        toolViewFrame.setStyleName("viewframe");

        this.addComponent(toolViewFrame);
        this.setExpandRatio(toolViewFrame, 100);

        AbsoluteLayout toolViewFrameContent = new AbsoluteLayout();
        toolViewFrameContent.addStyleName("viewframecontent");
        toolViewFrameContent.setSizeFull();
        toolViewFrame.addComponent(toolViewFrameContent);
        
        toolViewFrameContent.addComponent(nelsLayout);
        toolViewFrameContent.addComponent(workflowLayout);
        

        mobilebtnContainer = new HorizontalLayout();
        mobilebtnContainer.setHeight(100, Unit.PERCENTAGE);
        mobilebtnContainer.setWidthUndefined();
        mobilebtnContainer.setSpacing(true);
        mobilebtnContainer.setStyleName("bottomsidebtncontainer");
//        mobilebtnContainer.setMargin(new MarginInfo(false, false, true, false));
//        rightLayoutContainer.addComponent(mobilebtnContainer);
//        rightLayoutContainer.setComponentAlignment(mobilebtnContainer, Alignment.TOP_CENTER);
//        rightLayoutContainer.setExpandRatio(mobilebtnContainer, 3);

        BigSideBtn nelsBtnM = new BigSideBtn("img/NeLS.png", "Get Data");
        nelsBtnM.setData("nels");
        mobilebtnContainer.addComponent(nelsBtnM);
        mobilebtnContainer.setComponentAlignment(nelsBtnM, Alignment.TOP_CENTER);
        nelsBtnM.addLayoutClickListener(ToolPresenter.this);
        btnsLayoutMap.put(nelsBtnM,nelsLayout);

        BigSideBtn workFlowBtnM = new BigSideBtn("img/workflow3.png", "Work-Flow");
        workFlowBtnM.setData("workflow");
        workFlowBtnM.addStyleName("zeropadding");
        mobilebtnContainer.addComponent(workFlowBtnM);
        mobilebtnContainer.setComponentAlignment(workFlowBtnM, Alignment.TOP_CENTER);
        workFlowBtnM.addLayoutClickListener(ToolPresenter.this);
        workFlowBtnM.setSelected(true);
        btnsLayoutMap.put(workFlowBtnM,workflowLayout);
        
        
        

//        BigSideBtn searchGUIBtnM = new BigSideBtn("img/searchgui.png","SearchGUI");
//        searchGUIBtnM.setData("searchgui");
//        mobilebtnContainer.addComponent(searchGUIBtnM);
//        mobilebtnContainer.setComponentAlignment(searchGUIBtnM, Alignment.TOP_CENTER);
//        searchGUIBtnM.addLayoutClickListener(ToolPresenter.this);
//        btnsSet.add(searchGUIBtnM);
//        BigSideBtn peptideShakerBtnM = new BigSideBtn("img/peptideshaker.png","PeptideShaker");
//        peptideShakerBtnM.setData("peptideshaker");
//        mobilebtnContainer.addComponent(peptideShakerBtnM);
//        mobilebtnContainer.setComponentAlignment(peptideShakerBtnM, Alignment.TOP_CENTER);
//        peptideShakerBtnM.addLayoutClickListener(ToolPresenter.this);
//        btnsSet.add(peptideShakerBtnM);
//        this.rightLayoutBtnsContainer = new VerticalLayout();
//        rightLayoutBtnsContainer.setSizeFull();
//        rightLayoutContainer.addComponent(this.rightLayoutBtnsContainer);
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
        return ToolPresenter.class.getName();
    }

    @Override
    public void minimizeView() {
        toolsBtn.setSelected(false);
        topToolsBtn.setSelected(false);
        this.addStyleName("hidepanel");
        this.btnContainer.removeStyleName("visible");
          this.mobilebtnContainer.addStyleName("hidepanel"); 

    }

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
//        rightLayoutContainer.removeStyleName("hide");
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
        return  mobilebtnContainer;
    }

    @Override
    public SmallSideBtn getTopView() {
        return topToolsBtn;
    }
    

}
