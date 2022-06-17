package uk.ac.soton.comp2211.group2.view;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import uk.ac.soton.comp2211.group2.controller.Pair;
import uk.ac.soton.comp2211.group2.model.IDLog;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static org.apache.logging.log4j.core.util.Loader.getClassLoader;

public class MainWindow {

    private final int width;
    private final int height;

    private final Stage stage;

    private BaseScene currentScene;
    private Scene scene;
    protected SummaryScene summaryScene;
    private FilterScene filterScene;
    private CompareScene compareScene;
    private MenuScene menuScene;
    protected String style = "style/main.css";


    /**
     * Creates application window
     * 
     * @param stage  stage
     * @param width  width
     * @param height height
     */
    public MainWindow(Stage stage, int width, int height) throws FileNotFoundException, ParseException {
        this.width = width;
        this.height = height;
        this.stage = stage;

        // Setup window
        setupStage();

        // Setup default scene
        setupDefaultScene();

        // Go to menu
        startMenu();
    }

    /**
     * Setup the default settings for the stage itself (the window), such as the
     * title and minimum width and height.
     */
    public void setupStage() {
        stage.setTitle("Ad Dashboard");
        stage.setMinWidth(width);
        stage.setMinHeight(height);
    }

    /**
     * Load a given scene which extends BaseScene and switch over.
     * 
     * @param newScene new scene to load
     */
    public void loadScene(BaseScene newScene) throws FileNotFoundException, ParseException {
        // Create the new scene and set it up
        if (currentScene != newScene) {
            newScene.build();
            currentScene = newScene;
            scene = newScene.setScene();
            stage.setScene(scene);

        // Initialise the scene when ready
            Platform.runLater(() -> currentScene.initialise());
        }
    }

    /**
     * Setup the default scene (an empty black scene) when no scene is loaded
     */
    public void setupDefaultScene() {
        this.scene = new Scene(new Pane(), width, height, Color.BLACK);
        stage.setScene(this.scene);
    }

    /**
     * Display the menu scene
     */
    public void startMenu() throws FileNotFoundException, ParseException {
        loadScene(new MenuScene(this));
    }

    public void startClicks() throws FileNotFoundException, ParseException {
        new ClickScene(this);
    }

    // side menu buttons functions
    public void startOverview() throws FileNotFoundException, ParseException {
        if (summaryScene == null) {
            summaryScene = new SummaryScene(this);
        }
        else{
            Platform.runLater(()->updateCSS(summaryScene));
        }
        //Platform.runLater(()->updateCSS(summaryScene));
        loadScene(summaryScene);
    }

    public void startFilter() throws FileNotFoundException, ParseException {
        if (filterScene == null) {
            filterScene = new FilterScene(this);
        }
        Platform.runLater(()->updateCSS(filterScene));
        loadScene(filterScene);
    }

    public void startCompareFilter(String chartType, Duration granularity, LocalDateTime start, LocalDateTime end) throws FileNotFoundException, ParseException {
        loadScene(new CompareFilterScene(this,chartType,granularity, start, end));
    }

    public void setCSS(String css){
        style = css;
    }

    public void updateCSS(BaseScene scene1){
        scene1.getStylesheets().clear();
        Platform.runLater(()->scene1.getStylesheets().add(getClassLoader().getResource(style).toExternalForm()));

    }

    public String getCSS(){
        return style;
    }

    // public void startMetrics() throws FileNotFoundException, ParseException {
    // loadScene(new MetricScene(this));}
    // public void startCharts() throws FileNotFoundException, ParseException {
    // loadScene(new ChartScene(this));}
    public void startCompare() throws FileNotFoundException, ParseException {
        loadScene(new CompareScene(this));
    }
     public void startSettings() throws FileNotFoundException, ParseException {
     loadScene(new SettingsScene(this));}

     public void startHelp() throws FileNotFoundException, ParseException {
     loadScene(new HelpScene(this));}
    // public void startMore() throws FileNotFoundException, ParseException {
    // loadScene(new MoreScene(this));}

    public void startSave() throws IOException{
        FileChooser fileChooser = new FileChooser();

        // add a file chooser for the log files
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.png");
        fileChooser.getExtensionFilters().add(extFilter);
        File file = fileChooser.showSaveDialog(this.getStage());
        if (file != null) {
            summaryScene.saveGraphImage(file);
        }
    }

    /**
     * Get the current scene being displayed
     * 
     * @return scene
     */
    public Scene getScene() {
        return scene;
    }

    public SummaryScene getSummaryScene() { return summaryScene; }

    public SummaryScene updateSummaryScene(ArrayList<Tab> tabs) throws FileNotFoundException, ParseException {
        summaryScene = new SummaryScene(this, tabs);
        return summaryScene;
    }

    public FilterScene getFilterScene() { return filterScene; }

    /**
     * Get the width of the Window
     * 
     * @return width
     */
    public int getWidth() {
        return this.width;
    }

    /**
     * Get the height of the Window
     * 
     * @return height
     */
    public int getHeight() {
        return this.height;
    }

    /**
     * Get the stage
     * 
     * @return stage
     */
    public Stage getStage() {
        return this.stage;
    }

    public void startNewCompareFilter(HashMap<String,Pair<Predicate<IDLog>, String>> info, VBox description, String chartType, Duration granularity, LocalDateTime start, LocalDateTime end) throws FileNotFoundException, ParseException {
        loadScene(new CompareFilterScene(this,info,description, chartType, granularity, start, end));
    }


}