/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.uib.web.peptideshaker.presenter.core;

import com.vaadin.event.LayoutEvents;
import com.vaadin.shared.ui.orderedlayout.VerticalLayoutState;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

/**
 * This class represents Label with link that support action
 *
 * @author Yehia Farag
 */
public class ActionLabel extends VerticalLayout implements LayoutEvents.LayoutClickListener {

    public ActionLabel(String value) {
        this.setSizeFull();
        Label label = new Label(value);
        this.addComponent(label);
        this.addLayoutClickListener(this);

    }

    @Override
    public void layoutClick(LayoutEvents.LayoutClickEvent event) {
        System.out.println("item clicked ");

    }

    @Override
    protected VerticalLayoutState getState() {
        VerticalLayoutState state = super.getState();
        if (state != null) {
////            System.out.println("at getstate " + state.enabled);
        }
        return state;//To change body of generated methods, choose Tools | Templates.
    }

   

}
