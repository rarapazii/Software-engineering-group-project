package uk.ac.soton.comp2211.group2.view;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.util.Duration;
import java.io.FileNotFoundException;
import java.text.ParseException;
import static org.apache.logging.log4j.core.util.Loader.getClassLoader;

public class SideMenu extends VBox {
    private boolean visible = true;
    private final int width = 200;

    private VBox menuItems;
    private Label filter;
    private Label compare;
    private Label back;
    private Label settings;
    private Label help;
    private Label overview;
    private MainWindow mainWindow;
    private Label clickCost;


    /**
     * Creates the side menu component
     * @param window the window this will be displayed in
     */
    public SideMenu(MainWindow window) {
        // Set default overview
        mainWindow = window;
        setPrefWidth(width);
        setSpacing(20);
        setPadding(new Insets(10, 10, 10, 10));
        getStyleClass().add("side-menu");
        setAlignment(Pos.TOP_CENTER);

        // Add the AdDash Logo
        var logo = new Image(getClassLoader().getResource("icons/small-logo.png").toExternalForm());
        var logoView = new ImageView(logo);
        logoView.setFitWidth(60);
        logoView.setPreserveRatio(true);

        // Add a list of menuItems
        filter = menuItem("Filter");
        compare = menuItem("Compare");
        overview = menuItem("Overview Page");
        back = menuItem ("Go back to menu");
        settings = menuItem("Settings");
        help = menuItem("Help");
        clickCost = menuItem("ClickCost Distribution");

        menuItems = new VBox();
        menuItems.setSpacing(20);
        menuItems.setPadding(new Insets(10, 10, 10, 10));
        menuItems.getChildren().addAll(overview,clickCost,filter,compare,settings,help,back);
        menuItems.getStyleClass().add("side-menu");

        // Add all components to the side menu
        getChildren().addAll(logoView, menuItems);

        // Toggle the side menu on click
        logoView.setOnMouseClicked((e) -> toggleSideMenu());

        // loads filter scene when 'filter' option clicked
        filter.setOnMouseClicked(this::startFilter);
    
        //loads menu scene when 'back' option clicked
        back.setOnMouseClicked(this::startMenu);

        //loads overview scene when 'overview' option clicked
        overview.setOnMouseClicked(this::startOverview);

        //loads compare scene when 'compare' option clicked
        compare.setOnMouseClicked(this::startCompare);

        //loads settings scene when 'settings' option clicked
        settings.setOnMouseClicked(this::startSettings);

        clickCost.setOnMouseClicked(this::startClickScene);

        help.setOnMouseClicked(this::startHelp);

    }

    /**
     * Makes a HBox for each menu item
     */
    public Label menuItem(String title) {
        var titleText = new Label(title);
        titleText.getStyleClass().add("side-menu-item");
        return titleText;
    }

    public void startFilter(MouseEvent mouseEvent) {
        try {
            mainWindow.startFilter();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void startMenu(MouseEvent mouseEvent) {
        try{
            mainWindow.startMenu();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void startOverview(MouseEvent mouseEvent) {
        try{
            mainWindow.startOverview();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void startCompare(MouseEvent mouseEvent) {
        try{
            mainWindow.startCompare();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (ParseException e) {
             e.printStackTrace();
        }
     }

     public void startSettings(MouseEvent mouseEvent) {
         try{
             mainWindow.startSettings();
         } catch (FileNotFoundException e) {
             e.printStackTrace();
         } catch (ParseException e) {
             e.printStackTrace();
         }
     }

    public void startClickScene(MouseEvent mouseEvent) {
        try{
            mainWindow.startClicks();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void startHelp(MouseEvent mouseEvent){
        App.getInstance().showHelp();
    }


    /**
     * Toggle the side menu when you click the logo
     */
    private void toggleSideMenu() {
        if (visible) {
            visible = false;
            menuItems.setVisible(false);
            Duration duration = Duration.millis(500);
            Timeline timeline = new Timeline(
                    new KeyFrame(duration, new KeyValue(this.prefWidthProperty(), 50, Interpolator.EASE_BOTH)));
            timeline.play();
        } else {
            visible = true;
            Duration duration = Duration.millis(500);
            Timeline timeline = new Timeline(
                    new KeyFrame(duration, new KeyValue(this.prefWidthProperty(), width, Interpolator.EASE_BOTH)));
            timeline.play();
            timeline.setOnFinished((e) -> {
                menuItems.setVisible(true);
            });
        }
    }





}
