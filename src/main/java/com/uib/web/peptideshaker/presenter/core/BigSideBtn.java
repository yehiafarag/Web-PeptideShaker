
package com.uib.web.peptideshaker.presenter.core;

import com.vaadin.server.ThemeResource;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Image;

/**
 * This class represent the top right small button
 *
 * @author Yehia Farag
 */
public class BigSideBtn extends AbsoluteLayout {

    public BigSideBtn(String iconUrl) {
        Image icon = new Image();
        icon.setSource(new ThemeResource(iconUrl));
        icon.setSizeFull();
        BigSideBtn.this.addComponent(icon);
        BigSideBtn.this.setSizeFull();
        BigSideBtn.this.setStyleName("bigmenubtn");
    }

    public void setSelected(boolean selected) {
        if (selected) {
            BigSideBtn.this.addStyleName("selectedbiglbtn");
        } else {
            BigSideBtn.this.removeStyleName("selectedbiglbtn");
        }
    }

}
