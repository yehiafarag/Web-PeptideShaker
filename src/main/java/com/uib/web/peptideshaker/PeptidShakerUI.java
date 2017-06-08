package com.uib.web.peptideshaker;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.Extension;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinService;
import com.vaadin.server.VaadinServlet;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.UI;
import javax.servlet.http.Cookie;

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
        if (VaadinSession.getCurrent().getAttribute("presessionid") != null) {
            UI.getCurrent().getSession().close();
            VaadinSession.getCurrent().getSession().invalidate();
            Page.getCurrent().reload();
        }
        VaadinSession.getCurrent().setAttribute("presessionid", "it is exist session");

        this.setSizeFull();
        WebPeptideShakerApp webPeptideShakerApp = new WebPeptideShakerApp();
        if ((Page.getCurrent().getBrowserWindowWidth() < Page.getCurrent().getBrowserWindowHeight()) || (Page.getCurrent().getBrowserWindowWidth() < 650) || (Page.getCurrent().getBrowserWindowHeight() < 600)) {
            webPeptideShakerApp.addStyleName("horizontalcss");
        } else {
            webPeptideShakerApp.removeStyleName("horizontalcss");
        }
        Page.getCurrent().addBrowserWindowResizeListener((Page.BrowserWindowResizeEvent event) -> {
            if ((Page.getCurrent().getBrowserWindowWidth() < Page.getCurrent().getBrowserWindowHeight()) || (Page.getCurrent().getBrowserWindowWidth() < 650) || (Page.getCurrent().getBrowserWindowHeight() < 600)) {
                webPeptideShakerApp.addStyleName("horizontalcss");
            } else {
                webPeptideShakerApp.removeStyleName("horizontalcss");
            }
        });

        setContent(webPeptideShakerApp);
        mimicNelsLogin();
    }

    private void mimicNelsLogin() {
        // Create a new cookie
        initCookie("SimpleSAMLAuthToken", "_3f8e6253727ff83b8ea362b136786756a676f83af1");
        initCookie("PHPSESSID", "d664d18c872013efad79dc820136a66e");
        initCookie("AuthMemCookie", "_0cded1a0bdc6fa712266a317ad22e667209a4773de");
    }

    private void initCookie(String name, String value) {
        // Create a new cookie
        Cookie myCookie = new Cookie(name, value);
// Make cookie expire in 2 minutes
        myCookie.setMaxAge(120);
// Set the cookie path.
        myCookie.setPath(VaadinService.getCurrentRequest().getContextPath());
// Save cookie
        VaadinService.getCurrentResponse().addCookie(myCookie);
    }

    @Override
    public void addExtension(Extension extension) {
        super.addExtension(extension); //To change body of generated methods, choose Tools | Templates.
    }

    @WebServlet(urlPatterns = "/*", name = "PeptidShakerUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = PeptidShakerUI.class, productionMode = false)
    public static class PeptidShakerUIServlet extends VaadinServlet {
    }
}
