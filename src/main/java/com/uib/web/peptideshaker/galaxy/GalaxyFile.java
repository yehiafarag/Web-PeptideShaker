package com.uib.web.peptideshaker.galaxy;

import com.vaadin.server.VaadinService;
import com.vaadin.server.VaadinSession;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class represents usable files on galaxy the class allow downloading the
 * files once there is a need to use it
 *
 * @author Yehia Farag
 */
public class GalaxyFile {

    private final DataSet dataset;
    private final File userFolder;

    public GalaxyFile(File userFolder, DataSet dataset) {
        this.dataset = dataset;
        this.userFolder = userFolder;

    }

    public DataSet getDataset() {
        return dataset;
    }

    public File getFile() {

        File file = new File(userFolder, dataset.getGalaxyId());
        if (file.exists()) {
             return file;
        }

        FileOutputStream fos = null;
        try {
            URL downloadableFile = new URL(dataset.getDownloadUrl() );
            System.err.println("at file path " + dataset.getDownloadUrl());
            URLConnection conn = downloadableFile.openConnection();
            conn.addRequestProperty("Cookie", VaadinSession.getCurrent().getSession().getAttribute("cookies") + "");
            conn.addRequestProperty("Accept", "*/*");
            conn.addRequestProperty("Accept-Encoding", "gzip, deflate, sdch, br");
            conn.addRequestProperty("Accept-Language", "ar,en-US;q=0.8,en;q=0.6,en-GB;q=0.4");
            conn.addRequestProperty("Cache-Control", "no-cache");
            conn.addRequestProperty("Connection", "keep-alive");
            conn.addRequestProperty("DNT", "1");
            conn.addRequestProperty("Pragma", "no-cache");
            conn.setDoInput(true);
            InputStream in = conn.getInputStream();
            try (ReadableByteChannel rbc = Channels.newChannel(in)) {
                fos = new FileOutputStream(file);
                fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
                fos.close();
                rbc.close();
                in.close();

            } catch (MalformedURLException ex) {
                ex.printStackTrace();
            } catch (IOException ex) {
                ex.printStackTrace();
            } finally {
                if (fos != null) {
                    try {
                        fos.close();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }

            }
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

//        String basepath = VaadinService.getCurrent().getBaseDirectory().getAbsolutePath();
//        file = new File(basepath + "/VAADIN/default_searching.par");
        return file;
    }
}
