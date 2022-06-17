package uk.ac.soton.comp2211.group2.view.Utilities;

import javafx.geometry.Insets;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.text.Text;
import uk.ac.soton.comp2211.group2.view.App;
import uk.ac.soton.comp2211.group2.view.MainWindow;
import uk.ac.soton.comp2211.group2.view.SummaryScene;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;

public class TopBar extends HBox {
    protected Text title;
    protected ImageView save;
    protected ImageView settings;
    protected ImageView help;
    protected HBox icons;
    private  MainWindow mainWindow;

    public TopBar(String text, MainWindow window) {
        mainWindow = window;
        save = new ImageView(new Image(SummaryScene.class.getResource("/icons/saveIcon.png").toExternalForm()));
        // setting the fit height and width of the image view
        save.setFitHeight(30);
        save.setFitWidth(30);
        // Setting the preserve ratio of the image view
        save.setPreserveRatio(true);

        help = new ImageView(new Image(SummaryScene.class.getResource("/icons/help.png").toExternalForm()));
        // setting the fit height and width of the image view
        help.setFitHeight(30);
        help.setFitWidth(30);
        // Setting the preserve ratio of the image view
        help.setPreserveRatio(true);

        settings = new ImageView(new Image(SummaryScene.class.getResource("/icons/settings.png").toExternalForm()));
        // setting the fit height and width of the image view
        settings.setFitHeight(30);
        settings.setFitWidth(30);
        // Setting the preserve ratio of the image view
        settings.setPreserveRatio(true);

        title = new Text(text);
        title.getStyleClass().add("overview-title");

        icons = new HBox(save, settings, help);
        icons.setSpacing(20D);

        Region spacer = new Region();
        spacer.setMinWidth(100D);
        getChildren().addAll(title, spacer, icons);
        setHgrow(spacer, Priority.ALWAYS);
        setPadding(new Insets(10, 10 , 10, 10));

        //loads settings scene when 'settings' option clicked
        save.setOnMouseClicked(this::startSave);
        settings.setOnMouseClicked(this::startSettings);
        help.setOnMouseClicked(this::startHelp);

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

    public void startSave(MouseEvent mouseEvent) {
        try {
            mainWindow.startSave();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void startHelp(MouseEvent mouseEvent){
        App.getInstance().showHelp();
    }
}
