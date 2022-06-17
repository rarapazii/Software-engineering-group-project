package uk.ac.soton.comp2211.group2.view.charts;

import static org.apache.logging.log4j.core.util.Loader.getClassLoader;

import java.io.FileNotFoundException;
import java.text.ParseException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.function.Predicate;

import com.sun.tools.javac.Main;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.chart.Axis;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Tooltip;
import uk.ac.soton.comp2211.group2.model.IDLog;
import uk.ac.soton.comp2211.group2.view.*;
import uk.ac.soton.comp2211.group2.view.Utilities.DataOnBoard;


/**
 * This class is the template of the line chart
 */
public class LChart {
    private Axis<String> x = new CategoryAxis();
    private NumberAxis y = new NumberAxis();
    private LineChart<String, Number> lineChart;
    //private String xLabel;
    //private String yLabel;
    //private String title;
    private DataOnBoard dataOnBoard;
    private MainWindow mainWindow;

    /**
     * The getter of the line chart
     *
     * @return Line chart
     */
    public LineChart<String, Number> getChart() {
        return this.lineChart;
    }

    public void initialize() {
        lineChart.setPadding(new Insets(12, 12, 12, 12));
        lineChart.setPrefHeight(400);
        lineChart.setPrefWidth(500);
        lineChart.setMinWidth(400);
        lineChart.setMinHeight(300);
        lineChart.getStylesheets().add(getClassLoader().getResource(mainWindow.getCSS()).toExternalForm());
        setOnHover();

    }

    /**
     * Set the mouse hover effect to the chart
     */
    public void setOnHover() {
        for (XYChart.Series<String, Number> s : lineChart.getData()) {
            for (XYChart.Data<String, Number> d : s.getData()) {
                Tooltip.install(d.getNode(), new Tooltip(
                        d.getXValue() + "\n" +
                                y.getLabel() + " : " + d.getYValue()));
                //Apparently needed null check?
                if (d.getNode() != null) {
	                //Adding class on hover
	                d.getNode().setOnMouseEntered(event -> d.getNode().getStyleClass().add("onHover"));
	
	                //Removing class on exit
	                d.getNode().setOnMouseExited(event -> d.getNode().getStyleClass().remove("onHover"));
                }
            }
        }
    }

    /**
     * The constructor of the line chart class
     *
     * @param xLabel The label of the X-axis
     * @param yLabel The label of the Y-axis
     */
    public LChart(String xLabel, String yLabel, MainWindow mainWindow) {
        x.setLabel(xLabel);
        y.setLabel(yLabel);
        lineChart = new LineChart(x, y);
        dataOnBoard = DataOnBoard.getSingletonInstance();
        this.mainWindow = mainWindow;
    }

    /**
     * This method is to add a datalist to the line chart
     *
     * @param data_integer The data imported from the FileProcessor
     */
    public void createSeries(String title, ObservableList<XYChart.Data> data_integer) {
        XYChart.Series series = new XYChart.Series();
        series.setName(title);
        for (XYChart.Data d : data_integer) {
            series.getData().add(d);
        }
        lineChart.getData().add(series);
    }

    public void filterAndAdd(Duration time, LocalDateTime start, LocalDateTime end, String datatype, String integerOrFloat, String title, Predicate<IDLog> check) {
        //lineChart.getData().clear();
        if (time.equals(Duration.ofHours(1))) {
            lineChart.setCreateSymbols(false);
        }
        // System.out.println(dataOnBoard.getDatatype(time,getChart().getYAxis().getLabel(),integerOrFloat,check));
        createSeries(title, dataOnBoard.getDatatype(time, start, end, datatype, integerOrFloat, check));
        initialize();
    }

    public void changeTime(String time) {

    }
}

