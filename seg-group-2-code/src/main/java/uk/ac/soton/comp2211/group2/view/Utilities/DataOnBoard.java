package uk.ac.soton.comp2211.group2.view.Utilities;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.function.Predicate;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart;
import uk.ac.soton.comp2211.group2.controller.Pair;
import uk.ac.soton.comp2211.group2.controller.interfaces.ControlAuctionData;
import uk.ac.soton.comp2211.group2.controller.interfaces.ControlFileLoader;
import uk.ac.soton.comp2211.group2.model.IDLog;
import uk.ac.soton.comp2211.group2.view.App;

public class DataOnBoard{
//    private ObservableList<XYChart.Data<String, Integer>> dataFromCSV;
    private ArrayList<Pair<String, Integer>> filter_data;
    private ArrayList<Pair<String, Float>> filter_data_float;
    private ControlFileLoader controlFileLoader;
    private DateTimeFormatter DATE_FORMAT= DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private ArrayList<Pair<Float,Integer>> distribution;
    
    private static DataOnBoard singletonInstance;

    private ControlAuctionData data;
    
    public static DataOnBoard getSingletonInstance() {
		return singletonInstance;
	}
    
	public static void setSingletonInstance(DataOnBoard singletonInstance) {
		DataOnBoard.singletonInstance = singletonInstance;
	}
	
	public DataOnBoard() {
//        dataFromCSV = FXCollections.observableArrayList();
        filter_data = new ArrayList<>();
        filter_data_float = new ArrayList<>();
        controlFileLoader = App.getModel().createFileLoader();
    }
	
	public void resetCalc() {
		controlFileLoader.resetCalcs();
	}
	
	public boolean hasAllFiles() {
		return controlFileLoader.hasFiles();
	}
	
	public void setImpressions(File f) {
		controlFileLoader.setImpressionLogs(f);
	}
	
	public void setClicks(File f) {
		controlFileLoader.setClickLogs(f);
	}
	
	public void setServerLogs(File f) {
		controlFileLoader.setServerLogs(f);
	}
	
	public void computeData() throws IOException {
		data = controlFileLoader.getData();
	}

    /**
     * @return The Data imported from the three csv files
     */
    public ControlAuctionData getDataSource() { return data; }

    public Duration chooseTimeInterval(String time) {
        Duration duration = null;
        switch (time) {
            case "hours":
                duration = Duration.ofHours(1);
                break;
            case "days":
                duration = Duration.ofDays(1);
                break;
            case "weeks":
                duration = Duration.ofDays(7);
                break;
            case "months":
                duration = Duration.ofDays(30);
                break;
            case "years":
                duration = Duration.ofDays(365);
                break;
        }
        return duration;
    }

    public DateTimeFormatter chooseDTF(Duration d) {
        if (d.minusDays(1).isNegative()) { //Duration is less than one day
            return DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        }
        else {
            return DateTimeFormatter.ofPattern("yyyy-MM-dd");
        }
    }

    public ObservableList getDatatype(Duration duration, LocalDateTime start, LocalDateTime end, String datatype,String integerOrFloat,Predicate<IDLog> check) {
        ObservableList<Object> objects = FXCollections.observableArrayList();
        switch (datatype) {
            case "Clicks":
                filter_data = getDataSource().getClicksPerTime(duration, start, end, check);
                break;
            case "Impressions":
                filter_data = getDataSource().getImpressionsPerTime(duration, start, end, check);
                break;
            case "Bounces":
                filter_data = getDataSource().getBouncesPerTime(duration, start, end, check);
                break;
            case "Conversions":
                filter_data = getDataSource().getConversionsPerTime(duration, start, end, check);
                break;
            case "Uniques":
                filter_data = getDataSource().getUniquesPerTime(duration, start, end, check);
                break;
            case "CTR":
                filter_data_float = getDataSource().getCTRPerTime(duration, start, end, check);
                break;
            case "CPA":
                filter_data_float = getDataSource().getCPAPerTime(duration, start, end, check);
                break;
            case "CPC":
                filter_data_float = getDataSource().getCPCPerTime(duration, start, end, check);
                break;
            case "CPM":
                filter_data_float = getDataSource().getCPMPerTime(duration, start, end, check);
                break;
            case "Bounce Rate":
                filter_data_float = getDataSource().getBounceRatePerTime(duration, start, end, check);
                break;
            case "Total Cost":
                filter_data_float = getDataSource().getTotalCostPerTime(duration, start, end, check);
                break;
            default:
                System.out.println("No such datatype");
                break;
        }
        if (integerOrFloat.equals("Integer")) {
//            dataFromCSV.clear();
            for (Pair<String, Integer> filter_datum : filter_data) {
                LocalDateTime localDateTime = LocalDateTime.parse(filter_datum.getFirst());
                String localDate = chooseDTF(duration).format(localDateTime);
                filter_datum.setFirst(localDate);
            }
            for (Pair<String, Integer> pair : filter_data) {
                objects.add(new XYChart.Data(pair.getFirst(), pair.getSecond()));
            }
//            createSeries(title,dataFromCSV);
//            System.out.println(dataFromCSV.size());
        }
        else if (integerOrFloat.equals("Float")) {
//            dataFromCSV2.clear();
            for (Pair<String, Float> stringFloatPair : filter_data_float) {
                LocalDateTime localDateTime = LocalDateTime.parse(stringFloatPair.getFirst());
                String localDate = chooseDTF(duration).format(localDateTime);
                stringFloatPair.setFirst(localDate);
            }
            for (Pair<String, Float> pair : filter_data_float) {
                objects.add(new XYChart.Data(pair.getFirst(), pair.getSecond()));
            }
            //            createSeries_float(title,dataFromCSV2);
//            System.out.println(dataFromCSV2.size());
        }else {
            System.out.println("Please use int or float");
        }
        return objects;
    }

    public ObservableList getClickCost(String time,Float delta,Predicate<IDLog> check) {
        ObservableList objects = FXCollections.observableArrayList();
        Duration duration = chooseTimeInterval(time);
        distribution = getDataSource().getClickCostDistribution(duration,delta,check);
        for (Pair<Float, Integer> pair : distribution) {
            objects.add(new XYChart.Data(pair.getFirst().toString(), pair.getSecond()));
        }
        return objects;
    }
}
