package uk.ac.soton.comp2211.group2.view;

import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.stage.FileChooser;
import uk.ac.soton.comp2211.group2.view.Utilities.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import static org.apache.logging.log4j.core.util.Loader.getClassLoader;

public class MenuScene extends BaseScene {
    private File file;
    @Override
    public ObservableList<String> getStylesheets() {
        return this.scene.getStylesheets();
    }
    /**
     * Create a new menu scene
     * 
     * @param mainWindow the window this will be displayed in
     */
    public MenuScene(MainWindow mainWindow) {
        super(mainWindow);
        //Create the singleton DataOnBoard object
        DataOnBoard.setSingletonInstance(new DataOnBoard());
    }

    /**
     * Builds the menu scene
     */
    @Override
    public void build() {
        root = new StackPane();

        var menuPane = new StackPane();
        root.setMaxWidth(mainWindow.getWidth());
        root.setMaxHeight(mainWindow.getHeight());
        root.getChildren().add(menuPane);
        menuPane.getStyleClass().add("menu-background");

        VBox menu = new VBox();
        menuPane.getChildren().add(menu);
        menu.setAlignment(Pos.CENTER);
        menu.setSpacing(8.5);

        try {
            // adding a logo to the menu page
            var logo = new Image(getClassLoader().getResource("icons/logo.png").toExternalForm());
            var logoView = new ImageView(logo);
            logoView.setFitHeight(300);
            logoView.setFitWidth(300);

            FileChooser fil_chooser = new FileChooser();

            // add a file chooser for the log files
            Label impression = new Label("Choose Impression Log");
            impression.setOnMouseClicked(mouseEvent -> {
                file = fil_chooser.showOpenDialog(mainWindow.getStage());
                if (file != null) {
                	DataOnBoard.getSingletonInstance().setImpressions(file);
                    impression.setText("IMPRESSION LOG: "+file.getName());
                }
            });
            impression.getStyleClass().add("menuItem");

            Label click = new Label("Choose Click Log");
            click.setOnMouseClicked(mouseEvent -> {
                file = fil_chooser.showOpenDialog(mainWindow.getStage());
                if (file != null) {
                    DataOnBoard.getSingletonInstance().setClicks(file);
                    click.setText("CLICK LOG: " + file.getName());
                }
            });

            click.getStyleClass().add("menuItem");

            Label server = new Label("Choose Server Log");
            server.setOnMouseClicked(mouseEvent -> {
                file = fil_chooser.showOpenDialog(mainWindow.getStage());
                if (file != null) {
                	DataOnBoard.getSingletonInstance().setServerLogs(file);
                    server.setText("SERVER LOG: " + file.getName());
                }
            });
            server.getStyleClass().add("menuItem");

            // buttons to open a window for summary and click costs
            var summary = new Label("Summary Overview");
            summary.setOnMouseClicked(mouseEvent -> {
            	dataLoad(() -> {
	                try {
	                    mainWindow.startOverview();
	                } catch (FileNotFoundException e) {
	                    e.printStackTrace();
	                } catch (ParseException e) {
	                    e.printStackTrace();
	                }
            	});
            });
            summary.getStyleClass().add("menuItem2");


            // hbox to allow summary and click cost to be aligned
            var hBox = new HBox();
            hBox.setAlignment(Pos.CENTER);
            hBox.setSpacing(40);
            hBox.getChildren().addAll(summary);
            
            //Aligns help button
            var helpButton = new Label("Help");
            
            helpButton.getStyleClass().add("menuItem");
            helpButton.setAlignment(Pos.TOP_RIGHT);
            
            helpButton.setOnMouseClicked(me -> {
            	App.showHelp();
            });
            
            var spacer = new Region();
            
            spacer.setMinHeight(400);
            
            // adds all components to the menu
            menu.getChildren().addAll(logoView, impression, click, server, hBox, spacer, helpButton);
            

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Initialise the menu
     */
    @Override
    public void initialise() {
    }

    /**
     * Loads the data from the three files given off thread. Runs 'onComplete' once done.
     * @param onComplete The Runnable to run on the GUI thread once we've successfully loaded the data.
     */
    public void dataLoad(Runnable onComplete) {
    	
    	if (!DataOnBoard.getSingletonInstance().hasAllFiles()) {
    		Alert failureBox = new Alert(AlertType.ERROR);
			failureBox.setTitle("Error!");
			failureBox.setHeaderText("Not all files selected!");
			failureBox.setContentText("Not all files have been selected. Please select all three files and try again!");
			failureBox.showAndWait();
			return;
    	}
    	
    	Alert loadingBox = new Alert(AlertType.INFORMATION);
    	loadingBox.setTitle("Please wait...");
    	loadingBox.setHeaderText("Please wait.");
    	loadingBox.setContentText("Your files are being loaded. This may take some time, please wait.");
    	loadingBox.show();
    	
    	Runnable process = () -> {
    		//Off thread now, can't edit the GUI
    		try {
				DataOnBoard.getSingletonInstance().computeData();
				//If we get here, we're done successfully, close the loading box and run the code we were given.
				Platform.runLater(() -> {
					loadingBox.close();
					onComplete.run();
				});
			} catch (IOException e) {
				e.printStackTrace();
				//Reset the file loader
				DataOnBoard.getSingletonInstance().resetCalc();
				//Something went wrong, show an error
				Platform.runLater(() -> {
					loadingBox.close();
					Alert failureBox = new Alert(AlertType.ERROR);
					failureBox.setTitle("Error!");
					failureBox.setHeaderText("Something went wrong!");
					failureBox.setContentText("Something went wrong loading the files. Double check you selected the right file for everything and that they're in the correct format. See the logs for more information.");
					failureBox.showAndWait();
				});
				
			}
    	};
    	
    	new Thread(process).start();
    }


}
