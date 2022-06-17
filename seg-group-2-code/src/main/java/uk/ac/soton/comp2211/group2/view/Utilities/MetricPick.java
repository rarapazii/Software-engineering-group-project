package uk.ac.soton.comp2211.group2.view.Utilities;

import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;

public class MetricPick extends VBox {

    private RadioButton metric1;
    private RadioButton metric2;
    private RadioButton metric3;
    private RadioButton metric4;
    private RadioButton metric5;
    private RadioButton metric6;
    private RadioButton metric7;
    private RadioButton metric8;
    private RadioButton metric9;
    private RadioButton metric10;

    VBox pane;
    Label metricName = new Label("METRICS: ");
    ToggleGroup metricToggle = new ToggleGroup();

    public MetricPick(){
        pane = new VBox();
        metricName.getStyleClass().add("titleprop");
        pane.getChildren().add(metricName);

        metric1 = new RadioButton("CPA");
        metric1.setToggleGroup(metricToggle);
        metric2 = new RadioButton("CPM");
        metric2.setToggleGroup(metricToggle);
        metric3 = new RadioButton("Bounce Rate");
        metric3.setToggleGroup(metricToggle);
        metric4 = new RadioButton("Total Cost");
        metric4.setToggleGroup(metricToggle);
        metric5 = new RadioButton("CTR");
        metric5.setToggleGroup(metricToggle);
        metric6 = new RadioButton("CPC");
        metric6.setToggleGroup(metricToggle);
        metric7 = new RadioButton("Impressions");
        metric7.setToggleGroup(metricToggle);
        metric8 = new RadioButton("Conversions");
        metric8.setToggleGroup(metricToggle);
        metric9 = new RadioButton("Clicks");
        metric9.setToggleGroup(metricToggle);
        metric10 = new RadioButton("Uniques");
        metric10.setToggleGroup(metricToggle);
        
        pane.getStyleClass().add("filter-rectangle");
        pane.getChildren().addAll(metric1,metric2,metric3,metric4,metric5,metric6,metric7,metric8,metric9,metric10);
        getChildren().add(pane);
   }
    
        
        /** 
         * gets the selected metric(s)
         * @return String
         */
        public String getSelectedMetric() {
            if(((RadioButton) metricToggle.getSelectedToggle() ==null ))
            return null;
            return ((RadioButton) metricToggle.getSelectedToggle()).getText();
        }
}
