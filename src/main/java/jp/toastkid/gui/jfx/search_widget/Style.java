package jp.toastkid.gui.jfx.search_widget;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.lang3.StringUtils;

import javafx.application.Application;

/**
 * JavaFX stylesheet definition.
 * @author Toast kid
 * @see Application
 */
public class Style {
    /** extension of stylesheet. */
    private static final String CSS = ".css";
    /** path to user defined stylesheets. */
    public static final String USER_DEFINED_PATH = "user/css/gui";

    /**
     * get path to css.
     * @param s Style name.
     * @return path to css.
     */
    public static String getPath(final String s) {
        if (StringUtils.isEmpty(s)) {
            return "MODENA";
        }
        final URL resource = Style.class.getResource("/css/" + s.toLowerCase() + CSS);
        if (resource == null) {
            final File userDefined = new File(USER_DEFINED_PATH, s.toLowerCase() + CSS);
            if (userDefined.exists()) {
                try {
                    return userDefined.toURI().toURL().toString();
                } catch (final MalformedURLException e) {
                    e.printStackTrace();
                }
            }
        }
        return (resource != null) ? resource.toString() : s.toString();
    }
}
