package com.uib.web.peptideshaker.presenter.components.peptideshakerview;

import com.uib.web.peptideshaker.presenter.core.BigSideBtn;
import com.vaadin.ui.Component;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * This class represents peptideShaker presenter selection manager
 *
 * @author Yehia Farag
 */
public class SelectionManager {

    private final Map<BigSideBtn, Component> btnsLayoutMap;

    public SelectionManager() {
        this.btnsLayoutMap = new LinkedHashMap<>();
    }

    public void addBtnLayout(BigSideBtn btn, Component layout) {
        btnsLayoutMap.put(btn, layout);
    }

    public void selectBtn(BigSideBtn btn) {
        for (BigSideBtn bbt : btnsLayoutMap.keySet()) {
            if (btn.getData().toString().equalsIgnoreCase(bbt.getData().toString())) {
                bbt.setSelected(true);
                btnsLayoutMap.get(bbt).removeStyleName("hidepanel");
            } else {
                bbt.setSelected(false);
                btnsLayoutMap.get(bbt).addStyleName("hidepanel");
            }
        }

    }

    public void selectBtn(int index) {
        BigSideBtn btn = (BigSideBtn) btnsLayoutMap.keySet().toArray()[index];
        selectBtn(btn);

    }
}
