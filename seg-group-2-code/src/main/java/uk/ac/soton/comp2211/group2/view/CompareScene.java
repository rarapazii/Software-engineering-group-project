package uk.ac.soton.comp2211.group2.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import uk.ac.soton.comp2211.group2.view.Utilities.*;

import java.io.FileNotFoundException;
import java.text.ParseException;

public class CompareScene extends BaseScene{

  private BorderPane mainPane;
  private TimeBox timeBox;
  private ChartPick chartPick;
  private TopBar topBar = new TopBar("Compare Graph", mainWindow);

  public CompareScene(MainWindow mainWindow){
    super(mainWindow);
    FXCollections.observableArrayList();
  }

  @Override
  public void initialise() {



  }
  @Override
  public ObservableList<String> getStylesheets() {
    return this.scene.getStylesheets();
  }
  @Override
  public void build() throws FileNotFoundException, ParseException {
    root = new StackPane();
    root.setMaxWidth(mainWindow.getWidth());
    root.setMaxHeight(mainWindow.getHeight());
    mainPane = new BorderPane();
    mainPane.getStyleClass().add("overview-background");

    var sideMenu = new SideMenu(mainWindow);
    mainPane.setLeft(sideMenu);

    var box = new VBox();

    timeBox = new TimeBox();
    chartPick = new ChartPick();

    Button update = new Button("Update");
    update.setMinSize(200, 100);
    update.setOnAction(this::updateAction);
    update.getStyleClass().add("button");

    box.getChildren().addAll(topBar, timeBox, chartPick,update);
    chartPick.setMaxWidth(timeBox.getMaxWidth());
    box.getStyleClass().add("overview-background");
    box.setAlignment(Pos.CENTER);
    box.setSpacing(20);
    mainPane.setCenter(box);
    root.getChildren().add(mainPane);

  }

  private void updateAction(ActionEvent actionEvent) {
    try {
      mainWindow.startCompareFilter(chartPick.getChartType(),timeBox.getDuration(), timeBox.getStartDate(), timeBox.getEndDate());
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (ParseException e) {
      e.printStackTrace();
    }
  }



}
