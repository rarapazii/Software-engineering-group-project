package uk.ac.soton.comp2211.group2.view.Utilities;

import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;

public class ChartPick extends VBox {
    RadioButton chart1;
    RadioButton chart2;
    VBox chartBox;
    Label chartName = new Label("CHARTS:");
    ToggleGroup radioGroup = new ToggleGroup();

    public ChartPick() {
        chartName.getStyleClass().add("titleprop");

        chart1 = new RadioButton("Bar Chart");
        chart1.setToggleGroup(radioGroup);

        chart2 = new RadioButton("Linear Chart");
        chart2.setToggleGroup(radioGroup);

        chartBox = new VBox();
        chartBox.setSpacing(10);

        chartBox.getChildren().addAll(chartName, chart1, chart2);
        chartBox.getStyleClass().add("filter-rectangle");
        getChildren().add(chartBox);
    }

    
    /** 
     * returns the selected chart type
     * bar or linear
     * @return String
     */
    public String getChartType() {
        return ((RadioButton) radioGroup.getSelectedToggle()).getText();
    }

}
