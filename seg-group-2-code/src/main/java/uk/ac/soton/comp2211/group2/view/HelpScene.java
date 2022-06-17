package uk.ac.soton.comp2211.group2.view;

import javafx.collections.ObservableList;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import uk.ac.soton.comp2211.group2.view.Utilities.*;
import java.io.FileNotFoundException;
import java.text.ParseException;

public class HelpScene extends BaseScene{
    /**
     * Create a new scene
     *
     * @param mainWindow the window
     */
    @Override
    public ObservableList<String> getStylesheets() {
        return this.scene.getStylesheets();
    }
    private TopBar topBar = new TopBar("Help", mainWindow);
    SideMenu sideMenu = new SideMenu(mainWindow);

    public HelpScene(MainWindow mainWindow) {
        super(mainWindow);
    }

    @Override
    public void initialise() {

    }

    @Override
    public void build() throws FileNotFoundException, ParseException {
        root = new StackPane();
        root.setMaxWidth(mainWindow.getWidth());
        root.setMaxHeight(mainWindow.getHeight());
        root.getStyleClass().add("overview-background");
        var mainPane = new BorderPane();
        root.getChildren().add(mainPane);
        Text text = new Text("TODO: help stuff");
        VBox centerBox = new VBox();
        centerBox.getChildren().addAll(topBar, text);
        mainPane.setCenter(centerBox);
        mainPane.setLeft(sideMenu);

    }

}
