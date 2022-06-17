package uk.ac.soton.comp2211.group2.view;

import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.layout.*;

import java.io.FileNotFoundException;
import java.text.ParseException;

import static org.apache.logging.log4j.core.util.Loader.getClassLoader;

public abstract class BaseScene {

    protected final MainWindow mainWindow;

    protected StackPane root;
    protected Scene scene;

    /**
     * Create a new scene
     * 
     * @param mainWindow the window
     */
    public BaseScene(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
    }

    /**
     * Initialise this scene. Called after creation
     */
    public abstract void initialise();

    /**
     * Build the layout of the scene
     */
    public abstract void build() throws FileNotFoundException, ParseException;

    /**
     * Create a new JavaFX scene using the root contained within this scene
     * 
     * @return JavaFX scene
     */
    public Scene setScene() {
        var previous = mainWindow.getScene();
        Scene scene = new Scene(root, previous.getWidth(), previous.getHeight(), Color.BEIGE);
        this.scene = scene;
        scene.getStylesheets().clear();
        scene.getStylesheets().add(getClassLoader().getResource(mainWindow.style).toExternalForm());
        return scene;
    }

    /**
     * Get the JavaFX scene contained inside
     * 
     * @return JavaFX scene
     */
    public Scene getScene() {
        return this.scene;
    }

    public abstract ObservableList<String> getStylesheets();
}
