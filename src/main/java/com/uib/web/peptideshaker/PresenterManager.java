package com.uib.web.peptideshaker;

import com.uib.web.peptideshaker.presenter.PresenterViewable;
import com.vaadin.event.LayoutEvents;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * This class represents the main layout of the application and main view
 * controller manager
 *
 * @author Yehia Farag
 */
public class PresenterManager extends HorizontalLayout implements LayoutEvents.LayoutClickListener {

    /**
     * Left layout container is the main layout container that contain the main
     * views.
     */
    private final AbsoluteLayout leftLayoutContainer;
    /**
     * Right layout container is the right side buttons layout container that
     * contain the small control buttons.
     */
    private final VerticalLayout rightLayoutBtnsContainer;
    /**
     * Right layout container is the right side buttons layout container that
     * contain the small control buttons.
     */
    private final   AbsoluteLayout rightLayoutContainer;
    /**
     * Map of current registered views.
     */
    private final Map<String, PresenterViewable> visualizationMap = new LinkedHashMap<>();

    /**
     * Constructor to initialize the layout
     */
    public PresenterManager() {
        PresenterManager.this.setSizeFull();
        PresenterManager.this.setStyleName("mainlayout");

        leftLayoutContainer = new AbsoluteLayout();
        leftLayoutContainer.setSizeFull();
        PresenterManager.this.addComponent(leftLayoutContainer);
        PresenterManager.this.setExpandRatio(leftLayoutContainer, 97);
 
        rightLayoutContainer = new AbsoluteLayout();
        rightLayoutContainer.setSizeFull();
        rightLayoutContainer.setStyleName("rightsidebtncontainer");
        rightLayoutContainer.addStyleName("hide");         
        PresenterManager.this.addComponent(rightLayoutContainer);
        PresenterManager.this.setExpandRatio(rightLayoutContainer, 3);

        VerticalLayout marker = new VerticalLayout();
        marker.setWidth(2, Unit.PIXELS);
        marker.setHeight(80, Unit.PERCENTAGE);
        marker.setStyleName("lightgraylayout");
        rightLayoutContainer.addComponent(marker, "left: 50%; top: 16px;");
        
        this.rightLayoutBtnsContainer = new VerticalLayout();
        rightLayoutBtnsContainer.setSizeFull();
        rightLayoutContainer.addComponent( this.rightLayoutBtnsContainer);

    }

    /**
     * Hide / show side buttons .
     *
     * @param showSideButtons boolean show the buttons.
     */
    public void setSideButtonsVisible(boolean showSideButtons) {
        if (showSideButtons) {
            rightLayoutContainer.removeStyleName("hide");
        } else {
            rightLayoutContainer.addStyleName("hide");
        }

    }

    /**
     * Register view into the view management system.
     *
     * @param view visualization layout.
     */
    public void registerView(PresenterViewable view) {
        view.getControlButton().addLayoutClickListener(PresenterManager.this);
        visualizationMap.put(view.getViewId(), view);
        rightLayoutBtnsContainer.addComponent(view.getControlButton());
        rightLayoutBtnsContainer.setComponentAlignment(view.getControlButton(), Alignment.TOP_CENTER);
        leftLayoutContainer.addComponent(view.getMainView());
    }

    /**
     * View only selected view and hide the rest of registered layout
     *
     * @param viewId selected view id
     */
    public void viewLayout(String viewId) {
        for (PresenterViewable view : visualizationMap.values()) {
            view.minimizeView();
        }
        visualizationMap.get(viewId).maximizeView();

    }

    @Override
    public void layoutClick(LayoutEvents.LayoutClickEvent event) {
        this.viewLayout(((AbsoluteLayout) event.getComponent()).getData().toString());
    }

}
