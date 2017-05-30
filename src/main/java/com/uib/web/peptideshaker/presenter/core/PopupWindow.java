package com.uib.web.peptideshaker.presenter.core;

import com.vaadin.event.LayoutEvents;
import com.vaadin.shared.ui.window.WindowMode;
import com.vaadin.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

/**
 * This Class represents pop-up window that support modal mode
 *
 * @author Yehia Farag
 */
public class PopupWindow extends VerticalLayout implements LayoutEvents.LayoutClickListener{

    private final Window window ;
    /**
     * Constructor to initialize the main layout*
     */
    public PopupWindow(String title,Layout popup) {
        Label titleLabel = new Label(title);
        titleLabel.setStyleName("windowtitle");
        this.addComponent(titleLabel);
        this.addLayoutClickListener(PopupWindow.this);
        
        window = new Window(){
            @Override
            public void close() {
               this.setVisible(false);
            }
        
        
        };
        window.center();
        window.setWindowMode(WindowMode.NORMAL);
        window.setStyleName("popupwindow");
        window.setClosable(true);
        window.setModal(true);
        window.setResizable(false);
        window.setDraggable(false);
        window.setVisible(false);
        UI.getCurrent().addWindow(window);
        window.setContent(popup);
        
    }

    @Override
    public void layoutClick(LayoutEvents.LayoutClickEvent event) {
        window.setVisible(true);
    }
    public void setPopupVisible(boolean visible){
        window.setVisible(visible);
    }
    

}
