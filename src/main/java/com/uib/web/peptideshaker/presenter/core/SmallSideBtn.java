
package com.uib.web.peptideshaker.presenter.core;

import com.vaadin.server.ThemeResource;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Image;

/**
 * This class represent the top right small button
 *
 * @author Yehia Farag
 */
public class SmallSideBtn extends AbsoluteLayout {

    public SmallSideBtn(String iconUrl) {
        Image icon = new Image();
        icon.setSource(new ThemeResource(iconUrl));
        icon.setSizeFull();
        SmallSideBtn.this.addComponent(icon);
        SmallSideBtn.this.setSizeFull();
        SmallSideBtn.this.setStyleName("smallmenubtn");
    }

    public void setSelected(boolean selected) {
        if (selected) {
            SmallSideBtn.this.addStyleName("selectedsmallbtn");
        } else {
            SmallSideBtn.this.removeStyleName("selectedsmallbtn");
        }
    }

}
