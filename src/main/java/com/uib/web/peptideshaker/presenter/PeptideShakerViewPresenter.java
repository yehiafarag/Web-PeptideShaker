package com.uib.web.peptideshaker.presenter;

import com.uib.web.peptideshaker.galaxy.PeptideShakerVisualizationDataset;
import com.uib.web.peptideshaker.presenter.components.peptideshakerview.PeptideShakerDatasesViewLayout;
import com.uib.web.peptideshaker.presenter.components.peptideshakerview.SelectionManager;
import com.uib.web.peptideshaker.presenter.core.BigSideBtn;
import com.uib.web.peptideshaker.presenter.core.SmallSideBtn;
import com.vaadin.event.LayoutEvents;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * This class represent PeptideShaker view presenter which is responsible for
 * viewing the peptide shaker results on web
 *
 * @author Yehia Farag
 */
public class PeptideShakerViewPresenter extends VerticalLayout implements ViewableFrame, LayoutEvents.LayoutClickListener {

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
    
    private PeptideShakerDatasesViewLayout datasetOverviewLayout;
    private final SelectionManager Selection_Manager;

    /**
     * Initialize the web tool main attributes
     *
     * @param searchGUITool SearchGUI web tool
     */
    public PeptideShakerViewPresenter() {
        PeptideShakerViewPresenter.this.setSizeFull();
        PeptideShakerViewPresenter.this.setStyleName("activelayout");
        this.toolsBtn = new SmallSideBtn("img/cluster.svg");
        this.toolsBtn.setData(PeptideShakerViewPresenter.this.getViewId());

        this.topToolsBtn = new SmallSideBtn("img/cluster.svg");
        this.topToolsBtn.setData(PeptideShakerViewPresenter.this.getViewId());        
        this.Selection_Manager = new SelectionManager();
        this.initLayout();
        PeptideShakerViewPresenter.this.minimizeView();

    }

    private void initLayout() {
        this.addStyleName("integratedframe");
        btnContainer = new VerticalLayout();
        btnContainer.setWidth(100, Unit.PERCENTAGE);
        btnContainer.setHeightUndefined();
        btnContainer.setSpacing(true);
        btnContainer.setMargin(new MarginInfo(false, false, true, false));
//
        BigSideBtn datasetsOverviewBtn = new BigSideBtn("img/graph.png", "Get Data");
        datasetsOverviewBtn.setData("datasetoverview");
        btnContainer.addComponent(datasetsOverviewBtn);
        btnContainer.setComponentAlignment(datasetsOverviewBtn, Alignment.TOP_CENTER);
        datasetsOverviewBtn.addLayoutClickListener(PeptideShakerViewPresenter.this);

        datasetOverviewLayout = new PeptideShakerDatasesViewLayout();
        datasetOverviewLayout.setSizeFull();
        Selection_Manager.addBtnLayout(datasetsOverviewBtn, datasetOverviewLayout);

        BigSideBtn proteinsOverviewBtn = new BigSideBtn("img/proteins.png", "Get Data");
        proteinsOverviewBtn.setData("proteinoverview");
        btnContainer.addComponent(proteinsOverviewBtn);
        btnContainer.setComponentAlignment(proteinsOverviewBtn, Alignment.TOP_CENTER);
        proteinsOverviewBtn.addLayoutClickListener(PeptideShakerViewPresenter.this);

        VerticalLayout proteinsOverviewLayout = new VerticalLayout();
        Selection_Manager.addBtnLayout(proteinsOverviewBtn, proteinsOverviewLayout);

        BigSideBtn peptidesOverviewBtn = new BigSideBtn("img/peptides.png", "Get Data");
        peptidesOverviewBtn.setData("peptidesoverview");
        btnContainer.addComponent(peptidesOverviewBtn);
        btnContainer.setComponentAlignment(peptidesOverviewBtn, Alignment.TOP_CENTER);
        peptidesOverviewBtn.addLayoutClickListener(PeptideShakerViewPresenter.this);

        VerticalLayout peptidesOverviewLayout = new VerticalLayout();
        Selection_Manager.addBtnLayout(peptidesOverviewBtn, peptidesOverviewLayout);

        VerticalLayout toolViewFrame = new VerticalLayout();
        toolViewFrame.setSizeFull();
        toolViewFrame.setStyleName("viewframe");

        this.addComponent(toolViewFrame);
        this.setExpandRatio(toolViewFrame, 100);

        AbsoluteLayout toolViewFrameContent = new AbsoluteLayout();
        toolViewFrameContent.addStyleName("viewframecontent");
        toolViewFrameContent.setSizeFull();
        toolViewFrame.addComponent(toolViewFrameContent);

        toolViewFrameContent.addComponent(datasetOverviewLayout);
        toolViewFrameContent.addComponent(proteinsOverviewLayout);
        toolViewFrameContent.addComponent(peptidesOverviewLayout);

        mobilebtnContainer = new HorizontalLayout();
        mobilebtnContainer.setHeight(100, Unit.PERCENTAGE);
        mobilebtnContainer.setWidthUndefined();
        mobilebtnContainer.setSpacing(true);
        mobilebtnContainer.setStyleName("bottomsidebtncontainer");

        BigSideBtn datasetsOverviewBtnM = new BigSideBtn("img/graph.png", "Work-Flow");
        datasetsOverviewBtnM.setData("datasetoverview");
        datasetsOverviewBtnM.addStyleName("zeropadding");
        mobilebtnContainer.addComponent(datasetsOverviewBtnM);
        mobilebtnContainer.setComponentAlignment(datasetsOverviewBtnM, Alignment.TOP_CENTER);
        datasetsOverviewBtnM.addLayoutClickListener(PeptideShakerViewPresenter.this);
        datasetsOverviewBtnM.setSelected(true);
        datasetsOverviewBtn.setSelected(true);

        BigSideBtn proteinsOverviewBtnM = new BigSideBtn("img/proteins.png", "Work-Flow");
        datasetsOverviewBtnM.setData("proteinsoverview");
        datasetsOverviewBtnM.addStyleName("zeropadding");
        mobilebtnContainer.addComponent(proteinsOverviewBtnM);
        mobilebtnContainer.setComponentAlignment(proteinsOverviewBtnM, Alignment.TOP_CENTER);
        proteinsOverviewBtnM.addLayoutClickListener(PeptideShakerViewPresenter.this);

        BigSideBtn peptidesOverviewBtnM = new BigSideBtn("img/peptides.png", "Work-Flow");
        peptidesOverviewBtnM.setData("peptidesoverview");
        peptidesOverviewBtnM.addStyleName("zeropadding");
        mobilebtnContainer.addComponent(peptidesOverviewBtnM);
        mobilebtnContainer.setComponentAlignment(peptidesOverviewBtnM, Alignment.TOP_CENTER);
        peptidesOverviewBtnM.addLayoutClickListener(PeptideShakerViewPresenter.this);

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
        return PeptideShakerViewPresenter.class.getName();
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
        Selection_Manager.selectBtn(comp);

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

    /**
     * Update the dataset overview layout.
     *
     * @param peptideShakerVisualizationMap map of visualization datasets
     */
    public void updateData(Map<String, PeptideShakerVisualizationDataset> peptideShakerVisualizationMap) {

        datasetOverviewLayout.updateData(peptideShakerVisualizationMap);

    }

    public void setSelectedDataset(PeptideShakerVisualizationDataset ds) {
         Selection_Manager.selectBtn(1);
        datasetOverviewLayout.selectDataset(ds);
    }
}
