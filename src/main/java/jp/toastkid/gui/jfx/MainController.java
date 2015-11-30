package jp.toastkid.gui.jfx;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import org.apache.commons.lang3.StringUtils;


/**
 * JavaFX の コントローラ.
 * @author Toast kid
 *
 */
public final class MainController implements Initializable {
    /** default width. */
    private static final int DEFAULT_WIDTH = 450;
    /** default height. */
    private static final int DEFAULT_HEIGHT = 110;
    /** Y! web search url. */
    private static final String YSEARCH_URL
        = "http://search.yahoo.co.jp/search?p=%s&search.x=1&fr=top_ga1_sa&tid=top_ga1_sa&ei=UTF-8&aq=&oq=";
    /** status label. */
    @FXML
    public Label status;
    /** in script area. */
    @FXML
    public HBox browserArea;
    /** script input. */
    @FXML
    public TextField input;
    /** web view. */
    @FXML
    public WebView webView;
    /** Stylesheet selector. */
    @FXML
    public ComboBox<String> style;

    /** Stage. */
    private Stage stage;

    @Override
    public final void initialize(
            final URL url,
            final ResourceBundle resourcebundle
            ) {
        Platform.runLater( () -> {
            readStyleSheets();
            setStylesheet();
            hideBrowser();
            }
        );
    }

    @FXML
    protected void hideBrowser() {
        browserArea.visibleProperty().setValue(false);
        browserArea.setManaged(false);
        setDefaultSize();
    }

    /**
     * search and show result.
     */
    @FXML
    public void search() {
        final String text = input.getText();
        if (StringUtils.isBlank(text)) {
            return;
        }
        webView.getEngine().load(generateYSearchURL(text).get());
        if (!browserArea.managedProperty().getValue()) {
            browserArea.visibleProperty().setValue(true);
            browserArea.setManaged(true);
        }
        stage.setWidth(600);
        stage.setHeight(400);
    }

    /**
     * generate Y! Search's URL.
     * @param query query(raw)
     * @return Y! search url.
     */
    private Optional<String> generateYSearchURL(final String query) {
        try {
            return Optional.of(String.format(
                    YSEARCH_URL, URLEncoder.encode(query, Charset.forName("UTF-8").toString())));
        } catch (final UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    /**
     * read stylesheets.
     */
    private void readStyleSheets() {
        final File cssDir = new File(Style.USER_DEFINED_PATH);
        if (!cssDir.exists()) {
            return;
        }
        final ObservableList<String> items = style.getItems();
        final String[] styles = cssDir.list();
        for (final String style : styles) {
            if (!style.contains(".")) {
                continue;
            }
            final String upperCase = style.toUpperCase().substring(0, style.indexOf("."));
            if (items.contains(upperCase)) {
                continue;
            }
            items.add(upperCase);
        }
    }
    /**
     * set stylesheet name in combobox.
     */
    private void setStylesheet() {
        @SuppressWarnings("rawtypes")
        final SingleSelectionModel selectionModel = style.getSelectionModel();
        selectionModel.select(0);
    }

    /**
     * set stylesheet.
     */
    @FXML
    public void callSetOnStyle() {
        final String styleName = style.getItems().get(style.getSelectionModel().getSelectedIndex())
                                    .toString();
        if (StringUtils.isEmpty(styleName)) {
            return;
        }
        final ObservableList<String> stylesheets = stage.getScene().getStylesheets();
        if (stylesheets != null) {
            stylesheets.clear();
        }
        if ("MODENA".equals(styleName) || "CASPIAN".equals(styleName)) {
            Application.setUserAgentStylesheet(styleName);
        } else {
            stylesheets.add(Style.getPath(styleName));
        }
    }

    /**
     * close application.
     * @param event ActionEvent
     */
    @FXML
    public final void closeApplication(final ActionEvent event) {
        System.exit(0);
    }

    /**
     * set stage.
     * @param stage
     */
    public final void setThisStage(final Stage stage) {
        this.stage = stage;
    }

    /**
     * appear status label.
     * @param str status
     */
    public final void setStatus(final String str) {
        status.setText(str);
    }

    public void setDefaultSize() {
        stage.setWidth(DEFAULT_WIDTH);
        stage.setHeight(DEFAULT_HEIGHT);
    }
}
