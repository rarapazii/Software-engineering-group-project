package uk.ac.soton.comp2211.group2.view;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import uk.ac.soton.comp2211.group2.controller.Pair;
import uk.ac.soton.comp2211.group2.model.IDLog;
import uk.ac.soton.comp2211.group2.view.Utilities.*;
import uk.ac.soton.comp2211.group2.view.charts.*;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

public class CompareFilterScene extends  BaseScene {

  private HashMap<String, Pair<Predicate<IDLog>,String>> info;
  private VBox description;
  private BorderPane mainPane;
  private ContextPick contextPick;
  private MetricPick metricPick;
  private GridPane base;
  private TopBar topBar = new TopBar("Add Charts to Compare", mainWindow);
  private String chartType;
  private ArrayList<Tab> tabList;
  private Tab tab;
  private TextField textBox;
  private Duration timeGranularity;
  
  private LocalDateTime start;
  private LocalDateTime end;


  public CompareFilterScene(MainWindow mainWindow, String chartType, Duration timeGranular, LocalDateTime start, LocalDateTime end){
    super(mainWindow);
    if (mainWindow.getFilterScene() != null) {
      tabList = mainWindow.getFilterScene().getTabs();
    } else {
      tabList = new ArrayList<>();
    }
    this.chartType = chartType;
    System.out.println(chartType);
    this.info = new HashMap<>();
    if(timeGranular != null) {
      timeGranularity = timeGranular;
    }else{
      timeGranularity = Duration.ofDays(1);
    }
    this.start = start;
    this.end = end;

  }

  public CompareFilterScene(MainWindow mainWindow, HashMap<String,Pair<Predicate<IDLog>,String>> info, VBox description, String chartType, Duration timeGranular, LocalDateTime start, LocalDateTime end){
    super(mainWindow);
    if (mainWindow.getFilterScene() != null) {
      tabList = mainWindow.getFilterScene().getTabs();
    } else {
      tabList = new ArrayList<>();
    }
    this.description = description;
    this.info = info;
    this.chartType = chartType;
    System.out.println(chartType);
    if(timeGranular != null) {
      timeGranularity = timeGranular;
    }else{
      timeGranularity = Duration.ofDays(1);
    }
    this.start = start;
    this.end = end;
  }

  @Override
  public void initialise() {

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
    base = new GridPane();
    base.setHgap(20);
    base.setVgap(20);

    metricPick = new MetricPick();
    contextPick = new ContextPick();

    if(description == null){
      var descLabel = new Label("Chart Description");
      descLabel.getStyleClass().add("titleprop");
      description = new VBox(descLabel);
      description.getStyleClass().add("filter-rectangle");
    }

    var getTitle = new HBox();
    var titleLabel = new Label("Add Description for Data:");
    titleLabel.getStyleClass().add("titleprop");
    textBox = new TextField();
    getTitle.getChildren().addAll(titleLabel,textBox);
    getTitle.setSpacing(20);
    getTitle.getStyleClass().add("filter-rectangle");

    base.add(metricPick, 1, 1, 1, 2);
    base.add(contextPick, 0, 2, 1, 1);
    base.add(description,2,1,1,2);
    base.add(getTitle,0,0,4,1);
    base.getStyleClass().add("overview-background");
    base.setAlignment(Pos.CENTER);

    Button update = new Button("Update");
    update.setMinSize(75, 50);
    update.setOnAction(this::updateAction);
    update.getStyleClass().add("button");

    Button finish = new Button("Finish");
    finish.setMinSize(75, 50);
    finish.getStyleClass().add("button");
    finish.setOnAction(this::createGraph);

    var buttonBox =  new HBox();
    buttonBox.getChildren().addAll(update,finish);
    buttonBox.setAlignment(Pos.CENTER);
    buttonBox.setSpacing(20);

    box.getChildren().addAll(topBar, base, buttonBox);
    box.setAlignment(Pos.CENTER);
    box.setSpacing(20);
    VBox.setVgrow(base, Priority.ALWAYS);
    mainPane.setCenter(box);
    root.getChildren().add(mainPane);

  }

  @Override
  public ObservableList<String> getStylesheets() {
    return this.scene.getStylesheets();
  }


  private void createGraph(ActionEvent actionEvent) {
    if(metricPick.getSelectedMetric() != null) {
      info.put(textBox.getText(), new Pair<>(contextPick.getPredicate(), metricPick.getSelectedMetric()));
    }
      if (textBox.getText() == null) {
        Alert failureBox = new Alert(Alert.AlertType.ERROR);
        failureBox.setTitle("Error!");
        failureBox.setContentText("Name for new data series is required");
        failureBox.showAndWait();
      } else {
        switch (chartType) {
          case "Linear Chart":
            var lChart = new LChart("Frequency", "Time", mainWindow);
            for (Map.Entry<String, Pair<Predicate<IDLog>, String>> chart : info.entrySet()) {
              lChart.filterAndAdd(timeGranularity, start, end, chart.getValue().getSecond(), integerOrFloat(chart.getValue().getSecond()), chart.getKey(), chart.getValue().getFirst());
            }
            tab = new Tab("Comparing charts", lChart.getChart());
            break;
          case "Bar Chart":
            var bChart = new BChart("Frequency", "Time", mainWindow);
            System.out.println(timeGranularity);
            for (Map.Entry<String, Pair<Predicate<IDLog>, String>> chart : info.entrySet()) {
              bChart.filterAndAdd(timeGranularity, start, end, chart.getValue().getSecond(), integerOrFloat(chart.getValue().getSecond()), chart.getKey(), chart.getValue().getFirst());
            }
            tab = new Tab("Comparing charts", bChart.getChart());
            break;
        }


        tabList.add(tab);
        try {
          mainWindow.updateSummaryScene(tabList);
          mainWindow.startOverview();
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
  }

  private String integerOrFloat(String metric){
    String thing = "";
    switch (metric){
      case "Clicks":
      case "Impressions":
      case "Uniques":
      case "Bounces":
      case "Conversions":
        thing = "Integer";
        break;
      default:
        thing = "Float";
        break;

    }
    return(thing);
  }





    private void updateAction(ActionEvent actionEvent) {
      if(textBox.getText() == null){
      Alert failureBox = new Alert(Alert.AlertType.ERROR);
      failureBox.setTitle("Error!");
      failureBox.setContentText("Name for new data series is required");
      failureBox.showAndWait();
      }else {
        info.put(textBox.getText(), new Pair<>(contextPick.getPredicate(), metricPick.getSelectedMetric()));
        var newDesc = new Text(textBox.getText());
        description.getChildren().add(newDesc);
      try {
        mainWindow.startNewCompareFilter(info, description, chartType, timeGranularity, start, end);
      } catch (FileNotFoundException e) {
        e.printStackTrace();
      } catch (ParseException e) {
        e.printStackTrace();
      }
    }


  }


}
