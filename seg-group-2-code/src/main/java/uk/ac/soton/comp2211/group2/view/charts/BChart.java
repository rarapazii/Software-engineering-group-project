package uk.ac.soton.comp2211.group2.view.charts;

import static org.apache.logging.log4j.core.util.Loader.getClassLoader;

import java.io.FileNotFoundException;
import java.text.ParseException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.function.Predicate;

import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.chart.Axis;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.PieChart.Data;
import javafx.scene.control.Tooltip;
import uk.ac.soton.comp2211.group2.controller.interfaces.ControlAuctionData;
import uk.ac.soton.comp2211.group2.model.IDLog;
import uk.ac.soton.comp2211.group2.view.Utilities.DataOnBoard;
import uk.ac.soton.comp2211.group2.view.MainWindow;

public class BChart {


    //Bar chart template
    //Can create histogram using this template
    @SuppressWarnings("rawtypes")
	private Axis frequency = new NumberAxis();
    private CategoryAxis time = new CategoryAxis();
    private BarChart<String,Integer> bchart;
    private DataOnBoard dataOnBoard = DataOnBoard.getSingletonInstance();
    private MainWindow mainWindow;


    /**
     * The constructor of the barchart class
     *
     * @param xLabel The label of the X-axis
     * @param yLabel The label of the Y-axis
     */
    @SuppressWarnings("unchecked")
	public BChart(String xLabel, String yLabel, MainWindow mainWindow) {
        bchart = new BarChart<String,Integer>(time,frequency);
        time.setLabel(xLabel);
        frequency.setLabel(yLabel);
        frequency.setAnimated(true);
        this.mainWindow = mainWindow;
    }



    /**
     * This method is to add a datalist to the bar chart
     * @param data The data imported from the FileProcessor
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public void createSeries(String title, ObservableList<XYChart.Data> data){
        XYChart.Series series = new XYChart.Series();
        series.setName(title);
        for (XYChart.Data d : data){
            series.getData().add(d);
        }
        bchart.getData().add(series);
    }



    /**
     * Initialize the Barchart
     */
    public void initialize(){
        bchart.setPadding(new Insets(12,12,12,12));
        //bchart.setPrefHeight(500);
       // bchart.setPrefWidth(500);
        bchart.setMinWidth(400);
        bchart.setMinHeight(450);
        bchart.getStylesheets().add(getClassLoader().getResource(mainWindow.getCSS()).toExternalForm());
        setOnHover();
    }

    /**
     * Set the mouse hover effect to the chart
     */
    public void setOnHover() {
        for (XYChart.Series<String, Integer> s : bchart.getData()) {
            for (XYChart.Data<String, Integer> d : s.getData()) {
                Tooltip.install(d.getNode(), new Tooltip(
                        d.getXValue() + "\n" +
                                frequency.getLabel() + " : " + d.getYValue()));

                //Adding class on hover
                d.getNode().setOnMouseEntered(event -> d.getNode().getStyleClass().add("onHover"));

                //Removing class on exit
                d.getNode().setOnMouseExited(event -> d.getNode().getStyleClass().remove("onHover"));
            }
        }
    }



    /**
     * @return Get the instance of a barchart
     */
    public BarChart<String,Integer> getChart() {
        return this.bchart;
    }


    public void filterAndAdd(Duration time, LocalDateTime start, LocalDateTime end, String datatype, String integerOrFloat, String title, Predicate<IDLog> check){
      //  bchart.getData().clear();
        createSeries(title,dataOnBoard.getDatatype(time, start, end, datatype,integerOrFloat,check));
        initialize();
    }


    public void createClickCost(String time,Float delta,Predicate<IDLog> check,String title){
        bchart.getData().clear();
        createSeries(title,dataOnBoard.getClickCost(time,delta,check));
        initialize();
    }

    //to control the width/height of the chart
    public void setWidth(Integer width) { this.bchart.setMaxWidth(width); }

    public void setHeight(Integer height) { this.bchart.setMaxHeight(height); }


}
