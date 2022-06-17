package uk.ac.soton.comp2211.group2.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import uk.ac.soton.comp2211.group2.view.Utilities.*;
import uk.ac.soton.comp2211.group2.view.charts.BChart;
import uk.ac.soton.comp2211.group2.view.charts.LChart;
import javafx.scene.control.Tab;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.util.ArrayList;

public class FilterScene extends BaseScene {

    private GridPane base;
    private TimeBox timeBox;
    private BorderPane mainPane;
    //private DataOnBoard dataOnBoard;
    private LChart lChart;
    private BChart bChart;
    private Tab tab;
    private ChartPick chartPick;
    private MetricPick metricPick;
    private ContextPick contextPick;
    public ArrayList<Tab> tabList = new ArrayList<>();
    private TopBar topBar = new TopBar("Create a Graph", mainWindow);
    @Override
    public ObservableList<String> getStylesheets() {
        return this.scene.getStylesheets();
    }
    /** 
     * Constructor for FilterScene
     * 
     * @param mainWindow
     */
    public FilterScene(MainWindow mainWindow) throws FileNotFoundException, ParseException {
        super(mainWindow);
        FXCollections.observableArrayList();
    }

    @Override
    public void initialise() {
    }

    
    /** 
     * This method creates the filter scene.
     * 
     * @return the filter scene
     * @throws FileNotFoundException
     * @throws ParseException
     */
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

        timeBox = new TimeBox();
        chartPick = new ChartPick();
        metricPick = new MetricPick();
        contextPick = new ContextPick();

        base.add(timeBox, 0, 0, 2, 1);
        base.add(metricPick, 1, 1, 1, 2);
        base.add(chartPick, 0, 1, 1, 1);
        base.add(contextPick, 0, 2, 1, 1);
        base.getStyleClass().add("overview-background");
        base.setAlignment(Pos.CENTER);

        Button update = new Button("Update");
        update.setMinSize(200, 100);
        update.setOnAction(this::updateAction);
        update.getStyleClass().add("button");
        box.getChildren().addAll(topBar, base, update);
        box.setAlignment(Pos.CENTER);
        box.setSpacing(20);
        VBox.setVgrow(base, Priority.ALWAYS);
        mainPane.setCenter(box);
        root.getChildren().add(mainPane);
    }

    
    /** 
     * Gets the line chart
     * @return LChart
     */
    public LChart getLChart() {
        return lChart;
    }

    
    /** 
     * gets the bar chart
     * @return BChart
     */
    public BChart getBChart() {
        return bChart;
    }

    
    /** 
     * function called to generate a new chart when update button is pressed
     * @param event
     */
    private void updateAction(ActionEvent event) {
        switch (chartPick.getChartType()) {
            case "Linear Chart":
                lChart = new LChart("Time", metricPick.getSelectedMetric(), mainWindow);
                lChart.filterAndAdd(timeBox.getDuration(), timeBox.getStartDate(), timeBox.getEndDate(),metricPick.getSelectedMetric(), integerOrFloat(metricPick.getSelectedMetric()), metricPick.getSelectedMetric() + " Per Day", contextPick.getPredicate());
                tab = new Tab(metricPick.getSelectedMetric(), lChart.getChart());
                break;
            case "Bar Chart":
                bChart = new BChart("Time", metricPick.getSelectedMetric(), mainWindow);
                System.out.println(timeBox.getDuration());
                bChart.filterAndAdd(timeBox.getDuration(), timeBox.getStartDate(), timeBox.getEndDate(),metricPick.getSelectedMetric(), integerOrFloat(metricPick.getSelectedMetric()), metricPick.getSelectedMetric() + " Per Day", contextPick.getPredicate());
                tab = new Tab(metricPick.getSelectedMetric(), bChart.getChart());
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
    public ArrayList<Tab> getTabs(){
        return tabList;
    }
}
