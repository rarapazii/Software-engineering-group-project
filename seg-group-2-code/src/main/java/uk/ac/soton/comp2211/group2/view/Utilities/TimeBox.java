package uk.ac.soton.comp2211.group2.view.Utilities;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class TimeBox extends VBox {
    private StringProperty displayBy = new SimpleStringProperty("Days");
    private VBox timePeriod;
    private TextField timeField;
    private DatePicker dateStart;
    private DatePicker dateEnd;

    public TimeBox () {
        create();
    }

    public void create() {
        var dispBox = new HBox();

        var dispName = new Label("Display By:");
        dispName.getStyleClass().add("titleprop");

        dispBox.getChildren().addAll(dispName, dispHelper("Hour"), dispHelper("Day"), dispHelper("Week"), dispHelper("Month"), dispHelper("Year"), dispHelper("Custom"));
        dispBox.setSpacing(50D);

        var startText = new Label("Start date: ");
        startText.getStyleClass().add("titleprop");
        this.dateStart = new DatePicker();
        dateStart.setPrefSize(150, 10);
        var endText = new Label("End date: ");
        endText.getStyleClass().add("titleprop");
        this.dateEnd = new DatePicker();
        dateEnd.setPrefSize(150, 10);

        var timeBox = new HBox();

        this.timePeriod = new VBox();
        var timeName = new Label("Time Period :");
        timeName.getStyleClass().add("titleprop");
        var slideTime = new Label("Days");
        slideTime.getStyleClass().add("titleprop");
        timePeriod.getChildren().addAll(timeName, slideTime);

        this.timeField = new TextField();

        timeBox.getChildren().addAll(startText, dateStart, endText, dateEnd, timePeriod, timeField);
        timeBox.setSpacing(40D);
        HBox.setHgrow(timeField, Priority.ALWAYS);
        timePeriod.setVisible(false);
        timeField.setVisible(false);

        this.getChildren().addAll(dispBox, timeBox);
        this.getStyleClass().add("filter-rectangle");
        this.setSpacing(10D);
    }

    public void setCustomButtons() {
        timePeriod.setVisible(true);
        timeField.setVisible(true);
    }

    public StackPane dispHelper(String title) {
        var stack = new StackPane();
        var rect = new Rectangle(100D, 40D);
        rect.setArcWidth(10D);
        rect.setArcHeight(10D);
        rect.getStyleClass().add("dark-box");
        var text = new Text(title);
        text.getStyleClass().add("overview");
        stack.getChildren().addAll(rect, text);
        rect.setOnMouseClicked(mouseEvent -> {
            setDisplayBy(title);
            setCustomButtons();
             System.out.println(rect.getStyleClass().toString());
             switch(rect.getStyleClass().toString()){
                 case "dark-box":
                     rect.getStyleClass().remove("dark-box");
                     rect.getStyleClass().add("dark-box-selected");
                     break;
                 case "dark-box-selected":
                     rect.getStyleClass().remove("dark-box-selected");
                     rect.getStyleClass().add("dark-box");
                     break;
             }
         });


        return stack;
    }

    public void setDisplayBy(String title) { this.displayBy.set(title); }

    public Duration getDuration() {
    	String tv = this.displayBy.get();
    	System.out.println("TV string: " + tv);
        if (tv.equalsIgnoreCase("Hour")) {
            return Duration.ofHours(1);
        } else if (tv.equalsIgnoreCase("Day")) {
            return Duration.ofDays(1);
        } else if (tv.equalsIgnoreCase("Week")) {
            return Duration.ofDays(7);
        } else if (tv.equalsIgnoreCase("Month")) {
            return Duration.ofDays(30);
        } else if (tv.equalsIgnoreCase("Year")) {
            return Duration.ofDays(365);
        } else {
            try {
                return Duration.ofDays(Integer.parseInt(this.timeField.getText()));
            } catch (NumberFormatException e) {
            	e.printStackTrace();
                return Duration.ofHours(1);
            }
        }

    }

    public LocalDateTime getStartDate() {
    	LocalDate v = this.dateStart.getValue();
    	if (v == null) {
    		return null;
    	}
    	return v.atStartOfDay(); 
    }

    public LocalDateTime getEndDate () { 
    	LocalDate v = this.dateEnd.getValue();
    	if (v == null) {
    		return null;
    	}
    	return v.atStartOfDay(); 
    }
}
