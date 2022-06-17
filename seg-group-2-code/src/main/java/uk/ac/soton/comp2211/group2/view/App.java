package uk.ac.soton.comp2211.group2.view;

import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Screen;
import javafx.stage.Stage;
import uk.ac.soton.comp2211.group2.controller.interfaces.ControlModel;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.text.ParseException;

/**
 * JavaFX App
 */
public class App extends Application {

    private static final boolean ENABLE_HELP = true;

    private static App instance;
    @SuppressWarnings("unused")
    private Stage stage;

    private static ControlModel dataModel;

    @Override
    public void start(Stage stage) throws FileNotFoundException, ParseException, MalformedURLException {
        instance = this;
        this.stage = stage;
        Rectangle2D screenBounds = Screen.getPrimary().getBounds();
        @SuppressWarnings("unused")
		var mainWindow = new MainWindow(stage, (int)(screenBounds.getWidth()*8/9), (int)(screenBounds.getHeight())*8/9);
        stage.show();

    }

    public static void main(ControlModel cm, String[] args) {
        dataModel = cm;
        launch();
    }

    /**
     * Shutdown the dashboard
     */
    public void shutdown() {
        System.exit(0);
    }

    /**
     * Get the singleton App instance
     * 
     * @return the app
     */
    public static App getInstance() {
        return instance;
    }

    /**
     * Get (a reference to) the App's Model, wrapped in Controller interfaces.
     * 
     * @return the model.
     */
    public static ControlModel getModel() {
        return dataModel;
    }

    public static boolean showHelp() {
        if (!ENABLE_HELP) {
            return false; // Do nothing
        }
        // Try and open help with the default PDF viewer.
        if (Desktop.isDesktopSupported()) {
            Alert a = new Alert(AlertType.INFORMATION, "Help page loading");
            a.show();
            try {
                // Copy the PDF to a temp file.
                File tmpFile = File.createTempFile("addashhelp", ".pdf");
                Files.copy(App.class.getResourceAsStream("/help/COMP2211_User_Guide.pdf"), tmpFile.toPath(),
                        StandardCopyOption.REPLACE_EXISTING);
                // Display from the temp file
                Desktop.getDesktop().open(tmpFile);
                a.close();
                return true;
            } catch (IOException e1) {
                // f. ignore for now.
                e1.printStackTrace();
                return false;
            }
        }
        return false;
    }

}
