package jp.toastkid.gui.jfx;

import java.io.File;
import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

/**
 * JavaFX 製 Search widget.
 * @author Toast kid
 * @version 0.0.1
 * @see <a href="http://www.torutk.com/projects/swe/wiki/JavaFXとアナログ時計">JavaFXとアナログ時計
 * </a>
 */
public final class Main extends Application {
    /** fxml ファイル. */
    private static final String FXML_PATH = "res/scenes/Main.fxml";

    /** コントローラ. */
    private MainController controller;

    private double dragStartX;
    private double dragStartY;

    @Override
    public void start(final Stage stage) {
        final long start = System.currentTimeMillis();
        stage.setTitle("Search Widget");
        final Scene scene = readScene(stage);
        stage.setScene(scene);
        // move to background.
        stage.focusedProperty().addListener((a, b, c) -> {stage.toBack();});

        stage.initStyle(StageStyle.TRANSPARENT);
        // implement drag event.
        scene.setFill(null);
        scene.setOnMousePressed((event) -> {
            dragStartX = event.getSceneX();
            dragStartY = event.getSceneY();
        });
        scene.setOnMouseDragged((event) -> {
            stage.setX(event.getScreenX() - dragStartX);
            stage.setY(event.getScreenY() - dragStartY);
        });
        stage.setOnCloseRequest(
            (event) -> {
            if (event.getEventType().equals(WindowEvent.WINDOW_CLOSE_REQUEST)) {
                controller.closeApplication(null);
            }
        });
        stage.show();
        // set small size.
        controller.setDefaultSize();
        controller.setStatus("完了 - " + (System.currentTimeMillis() - start) + "[ms]");
    }

    /**
     *
     * @param stage
     * @return
     */
    private final Scene readScene(final Stage stage) {
        try {
            final FXMLLoader loader = new FXMLLoader(new File(FXML_PATH).toURI().toURL());
            final VBox loaded = (VBox) loader.load();
            controller = (MainController) loader.getController();
            controller.setThisStage(stage);
            return new Scene(loaded);
        } catch (final IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        controller = null;
    }
    /**
     *
     * @param args
     */
    public static void main(final String[] args) {
        Application.launch(Main.class);
    }
}