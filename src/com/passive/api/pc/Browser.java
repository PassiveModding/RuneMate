package com.passive.api.pc;

import java.awt.*;
import java.io.IOException;
import java.net.URI;

/**
 * Created by SlashnHax on 1/01/2016.
 */
public class Browser {
    public static void openWebpage(URI uri) {
        try {
            Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
            if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE))
                desktop.browse(uri);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void openWebpage(String url) {
        try {
            openWebpage(new URI(url));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
