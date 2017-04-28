package com.uib.web.peptideshaker.presenter;

//import com.github.jmchilton.blend4j.galaxy.GalaxyInstance;
//import com.uib.onlinepeptideshaker.managers.RegistrableView;
//import com.uib.onlinepeptideshaker.presenter.view.SmallSideBtn;
import com.github.jmchilton.blend4j.galaxy.GalaxyInstance;
import com.uib.web.peptideshaker.presenter.components.GalaxyConnectionPanelLayout;
import com.uib.web.peptideshaker.presenter.core.SmallSideBtn;
import com.vaadin.event.LayoutEvents;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Link;
import com.vaadin.ui.PopupView;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
//import sun.security.provider.certpath.Vertex;

/**
 * This class represents the welcome page for Online PeptideShaker
 *
 * @author Yehia Farag
 */
public abstract class WelcomePage extends VerticalLayout implements PresenterViewable {

    /**
     * The body layout panel.
     */
    private final HorizontalLayout bodyPanel;
    /**
     * The header layout panel.
     */
    private final VerticalLayout headerPanel;
    /**
     * The header layout container.
     */
    private final HorizontalLayout headerPanelLayout;
    /**
     * Galaxy connection setting popup.
     */
    private PopupView connectionSettingsPanel;

    /**
     * The galaxy server connection panel.
     */
//    private GalaxyConnectionPanel galaxyInputPanel;
    /**
     * The galaxy server connection panel.
     */
    private SmallSideBtn homeBtn;

    /**
     * Constructor to initialize the layout.
     */
    public WelcomePage() {
        WelcomePage.this.setSizeFull();
        WelcomePage.this.setStyleName("activelayout");
        headerPanel = new VerticalLayout();
        headerPanel.setHeight(50, Unit.PIXELS);
        WelcomePage.this.addComponent(headerPanel);
        WelcomePage.this.setComponentAlignment(headerPanel, Alignment.MIDDLE_LEFT);
        WelcomePage.this.setExpandRatio(headerPanel, 10);
        headerPanelLayout = initializeHeaderPanel();
        headerPanel.addComponent(headerPanelLayout);
        headerPanel.setMargin(new MarginInfo(false, false, false, false));

        bodyPanel = new HorizontalLayout();
        bodyPanel.setSizeFull();
        bodyPanel.setMargin(new MarginInfo(true, false, false, false));
        WelcomePage.this.addComponent(bodyPanel);
        WelcomePage.this.setComponentAlignment(bodyPanel, Alignment.TOP_CENTER);
        WelcomePage.this.setExpandRatio(bodyPanel, 70);

        VerticalLayout bodyContent = new VerticalLayout();
        bodyContent.setSizeFull();
        bodyContent.setSpacing(true);
        bodyContent.setWidth(405, Unit.PIXELS);
        bodyContent.setHeight(130, Unit.PIXELS);
        bodyPanel.addComponent(bodyContent);
        bodyPanel.setComponentAlignment(bodyContent, Alignment.TOP_CENTER);

        Label welcomeText = new Label();
        welcomeText.setSizeFull();
        welcomeText.setContentMode(ContentMode.HTML);
        welcomeText.setStyleName(ValoTheme.LABEL_NO_MARGIN);
        welcomeText.setValue("<font style='font-weight: bold; font-size:23px'>Welcome to PeptideShaker <font size='2'><i>(online version)</i></font></font> <br/><br/>To start using the system connect to your Galaxy Server");
        bodyContent.addComponent(welcomeText);
        bodyContent.setComponentAlignment(welcomeText, Alignment.TOP_LEFT);

        HorizontalLayout bottomLayout = new HorizontalLayout();
        bottomLayout.setSizeFull();
        bottomLayout.setSpacing(true);
        bodyContent.addComponent(bottomLayout);
        bodyContent.setComponentAlignment(bottomLayout, Alignment.TOP_LEFT);

        Label connectionStatuesLabel = new Label("Galaxy is<font color='red'>  not connected </font><font size='3' color='red'> &#128528;</font>");
        connectionStatuesLabel.setContentMode(ContentMode.HTML);

        connectionStatuesLabel.setHeight(20, Unit.PIXELS);
        connectionStatuesLabel.setWidth(160, Unit.PIXELS);
        connectionStatuesLabel.setStyleName(ValoTheme.LABEL_SMALL);
        connectionStatuesLabel.addStyleName(ValoTheme.LABEL_BOLD);
        connectionStatuesLabel.addStyleName(ValoTheme.LABEL_TINY);
        connectionStatuesLabel.addStyleName(ValoTheme.LABEL_NO_MARGIN);
        bottomLayout.addComponent(connectionStatuesLabel);
        bottomLayout.setComponentAlignment(connectionStatuesLabel, Alignment.BOTTOM_LEFT);

        HorizontalLayout galaxyConnectionBtnContainer = new HorizontalLayout();
        galaxyConnectionBtnContainer.setWidthUndefined();
        galaxyConnectionBtnContainer.setHeight(100, Unit.PERCENTAGE);
        galaxyConnectionBtnContainer.setSpacing(false);
        bottomLayout.addComponent(galaxyConnectionBtnContainer);
        bottomLayout.setComponentAlignment(galaxyConnectionBtnContainer, Alignment.BOTTOM_RIGHT);

        Button connectionBtn = new Button("Connect");
        connectionBtn.setDisableOnClick(true);
        connectionBtn.setStyleName("galaxybtn");
        connectionBtn.addStyleName(ValoTheme.BUTTON_LINK);
        connectionBtn.setDescription("Connect / Disconnect to Galaxy server");
        connectionBtn.addStyleName(ValoTheme.LABEL_NO_MARGIN);
        connectionBtn.setWidth(150, Unit.PIXELS);
        connectionBtn.setHeight(25, Unit.PIXELS);
        galaxyConnectionBtnContainer.addComponent(connectionBtn);
        galaxyConnectionBtnContainer.setComponentAlignment(connectionBtn, Alignment.BOTTOM_RIGHT);

        VerticalLayout settingBtn = new VerticalLayout();
        settingBtn.addStyleName("settingbtn");
        settingBtn.setDescription("Galaxy server settings");
        settingBtn.setWidth(25, Unit.PIXELS);
        settingBtn.setHeight(25, Unit.PIXELS);
        galaxyConnectionBtnContainer.addComponent(settingBtn);
        galaxyConnectionBtnContainer.setComponentAlignment(settingBtn, Alignment.BOTTOM_RIGHT);

        GalaxyConnectionPanelLayout galaxyConnectionSettingsPanel = new GalaxyConnectionPanelLayout() {
            @Override
            public void connectedToGalaxy(GalaxyInstance galaxyInstant) {
                if (galaxyInstant != null) {
                    connectionBtn.setCaption("Disconnect");
                    connectionBtn.addStyleName("disconnect");
                    connectionStatuesLabel.setValue("Galaxy is <font color='green'>connected </font><font size='3' color='green'> &#128522;</font>");
                } else {
                    connectionSettingsPanel.setPopupVisible(true);
                }
                systemConnected(galaxyInstant);
                connectionBtn.setEnabled(true);
            }

            @Override
            public void hideGalaxyPanel() {
                connectionSettingsPanel.setPopupVisible(false);
            }

        };
        connectionSettingsPanel = new PopupView(null, galaxyConnectionSettingsPanel);
        connectionSettingsPanel.setSizeFull();
        connectionSettingsPanel.setHideOnMouseOut(false);
        settingBtn.addComponent(connectionSettingsPanel);

       

        connectionBtn.addClickListener((Button.ClickEvent event) -> {

            if (connectionBtn.getCaption().equalsIgnoreCase("Disconnect")) {
                //disconnect from galaxy
                connectionBtn.setCaption("Connect");
                connectionBtn.removeStyleName("disconnect");
                connectionStatuesLabel.setValue("Galaxy is<font color='red'>  not connected </font><font size='3' color='red'> &#128528;</font>");
                galaxyConnectionSettingsPanel.disconnectGalaxy();
                systemConnected(null);

            } else {
                //connect to galaxy
                galaxyConnectionSettingsPanel.validateAndConnect();
            }

        });
        homeBtn = new SmallSideBtn("img/home-o.png");
        homeBtn.setData(WelcomePage.this.getViewId());

    }

    /**
     * Initialize the header layout.
     */
    private HorizontalLayout initializeHeaderPanel() {
        HorizontalLayout headerLayoutContainer = new HorizontalLayout();
        headerLayoutContainer.setSpacing(true);
        Image peptideShakerLogoIcon = new Image();
        peptideShakerLogoIcon.setSource(new ThemeResource("img/peptideshakericon.png"));
        peptideShakerLogoIcon.setHeight(100, Unit.PIXELS);
        headerLayoutContainer.addComponent(peptideShakerLogoIcon);
        headerLayoutContainer.setComponentAlignment(peptideShakerLogoIcon, Alignment.MIDDLE_LEFT);

        Link headerLogoLabel = new Link("PeptideShaker <font>(Online Version)</font>", new ExternalResource(""));
        headerLayoutContainer.addComponent(headerLogoLabel);
        headerLogoLabel.setCaptionAsHtml(true);
        headerLayoutContainer.setComponentAlignment(headerLogoLabel, Alignment.MIDDLE_LEFT);
        headerLogoLabel.setStyleName("headerlogo");
        return headerLayoutContainer;
    }

    public abstract void systemConnected(GalaxyInstance galaxyInstant);

    @Override
    public String getViewId() {
        return WelcomePage.class.getName();
    }

    @Override
    public void minimizeView() {
        homeBtn.setSelected(false);
        this.addStyleName("hidepanel");
    }

    @Override
    public void maximizeView() {
        homeBtn.setSelected(true);
        this.removeStyleName("hidepanel");
    }

    @Override
    public SmallSideBtn getRightView() {
        return homeBtn;
    }

    @Override
    public VerticalLayout getMainView() {
        return this;
    }

    @Override
    public VerticalLayout getLeftView() {
        return new VerticalLayout();
    }

    @Override
    public HorizontalLayout getBottomView() {
        return new HorizontalLayout();
    }
    

}
