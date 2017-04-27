package com.uib.web.peptideshaker;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

/**
 * This UI is the application entry point. A UI may either represent a browser
 * window (or tab) or some part of a html page where a Vaadin application is
 * embedded.
 * <p>
 * The UI is initialized using {@link #init(VaadinRequest)}. This method is
 * intended to be overridden to add component to the user interface and
 * initialize non-component functionality.
 */
@Theme("webpeptideshakertheme")
public class PeptidShakerUI extends UI {

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        this.setSizeFull();

        System.out.println("at w " + Page.getCurrent().getWebBrowser().getScreenWidth() + "  h " + Page.getCurrent().getWebBrowser().getScreenHeight() + "  " + Page.getCurrent().getWebBrowser().isTouchDevice());
        WebPeptideShakerApp webPeptideShakerApp = new WebPeptideShakerApp();
        if (Page.getCurrent().getBrowserWindowWidth() < Page.getCurrent().getBrowserWindowHeight()) {
            webPeptideShakerApp.addStyleName("horizontalcss");
            System.out.println("at page resized " + Page.getCurrent().getBrowserWindowWidth() + "  h " + Page.getCurrent().getBrowserWindowHeight());
        } else {
            webPeptideShakerApp.removeStyleName("horizontalcss");
        }
        Page.getCurrent().addBrowserWindowResizeListener(new Page.BrowserWindowResizeListener() {
            @Override
            public void browserWindowResized(Page.BrowserWindowResizeEvent event) {
                if (Page.getCurrent().getBrowserWindowWidth() < Page.getCurrent().getBrowserWindowHeight()) {
                    webPeptideShakerApp.addStyleName("horizontalcss");
                    System.out.println("at page resized " + Page.getCurrent().getBrowserWindowWidth() + "  h " + Page.getCurrent().getBrowserWindowHeight());
                } else {
                    webPeptideShakerApp.removeStyleName("horizontalcss");
                }
            }
        });

        setContent(webPeptideShakerApp);
    }

    @WebServlet(urlPatterns = "/*", name = "PeptidShakerUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = PeptidShakerUI.class, productionMode = false)
    public static class PeptidShakerUIServlet extends VaadinServlet {
    }
}
