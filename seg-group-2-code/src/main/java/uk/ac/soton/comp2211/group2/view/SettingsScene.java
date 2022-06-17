package uk.ac.soton.comp2211.group2.view;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import uk.ac.soton.comp2211.group2.controller.interfaces.ControlAuctionData;
import uk.ac.soton.comp2211.group2.view.Utilities.*;

import java.io.FileNotFoundException;
import java.text.ParseException;
import java.util.ArrayList;

import static java.lang.Integer.valueOf;

public class SettingsScene extends BaseScene{
    /**
     * Create a new scene
     *
     * @param mainWindow the window
     */

    private TopBar topBar = new TopBar("Settings", mainWindow);
    private ToggleGroup bounceOptions = new ToggleGroup();
    private ToggleGroup colourOptions = new ToggleGroup();
    TextField box1 = new TextField();
    TextField box2 = new TextField();
    RadioButton butt1 = new RadioButton("Max pages visited");
    RadioButton butt2 = new RadioButton("Time elapsed (seconds)");
    SideMenu sideMenu = new SideMenu(mainWindow);
    RadioButton col1 = new RadioButton("Blue");
    RadioButton col2 = new RadioButton("Purple");

    public SettingsScene(MainWindow mainWindow) {
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
        Text bounceRate = new Text("Define bounce rate");
        bounceRate.getStyleClass().add("titleprop");
        VBox centerBox = new VBox();
        HBox op1 = new HBox();
        HBox op2 = new HBox();

        butt1.setToggleGroup(bounceOptions);
        butt2.setToggleGroup(bounceOptions);
        op1.getChildren().addAll(butt1, box1);
        op2.getChildren().addAll(butt2, box2);

        Button save = new Button("Save Preferences");;
        save.setOnAction(this::saveAction);
        save.getStyleClass().add("button");
        Text colScheme = new Text("Select colour scheme");  
        colScheme.getStyleClass().add("titleprop");
        col1.setToggleGroup(colourOptions);
        col2.setToggleGroup(colourOptions);
        VBox box = new VBox(bounceRate, op1, op2, colScheme, col1, col2);
        box.setPadding(new Insets(10, 10, 10, 10));
        centerBox.getChildren().addAll(topBar, box, save);
        mainPane.setCenter(centerBox);
        mainPane.setLeft(sideMenu);

    }

    @Override
    public ObservableList<String> getStylesheets() {
        return this.scene.getStylesheets();
    }

    public String getSelectedBounce() {
        String str = "";
        RadioButton val = (RadioButton) bounceOptions.getSelectedToggle();
        if (val != null) str = val.getText();
        return str;
    }
    public String getSelectedColour() {
        String str = "";
        RadioButton val = (RadioButton) colourOptions.getSelectedToggle();
        if (val != null) str = val.getText();
        return str;
    }

    private void saveAction(ActionEvent event) {
        ControlAuctionData auctionData = DataOnBoard.getSingletonInstance().getDataSource();
        Integer val = 0;
        switch (getSelectedBounce()) {
            case "Max pages visited":
                //set bounce rate to this value
                //TODO: add input validation
                try {
                    val = valueOf(box1.getText());
                    if (val >= 0) auctionData.setPagesViewedBounceMinimum(val);
                }catch (NumberFormatException e){
                    //TODO: add error message to gui
                    System.out.println("Invalid value for "+ getSelectedBounce());
                }
                break;
            case "Time elapsed (seconds)":
                try {
                    val = valueOf(box2.getText());
                    if (val >= 0) auctionData.setBounceTimeLimit(val);
                }catch (NumberFormatException e){
                    //TODO: add error message to gui
                    System.out.println("Invalid value for "+ getSelectedBounce());
                }
                break;
        }
        switch (getSelectedColour()){
            case "Blue":
                mainWindow.setCSS("style/main.css");
                break;
            case "Purple":
                mainWindow.setCSS("style/scheme1.css");
                break;
        }
        try {
            mainWindow.startSettings();
            if (mainWindow.getFilterScene() != null) {
                mainWindow.updateSummaryScene(mainWindow.getFilterScene().getTabs());
            } else{
                mainWindow.updateSummaryScene(new ArrayList<>());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
